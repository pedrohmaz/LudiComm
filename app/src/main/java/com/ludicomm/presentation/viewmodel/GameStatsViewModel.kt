package com.ludicomm.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ludicomm.data.model.Match
import com.ludicomm.data.repository.AuthRepository
import com.ludicomm.data.repository.FirestoreRepository
import com.ludicomm.util.formatDecimal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class GameStatsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    private val _game = MutableStateFlow("")
    val game = _game.asStateFlow()

    private val _matchList = MutableStateFlow<List<Match>>(listOf())
    val matchList = _matchList.asStateFlow()

    private val _winnerList = MutableStateFlow<List<Pair<String, Int>>>(listOf())
    val winnerList = _winnerList.asStateFlow()

    val user = authRepository.currentUser()?.displayName ?: ""

    fun getGame(gameName: String) {
        _game.value = gameName
    }

    suspend fun getAllUserGameMatches() {
        _matchList.value = firestoreRepository.getAllUserGameMatches(user, _game.value)
            .sortedByDescending { it.dateAndTime }
    }

    fun computeTotalWins(): Int {
        var counter = 0
        _matchList.value.forEach { match ->
            if (match.winners.contains(user)) counter++
        }
        return counter
    }

    fun computeWinsPerPlayer() {
        val winnerList = mutableListOf<String>()
        val winnerFinalList = mutableListOf<Pair<String, Int>>()

        _matchList.value.forEach { match ->
            winnerList.addAll(match.winners)
        }
        winnerList.forEach { winner ->
            if (winnerFinalList.all { entry -> entry.first != winner } || winnerFinalList.isEmpty()) {
                winnerFinalList.add(Pair(winner, winnerList.count { it == winner }))
            }
        }
        _winnerList.value = winnerFinalList.sortedBy { it.second }
    }

    fun computeAveragePoints(): String {
        var counter = 0f
        Log.i("TAG", "computeAverageScore: ${_matchList.value}")
        _matchList.value.forEach { match ->
            match.playerDataList.forEach { playerData ->
                if (playerData.name == user) {
                    counter += playerData.score.toFloat()
                }
            }
        }

        return (counter / _matchList.value.size).formatDecimal()
    }

    fun getBestScore(): Int {
        val scoreList = mutableListOf<Int>()
        _matchList.value.forEach { match ->
            match.playerDataList.forEach { playerData ->
                if (playerData.name == user) {
                    scoreList.add(playerData.score.toInt())
                }
            }
        }
        return scoreList.sortedByDescending { it } [0]
    }

    fun signOut(navigate: () -> Unit) {
        authRepository.signOutUser()
        navigate()
    }

}