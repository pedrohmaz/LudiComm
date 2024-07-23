package com.ludicomm.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.firestore.ListenerRegistration
import com.ludicomm.data.model.User
import com.ludicomm.data.repository.AuthRepository
import com.ludicomm.data.repository.FirestoreRepository
import com.ludicomm.util.stateHandlers.FriendsState
import com.ludicomm.util.stateHandlers.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
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

    private val _state = MutableStateFlow(FriendsState())
    val state = _state.asStateFlow()

    private val _toggleConfirmDeleteDialog = MutableStateFlow(Pair(false, ""))
    val toggleConfirmDeleteDialog = _toggleConfirmDeleteDialog.asStateFlow()

    val username = authRepository.currentUser()?.displayName ?: ""

    private var listenerRegistration: ListenerRegistration? = null

    init {
        updateCurrentUserData()
        startListeningForChanges()
    }

    private fun startListeningForChanges() {
        viewModelScope.launch {
            val documentRef = firestoreRepository.getDocument(
                "users",
                authRepository.currentUser()?.uid.toString()
            )

            listenerRegistration = documentRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {

                    val data = snapshot.data

                    val receivedRequestList = data?.get("receivedRequestList") as? List<*>
                    if (receivedRequestList != null && receivedRequestList.all { it is String }) {
                        _receivedRequestList.value = receivedRequestList.filterIsInstance<String>()
                    } else _receivedRequestList.value = listOf()

                    val sentRequestList = data?.get("sentRequestList") as? List<*>
                    if (sentRequestList != null && sentRequestList.all { it is String }) {
                        _sentRequestList.value = sentRequestList.filterIsInstance<String>()
                    } else _sentRequestList.value = listOf()

                    val friendsList = data?.get("friendsList") as? List<*>
                    if (friendsList != null && friendsList.all { it is String }) {
                        _friendsList.value = friendsList.filterIsInstance<String>()
                    } else _friendsList.value = listOf()

                    updateCurrentUserData()
                }
            }
        }
    }


    private fun updateCurrentUserData() {
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

    fun toggleConfirmDeleteDialog(boolean: Boolean, username: String) {
        _toggleConfirmDeleteDialog.value = Pair(boolean, username)
    }

    fun acceptFriendRequest(requestingUser: String) {
        viewModelScope.launch {
            firestoreRepository.acceptFriendRequest(requestingUser, username)
            updateCurrentUserData()
            _state.value = FriendsState(isSuccess = "Friend request accepted")
        }
    }

    fun dismissSentRequest(requestedUser: String) {
        viewModelScope.launch {
            firestoreRepository.deleteFriendRequest(username, requestedUser)
            updateCurrentUserData()
            _state.value = FriendsState(isSuccess = "Friend request dismissed")
        }
    }

    fun dismissReceivedRequest(requestingUser: String) {
        viewModelScope.launch {
            firestoreRepository.deleteFriendRequest(requestingUser, username)
            updateCurrentUserData()
            _state.value = FriendsState(isSuccess = "Friend request dismissed")
        }
    }

    fun requestFriend() {
        viewModelScope.launch {
            firestoreRepository.requestFriend(_userQueryInput.value, username).collect { result ->
                when (result) {
                    is Resource.Error -> _state.value =
                        FriendsState(isError = result.message.toString())

                    is Resource.Loading -> _state.value = FriendsState(isLoading = true)
                    is Resource.Success -> {
                        _state.value = FriendsState(isSuccess = "Request sent")
                        updateCurrentUserData()
                    }
                }
            }
        }
    }

    fun deleteFriend(username: String) {
        viewModelScope.launch {
                authRepository.currentUser()?.displayName?.let {
                    firestoreRepository.deleteFriend(
                        it,
                        username
                    )
                }
            }
        }


    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }

}