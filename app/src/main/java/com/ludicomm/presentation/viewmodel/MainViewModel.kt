package com.ludicomm.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ludicomm.data.model.BoardGames
import com.ludicomm.data.model.Collection
import com.ludicomm.data.repository.AuthRepository
import com.ludicomm.data.repository.BGGRepository
import com.ludicomm.data.repository.FirestoreRepository
import com.ludicomm.util.RegistrationUtil
import com.ludicomm.util.stateHandlers.Resource
import com.ludicomm.util.stateHandlers.SignUpState
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
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {


    private val _boardGames = MutableStateFlow(BoardGames("", emptyList()))
    val boardGames = _boardGames.asStateFlow()

    private val _collection = MutableStateFlow(Collection())
    val collection = _collection.asStateFlow()

    private val _username = MutableStateFlow(authRepository.currentUser()?.displayName)
    val username = _username.asStateFlow()

    private val _newPasswordInput = MutableStateFlow("")
    val newPasswordInput = _newPasswordInput.asStateFlow()

    private val _confirmNewPasswordInput = MutableStateFlow("")
    val confirmNewPasswordInput = _confirmNewPasswordInput.asStateFlow()

    private val _isPasswordConfirmationNeeded = MutableStateFlow(false)
    val isPasswordConfirmationNeeded = _isPasswordConfirmationNeeded.asStateFlow()

    private val _state = MutableStateFlow(SignUpState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            isPasswordConfirmationNeeded()
        }
    }

    fun resetState() {
        _state.value = SignUpState()
    }

    fun changeNewPasswordInput(value: String) {
        _newPasswordInput.value = value
    }

    fun changeConfirmNewPasswordInput(value: String) {
        _confirmNewPasswordInput.value = value
    }

    fun isAccountVerified(): Boolean? {
        return authRepository.currentUser()?.isEmailVerified
    }

    private suspend fun isPasswordConfirmationNeeded() {
        val user = firestoreRepository.getUser(_username.value!!)
        _isPasswordConfirmationNeeded.value = user?.confirmPassword == true
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

    fun submitNewPassword() {
        viewModelScope.launch {
            val newPassword = _newPasswordInput.value ?: ""
            val confirmPassword = _confirmNewPasswordInput.value ?: ""

            RegistrationUtil.validateNewPassword(newPassword, confirmPassword).collect { result ->

                when (result) {
                    is Resource.Success -> {
                        val currentUser = authRepository.currentUser()
                        if (currentUser != null) {
                            currentUser.updatePassword(newPassword)
                                .addOnSuccessListener {
                                    viewModelScope.launch {
                                        firestoreRepository.toggleConfirmPassword(currentUser.email!!, false)
                                    }
                                    _state.value = SignUpState(isSuccess = "Password confirmed")
                                    _isPasswordConfirmationNeeded.value = false
                                }
                                .addOnFailureListener { exception ->
                                    _state.value = SignUpState(isError = "Could not update password: ${exception.message}")
                                }
                        } else {
                            _state.value = SignUpState(isError = "User not authenticated")
                        }
                    }
                    is Resource.Error -> {
                        _state.value = SignUpState(isError = result.message.toString())
                    }

                    is Resource.Loading -> {}
                }
            }
        }
    }

        fun signOut(navigate: () -> Unit) {
            authRepository.signOutUser()
            navigate()
        }



}





