package com.ludicomm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.ludicomm.data.repository.AuthRepository
import com.ludicomm.data.repository.FirestoreRepository
import com.ludicomm.util.InputField
import com.ludicomm.util.Resource
import com.ludicomm.util.SignUpState
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


    fun changeInput(inputField: InputField, text: String) {
        when (inputField) {
            InputField.Email -> _emailInput.value = text
            InputField.Password -> _passwordInput.value = text
            InputField.Username -> _usernameInput.value = text
        }
    }

    suspend fun registerUser(username: String, email: String, password: String) = viewModelScope.launch {
        authRepository.registerUser(username, email, password).collect { result ->
            when (result) {

                is Resource.Success -> {
                    firestoreRepository.registerUser(authRepository.currentUser()?.uid.toString())
                        .collect {
                            if (it is Resource.Success) _signUpState.value =
                                SignUpState(isSuccess = "User Created")
                            else {
                                authRepository.currentUser()?.delete()
                                _signUpState.value = SignUpState(isError = "Could not register user: ${it.message}")
                            }
                        }
                }

                is Resource.Loading -> {
                    _signUpState.value = SignUpState(isLoading = true)
                }

                is Resource.Error -> {
                    _signUpState.value = SignUpState(isError = result.message)
                }

            }
        }
    }


}