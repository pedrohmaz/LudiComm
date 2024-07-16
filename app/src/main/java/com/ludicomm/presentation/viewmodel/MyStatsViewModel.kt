package com.ludicomm.presentation.viewmodel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.ludicomm.data.model.Match
import com.ludicomm.data.repository.AuthRepository
import com.ludicomm.data.repository.FirestoreRepository
import com.ludicomm.presentation.theme.DarkBluePlayer
import com.ludicomm.presentation.theme.DarkGreenPlayer
import com.ludicomm.presentation.theme.GreyPlayer
import com.ludicomm.presentation.theme.LightBluePlayer
import com.ludicomm.presentation.theme.LightGreenPlayer
import com.ludicomm.presentation.theme.OrangePlayer
import com.ludicomm.presentation.theme.PinkPlayer
import com.ludicomm.presentation.theme.PurplePlayer
import com.ludicomm.presentation.theme.RedPlayer
import com.ludicomm.presentation.theme.YellowPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

val chartColorList = listOf(
    RedPlayer,
    DarkBluePlayer,
    DarkGreenPlayer,
    YellowPlayer,
    PurplePlayer,
    OrangePlayer,
    PinkPlayer,
    LightBluePlayer,
    LightGreenPlayer,
    GreyPlayer
)

@HiltViewModel
class MyStatsViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    authRepository: AuthRepository
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


    // chart vars
    private val _pieChartConfig = MutableStateFlow(
        PieChartConfig(
            backgroundColor = Color.Transparent,
            showSliceLabels = false,
            isAnimationEnable = true,
            animationDuration = 1300,
        )
    )
    val pieChartConfig = _pieChartConfig.asStateFlow()

    private val _winLossChartData = MutableStateFlow(
        PieChartData(
            slices = listOf(),
            plotType = PlotType.Pie
        )
    )
    val winLossChartData = _winLossChartData.asStateFlow()

    private val _gamesChartData = MutableStateFlow(
        PieChartData(
            slices = listOf(),
            plotType = PlotType.Pie
        )
    )
    val gamesChartData = _gamesChartData.asStateFlow()

    private val _playersChartData = MutableStateFlow(
        PieChartData(
            slices = listOf(),
            plotType = PlotType.Pie
        )
    )
    val playersChartData = _playersChartData.asStateFlow()


    init {
        viewModelScope.launch {
            getAllUserMatches()
            computeStats()
            populateCharts()
        }
    }

    private suspend fun getAllUserMatches() {
        _matchList.value =
            firestoreRepository.getAllUserMatches(user)
        _matchList.value = _matchList.value.sortedByDescending { it.dateAndTime }
    }

    private fun computeStats() {
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


        if (gamesPlayed.size < 3) {
            val copyList = gamesPlayed.toList().toMutableList()
            for (i in 1..3 - copyList.size) {
                copyList.add(Pair("No game", 0))
            }
            _gamesMostPlayed.value = copyList.toList().sortedByDescending { it.second }
        } else _gamesMostPlayed.value = gamesPlayed.toList().sortedByDescending { it.second }

        Log.i("indexTAG", "computeGamesMostPlayed: ${_gamesMostPlayed.value}")

    }

    private fun computePlayersMostPlayed() {
        val playersPlayed = mutableListOf<String>()
        val playerMap = mutableMapOf<String, Int>()
        _matchList.value.forEach { match ->
            match.playerNames.forEach { player ->
                playersPlayed.add(player)
            }
        }

        playersPlayed.forEach { player ->
            if (!playerMap.containsKey(player) && player != user) {
                val count = playersPlayed.count { it == player }
                playerMap[player] = count
            }
        }

        if (playerMap.size < 3) {
            val copyList = playerMap.toList().toMutableList()
            for (i in 1..3 - copyList.size) {
                copyList.add(Pair("No player", 0))
            }
            _playersMostPlayed.value = copyList.toList().sortedByDescending { it.second }
        } else _playersMostPlayed.value = playerMap.toList().sortedByDescending { it.second }

        Log.i("indexTAG", "computePlayersMostPlayed: ${_playersMostPlayed.value}")

    }

    private fun populateCharts() {

        _winLossChartData.value = PieChartData(
            listOf(
                PieChartData.Slice(
                    "Wins: ${(_totalWins.value * 100f / _totalMatches.value).formatToSinglePrecision()}%",
                    _totalWins.value.toFloat(),
                    RedPlayer
                ),
                PieChartData.Slice(
                    "Losses: ${((_totalMatches.value - _totalWins.value) * 100f / _totalMatches.value).formatToSinglePrecision()}%",
                    (_totalMatches.value - _totalWins.value).toFloat(), DarkBluePlayer
                )
            ),
            plotType = PlotType.Pie
        )

        val gameChartSliceList = mutableListOf<PieChartData.Slice>()
        for (i in 0..<_gamesMostPlayed.value.size) {
            if (i < 10) {
                val ellipsis = if(_gamesMostPlayed.value[i].first.length > 15) "..." else ""
                gameChartSliceList.add(
                    PieChartData.Slice(
                        "${_gamesMostPlayed.value[i].first.take(15)}$ellipsis",
                        _gamesMostPlayed.value[i].second.toFloat(),
                        chartColorList[i]
                    )
                )
            }
        }
        _gamesChartData.value = PieChartData(
            gameChartSliceList,
            plotType = PlotType.Pie
        )

        val playerChartSliceList = mutableListOf<PieChartData.Slice>()
        for (i in 0..<_playersMostPlayed.value.size) {
            if (i < 10) {
                val ellipsis = if(_playersMostPlayed.value[i].first.length > 15) "..." else ""
                playerChartSliceList.add(
                    PieChartData.Slice(
                        "${_playersMostPlayed.value[i].first.take(15)}$ellipsis",
                        _playersMostPlayed.value[i].second.toFloat(),
                        chartColorList[i]
                    )
                )
            }
        }
        _playersChartData.value = PieChartData(
            playerChartSliceList,
            plotType = PlotType.Pie
        )
    }

}