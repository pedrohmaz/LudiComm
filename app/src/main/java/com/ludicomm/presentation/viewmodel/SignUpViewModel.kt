package com.ludicomm.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ludicomm.data.repository.AuthRepository
import com.ludicomm.data.repository.FirestoreRepository
import com.ludicomm.util.SignUpInputFields
import com.ludicomm.util.RegistrationUtil
import com.ludicomm.util.stateHandlers.Resource
import com.ludicomm.util.stateHandlers.SignUpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState = _signUpState.asStateFlow()

    private val _usernameInput = MutableStateFlow("")
    val usernameInput = _usernameInput.asStateFlow()

    private val _emailInput = MutableStateFlow("")
    val emailInput = _emailInput.asStateFlow()

    private val _passwordInput = MutableStateFlow("")
    val passwordInput = _passwordInput.asStateFlow()

    private val _confirmPasswordInput = MutableStateFlow("")
    val confirmPasswordInput = _confirmPasswordInput.asStateFlow()


    fun changeInput(inputField: SignUpInputFields, text: String) {
        when (inputField) {
            SignUpInputFields.Email -> _emailInput.value = text
            SignUpInputFields.Password -> _passwordInput.value = text
            SignUpInputFields.Username -> _usernameInput.value = text
            SignUpInputFields.ConfirmPassword -> _confirmPasswordInput.value = text
        }
    }

    fun resetState() {
        _signUpState.value = SignUpState()
    }

    suspend fun registerUser(username: String, email: String, password: String) =
        viewModelScope.launch {
            RegistrationUtil.validateUserRegistration(
                username,
                email,
                password,
                _confirmPasswordInput.value
            ).collect { validationResult ->
                if (validationResult is Resource.Success) {
                    authRepository.registerUser(username, email, password).collect { authResult ->
                        when (authResult) {
                            is Resource.Success -> {
                                firestoreRepository.registerUser(
                                    authRepository.currentUser()?.uid.toString(),
                                    username
                                ).collect { firestoreResult ->
                                    if (firestoreResult is Resource.Success) {
                                        _signUpState.value = SignUpState(isSuccess = "User Created")
                                    } else {
                                        authRepository.currentUser()?.delete()
                                        _signUpState.value =
                                            SignUpState(isError = "Could not register user: ${firestoreResult.message}")
                                    }
                                }
                            }

                            is Resource.Loading -> {
                                _signUpState.value = SignUpState(isLoading = true)
                            }

                            is Resource.Error -> {
                                _signUpState.value = SignUpState(isError = authResult.message.toString())
                            }
                        }
                    }
                } else _signUpState.value = SignUpState(isError = validationResult.message.toString())
            }
        }

}



