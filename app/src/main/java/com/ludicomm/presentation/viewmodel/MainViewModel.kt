package com.ludicomm.presentation.viewmodel

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

    private val _emailSent = MutableStateFlow(false)
    val emailSent = _emailSent.asStateFlow()

    fun isAccountVerified(): Boolean? {
        return authRepository.currentUser()?.isEmailVerified
    }

    private fun searchGames(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            //try {
            val result = repo.getBoardGames(name)
            _boardGames.value = result
            // } catch (e: Exception) {
            // _boardGames.value =  BoardGames()
            // }
        }
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
        return try {
            authRepository.currentUser()?.sendEmailVerification()?.await()
            _emailSent.value = true
            signOut(navigate)
            "Email sent. Please check your email inbox."
        } catch (e: Exception) {
            "Could not send email. Please check your internet connection."
        }
    }

    fun signOut(navigate: () -> Unit){
        authRepository.signOutUser()
        navigate()
    }


}





