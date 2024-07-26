package com.ludicomm.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.ludicomm.data.model.Match
import com.ludicomm.data.repository.AuthRepository
import com.ludicomm.data.repository.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
            if(match.winners.contains(user)) counter++
        }
        return counter
    }

    fun signOut(navigate: () -> Unit) {
        authRepository.signOutUser()
        navigate()
    }

}