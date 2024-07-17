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
class FriendsViewModel @Inject constructor(
    authRepository: AuthRepository,
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    private val _friendsList = MutableStateFlow(listOf<String>())
    val friendsList = _friendsList.asStateFlow()

    private val _sentRequestList = MutableStateFlow(listOf<String>())
    val sentRequestList = _sentRequestList.asStateFlow()

    private val _receivedRequestList = MutableStateFlow(listOf<String>())
    val receivedRequestList = _receivedRequestList.asStateFlow()

    private val _userQueryInput = MutableStateFlow("")
    val userQueryInput = _userQueryInput.asStateFlow()

    val username = authRepository.currentUser()?.displayName ?: ""

    init {
        updateCurrentUserData()
    }

    private fun updateCurrentUserData(){
        viewModelScope.launch {
            val user = firestoreRepository.getUser(username)
            if (user != null) {
                _friendsList.value = user.friends
                _sentRequestList.value = user.pendingRequestsSent
                _receivedRequestList.value = user.pendingRequestsReceived
            }
        }
    }

    fun changeInput(value: String) {
        _userQueryInput.value = value
    }

    fun requestFriend(){
        viewModelScope.launch {
            firestoreRepository.requestFriend(
                _userQueryInput.value,
                username
            )
        }
    }

}