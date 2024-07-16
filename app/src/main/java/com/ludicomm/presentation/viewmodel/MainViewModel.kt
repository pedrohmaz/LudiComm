package com.ludicomm.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ludicomm.data.model.BoardGames
import com.ludicomm.data.model.Collection
import com.ludicomm.data.repository.AuthRepository
import com.ludicomm.data.repository.BGGRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: BGGRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {


    private val _boardGames = MutableStateFlow(BoardGames("", emptyList()))
    val boardGames = _boardGames.asStateFlow()

    private val _collection = MutableStateFlow(Collection())
    val collection = _collection.asStateFlow()

    private val _username = MutableStateFlow(authRepository.currentUser()?.displayName)
    val username = _username.asStateFlow()


    fun isAccountVerified(): Boolean? {
        return authRepository.currentUser()?.isEmailVerified
    }

    private fun searchCollection(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var result = repo.getCollection(name)
                while (result.boardGames.isEmpty()) {
                    result = repo.getCollection(name)
                    if (result.boardGames.isEmpty()) {
                        delay(2000)
                    }
                }
                _collection.value = result
            } catch (e: Exception) {
                _boardGames.value = BoardGames("", emptyList())
            }
        }
    }

    suspend fun sendEmailVerification(navigate: () -> Unit): String {
        var message = "Email sent. Please check your email inbox."
        viewModelScope.launch {
        try {
            authRepository.currentUser()?.sendEmailVerification()?.await()
            signOut(navigate)
        } catch (e: Exception) {
           message = "Could not send email. Please check your internet connection."
        }
    }
        return message
}

    fun signOut(navigate: () -> Unit){
        authRepository.signOutUser()
        navigate()
    }


}





