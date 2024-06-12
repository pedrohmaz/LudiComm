package com.ludicomm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ludicomm.data.repository.AuthRepository
import com.ludicomm.util.InputField
import com.ludicomm.util.LogInState
import com.ludicomm.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _logInState = MutableStateFlow(LogInState())
    val logInState = _logInState.asStateFlow()

    private val _emailInput = MutableStateFlow("")
    val emailInput = _emailInput.asStateFlow()

    private val _passwordInput = MutableStateFlow("")
    val passwordInput = _passwordInput.asStateFlow()

    fun isUserLoggedIn(): Boolean {
        return authRepository.isUserLoggedIn()
    }

    fun changeInput(inputField: InputField, text: String) {
        when (inputField) {
            InputField.Email -> _emailInput.value = text
            InputField.Password -> _passwordInput.value = text
            else -> Unit
        }
    }

    fun loginUser(email: String, password: String) = viewModelScope.launch {
        authRepository.loginUser(email, password).collect { result ->
            when (result) {

                is Resource.Success -> {
                    _logInState.value = LogInState(isSuccess = "Login Success")
                }

                is Resource.Loading -> {
                    _logInState.value = LogInState(isLoading = true)
                }

                is Resource.Error -> {
                    _logInState.value = LogInState(isError = result.message)
                }
            }
        }
    }



}