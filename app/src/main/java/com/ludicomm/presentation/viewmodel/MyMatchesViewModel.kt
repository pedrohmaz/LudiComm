package com.ludicomm.presentation.viewmodel

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
class MyMatchesViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _matchList = MutableStateFlow(listOf<Match>())
    val matchList = _matchList.asStateFlow()

   private val _noMatches = MutableStateFlow(false)
    val noMatches = _noMatches.asStateFlow()

    private val _toggleConfirmDeleteDialog = MutableStateFlow(Pair(false, ""))
    val toggleConfirmDeleteDialog = _toggleConfirmDeleteDialog.asStateFlow()

    init {
        viewModelScope.launch {
           getAllUserMatches()
        }
    }

    private suspend fun getAllUserMatches()  {
        _matchList.value =
            firestoreRepository.getAllUserMatches(authRepository.currentUser()?.displayName.toString())
        _matchList.value = _matchList.value.sortedByDescending { it.dateAndTime }
        if (_matchList.value.isEmpty()) _noMatches.value = true
    }

    fun toggleConfirmDeleteDialog(boolean: Boolean, dateAndTime: String){
        _toggleConfirmDeleteDialog.value = Pair(boolean, dateAndTime)
    }

    fun deleteMatch(dateAndTime: String) {
        viewModelScope.launch {
            firestoreRepository.deleteMatch(dateAndTime)
            getAllUserMatches()
        }
    }

}