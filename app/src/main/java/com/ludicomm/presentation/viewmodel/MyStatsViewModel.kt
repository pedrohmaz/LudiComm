package com.ludicomm.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ludicomm.data.model.Match
import com.ludicomm.data.repository.AuthRepository
import com.ludicomm.data.repository.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyStatsViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _matchList = MutableStateFlow(listOf<Match>())
    val matchList = _matchList.asStateFlow()

    private val _totalMatches = MutableStateFlow(-1)
    val totalMatches = _totalMatches.asStateFlow()

    private val _totalWins = MutableStateFlow(0)
    val totalWins = _totalWins.asStateFlow()

    private val _gamesMostPlayed = MutableStateFlow<List<Pair<String, Int>>>(listOf())
    val gamesMostPlayed = _gamesMostPlayed.asStateFlow()

    private val _playersMostPlayed = MutableStateFlow<List<Pair<String, Int>>>(listOf())
    val playersMostPlayed = _playersMostPlayed.asStateFlow()


    val user = authRepository.currentUser()?.displayName.toString()

    init {
        viewModelScope.launch {
            getAllUserMatches()
            computeStats()
        }
    }

    private suspend fun getAllUserMatches() {
        _matchList.value =
            firestoreRepository.getAllUserMatches(user)
        _matchList.value = _matchList.value.sortedByDescending { it.dateAndTime }
    }

    private suspend fun computeStats() {
        _totalMatches.value = _matchList.value.size
        var winCounter = 0
        _matchList.value.forEach {
            if (it.winners.contains(user)) winCounter++
        }
        _totalWins.value = winCounter
        computeGamesMostPlayed()
        computePlayersMostPlayed()
    }

    private fun computeGamesMostPlayed() {
        val gamesPlayed = mutableMapOf<String, Int>()
        _matchList.value.forEach { match ->
            if (!gamesPlayed.containsKey(match.game)) {
                val count = _matchList.value.count { it.game == match.game }
                gamesPlayed[match.game] = count
            }
        }

        _gamesMostPlayed.value = gamesPlayed.toList().sortedByDescending { it.second }

    }

    private fun computePlayersMostPlayed() {
        val playersPlayed = mutableListOf<String>()
        val playerMap = mutableMapOf<String, Int>()
        _matchList.value.forEach { match ->
            match.playerNames.forEach { player ->
                playersPlayed.add(player)
            }
        }
        Log.i("TAG", "allPlayerList: $playersPlayed")

        playersPlayed.forEach {player ->
            if (!playerMap.containsKey(player) && player != user) {
                val count = playersPlayed.count{ it == player }
                playerMap[player] = count
            }
        }

        Log.i("TAG", "playerMap: $playerMap")

        _playersMostPlayed.value = playerMap.toList().sortedByDescending { it.second }
        Log.i("TAG", "finalList: ${_playersMostPlayed.value}")

    }

}