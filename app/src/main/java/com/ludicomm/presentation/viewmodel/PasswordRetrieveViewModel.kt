package com.ludicomm.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ludicomm.data.repository.AuthRepository
import com.ludicomm.data.repository.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordRetrieveViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    private val _emailInput = MutableStateFlow("")
    val emailInput = _emailInput.asStateFlow()

    fun changeInput(value: String) {
        _emailInput.value = value
    }

    fun sendPasswordRetrievalEmail() {
        viewModelScope.launch {
                firestoreRepository.toggleConfirmPassword(_emailInput.value, true)
                authRepository.sendPasswordRetrievalEmail(_emailInput.value)
        }
    }

}