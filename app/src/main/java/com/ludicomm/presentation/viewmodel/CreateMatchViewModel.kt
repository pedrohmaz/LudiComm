package com.ludicomm.presentation.viewmodel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ludicomm.data.model.Match
import com.ludicomm.data.model.PlayerMatchData
import com.ludicomm.data.repository.BGGRepository
import com.ludicomm.data.repository.FirestoreRepository
import com.ludicomm.util.CreateMatchInputFields
import com.ludicomm.util.RegistrationUtil
import com.ludicomm.util.stateHandlers.CreateMatchState
import com.ludicomm.util.stateHandlers.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateMatchViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val bggRepository: BGGRepository
) :
    ViewModel() {

    private val _state = MutableStateFlow(CreateMatchState())
    val state = _state.asStateFlow()

    private val _suggestionList = MutableStateFlow<List<List<String>>>(listOf())
    val suggestionList = _suggestionList.asStateFlow()

    private val _playerList = MutableStateFlow<MutableList<PlayerMatchData>>(mutableListOf())
    val playerList = _playerList.asStateFlow()

    private val _gameQueryInput = MutableStateFlow("")
    val gameQueryInput = _gameQueryInput.asStateFlow()

    private val _gameThumbnail = MutableStateFlow("")
    val gameThumbnail = _gameThumbnail.asStateFlow()

    private val _nameInput = MutableStateFlow("")
    val nameInput = _nameInput.asStateFlow()

    private val _colorOrFactionInput = MutableStateFlow("")
    val colorOrFactionInput = _colorOrFactionInput.asStateFlow()

    private val _scoreInput = MutableStateFlow("")
    val scoreInput = _scoreInput.asStateFlow()

    private val _selectedColor = MutableStateFlow<Color?>(null)
    val selectedColor = _selectedColor.asStateFlow()

    private val _toggleSuggestionList = MutableStateFlow(false)
    val toggleSuggestionList = _toggleSuggestionList.asStateFlow()

    private val _toggleNoWinnerDialog = MutableStateFlow(false)
    val toggleNoWinnerDialog = _toggleNoWinnerDialog.asStateFlow()

    private var lastGameCLicked = ""

    private var onGoingQuery = false

    fun setLastGameCLicked(string: String) {
        lastGameCLicked = string
    }

    fun changeInput(newValue: String, inputField: CreateMatchInputFields) {
        when (inputField) {
            CreateMatchInputFields.GameQuery -> _gameQueryInput.value = newValue
            CreateMatchInputFields.Name -> _nameInput.value = newValue
            CreateMatchInputFields.ColorOrFaction -> _colorOrFactionInput.value = newValue
            CreateMatchInputFields.Score -> _scoreInput.value = newValue
        }
    }

    fun changeSelectedColor(color: Color) {
        _selectedColor.value = color
    }

    fun changeGameThumbnail(uri: String) {
        _gameThumbnail.value = uri
    }

    fun clearInputs() {
        _nameInput.value = ""
        _colorOrFactionInput.value = ""
        _scoreInput.value = ""
        _selectedColor.value = null
    }

    fun addPlayer(player: PlayerMatchData) {
        _playerList.value += player
    }

    fun editPlayer(index: Int, newPlayer: PlayerMatchData) {
        _playerList.value[index] = newPlayer
    }

    fun deletePlayer(player: PlayerMatchData) {
        _playerList.value = _playerList.value.filter { it != player }.toMutableList()
    }

    fun toggleSuggestionList(value: Boolean) {
        _toggleSuggestionList.value = value
    }

    fun toggleNoWinnerDialog(value: Boolean) {
        _toggleNoWinnerDialog.value = value
    }

    fun toggleOnGoingQuery(state: Boolean){
        onGoingQuery = state
    }

    fun createBGGSuggestions() {
        viewModelScope.launch {
            delay(800)
            if (_gameQueryInput.value.isNotBlank() && _gameQueryInput.value.length > 2
                && _gameQueryInput.value != lastGameCLicked && !onGoingQuery
            ) {
                onGoingQuery = true
                _toggleSuggestionList.value = true
                bggRepository.getBoardGames(_gameQueryInput.value).collect { result ->
                    when (result) {
                        is Resource.Success -> {

                            val gameList = mutableListOf<List<String>>()
                            val newList = mutableListOf<List<String>>()
                            var counter = 0
                            result.data?.boardGames?.forEach { boardGame ->
                                    boardGame.name?.value?.let { value ->
                                        gameList.add(listOf(value, boardGame.id ?: ""))
                                    }
                                }
                            gameList.sortBy { it[0].length }
                            val sortedGameList = gameList.take(10)
                            sortedGameList.forEach { listItem ->
                                    val uri =
                                        bggRepository.getBoardGame(listItem[1]).boardGames?.get(0)?.thumbnail
                                            ?: ""
                                    newList.add(listOf(listItem[0], uri))
                                    _suggestionList.value = newList
                            }
                            _state.value =
                                CreateMatchState(isSuccess = result.message.toString())
                            onGoingQuery = false
                        }

                        is Resource.Error -> {
                            _suggestionList.value = mutableListOf()
                            _toggleSuggestionList.value = false
                            _state.value = CreateMatchState(isError = result.message.toString())
                            onGoingQuery = false
                        }

                        is Resource.Loading -> _state.value =
                            CreateMatchState(isLoading = true)
                    }
                }
            } else {
                _suggestionList.value = emptyList()
                onGoingQuery = false
            }
        }

    }


    fun resetState() {
        _state.value = CreateMatchState()
    }

    fun updatePlayerList(){
        val copyList = mutableListOf<PlayerMatchData>()
        copyList.addAll(_playerList.value)
        _playerList.value.clear()
        _playerList.value = copyList
    }

    fun submitMatch() {
        viewModelScope.launch(Dispatchers.IO) {
            val nameList = mutableListOf<String>()
            val winnerList = mutableListOf<String>()
            _playerList.value.forEach {
                nameList.add(it.name)
                if (it.isWinner) winnerList.add(it.name)
            }
            val result =
                RegistrationUtil.validateMatchSubmission(
                    _gameQueryInput.value,
                    lastGameCLicked,
                    _gameQueryInput.value,
                    _playerList.value
                )
            if (result is Resource.Success) {
                if (winnerList.isNotEmpty()) {
                    val match = Match(
                        game = _gameQueryInput.value,
                        dateAndTime = System.currentTimeMillis().toString(),
                        numberOfPlayers = _playerList.value.size,
                        playerNames = nameList.toList(),
                        winners = winnerList
                    )
                    playerList.value.forEach { match.playerDataList.add(it) }
                    firestoreRepository.submitMatch(match).collect { firestoreResult ->
                        when (firestoreResult) {
                            is Resource.Error -> _state.value =
                                CreateMatchState(isError = firestoreResult.message.toString())

                            is Resource.Loading -> {}
                            is Resource.Success -> _state.value =
                                CreateMatchState(isSuccess = "Match submitted successfully")
                        }
                    }
                } else _toggleNoWinnerDialog.value = true
            } else _state.value = CreateMatchState(isError = result.message.toString())
        }
    }

    fun submitNoWinnerMatch() {
        viewModelScope.launch(Dispatchers.IO) {
            val nameList = mutableListOf<String>()
            val winnerList = mutableListOf<String>()
            _playerList.value.forEach {
                nameList.add(it.name)
            }
            val match = Match(
                game = _gameQueryInput.value,
                dateAndTime = System.currentTimeMillis().toString(),
                numberOfPlayers = _playerList.value.size,
                playerNames = nameList.toList(),
                winners = winnerList
            )
            playerList.value.forEach { match.playerDataList.add(it) }
            firestoreRepository.submitMatch(match).collect { firestoreResult ->
                when (firestoreResult) {
                    is Resource.Error -> _state.value =
                        CreateMatchState(isError = firestoreResult.message.toString())

                    is Resource.Loading -> {}
                    is Resource.Success -> _state.value =
                        CreateMatchState(isSuccess = "Match submitted successfully")
                }
            }
        }
    }

}


