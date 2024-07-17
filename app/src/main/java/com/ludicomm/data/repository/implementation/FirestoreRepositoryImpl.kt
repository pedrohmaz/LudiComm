package com.ludicomm.data.repository.implementation

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.toObjects
import com.ludicomm.data.model.Match
import com.ludicomm.data.model.User
import com.ludicomm.data.repository.FirestoreRepository
import com.ludicomm.util.stateHandlers.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore) :
    FirestoreRepository {
    override suspend fun registerUser(id: String, username: String): Flow<Resource<Unit>> {
        if (!isUsernameUsed(username)) {
            val user = User(id, username, username.lowercase())
            return flow {
                try {
                    firestore.collection("users").document(id).set(user).await()
                    emit(Resource.Success(Unit))
                } catch (e: Exception) {
                    emit(Resource.Error(message = e.message.toString()))
                }
            }
        } else return flowOf(Resource.Error(message = "User already chosen."))
    }

    override suspend fun submitMatch(match: Match): Flow<Resource<Unit>> {
        firestore.firestoreSettings = firestoreSettings {
        }
        FirebaseFirestoreSettings.Builder().setPersistenceEnabled(false).build()
        return flow {
            try {
                firestore.collection("matches").add(match).await()
                emit(Resource.Success(Unit))
                firestore.firestoreSettings = firestoreSettings {
                }
            } catch (e: Exception) {
                emit(Resource.Error("Could not submit match: $e"))
                firestore.firestoreSettings = firestoreSettings {
                }
            }
        }
    }

    override suspend fun getAllUserMatches(user: String): List<Match> {
        return firestore.collection("matches")
            .whereArrayContains("playerNames", user).get()
            .await().toObjects<Match>()
    }

    override suspend fun requestFriend(
        username: String,
        currentUser: String
    ): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading())
            try {
                val user = getUser(username)
                if (user != null) {
                    if (currentUser.lowercase() != username.lowercase()) {
                        val newRequestList = user.pendingRequestsReceived.toMutableList()
                        newRequestList.add(currentUser)
                        val updatedUser = user.copy(pendingRequestsReceived = newRequestList)
                        firestore.collection("users").document(user.id).set(updatedUser)

                        val newSendList = user.pendingRequestsSent.toMutableList()
                        newSendList.add(user.username)
                        val currentUserObject = getUser(currentUser)
                        val updatedCurrentUser =
                            currentUserObject?.copy(pendingRequestsSent = newSendList)
                        firestore.collection("users").document(updatedCurrentUser!!.id)
                            .set(updatedCurrentUser)
                        emit(Resource.Success(Unit))
                    } else emit(Resource.Error(message = "Same username as current user"))
                } else {
                    emit(Resource.Error(message = "User not found"))
                }
            } catch (e: Exception) {
                emit(Resource.Error(message = "$e"))
            }
        }
    }

    override suspend fun acceptFriendRequest(requestingUser: String, requestedUser: String) {
        val currentUserId = getUser(requestingUser)?.id ?: ""
        val otherUserId = getUser(requestedUser)?.id ?: ""
        firestore.collection("users").document(currentUserId)
            .update("friends", FieldValue.arrayUnion(requestedUser))
        firestore.collection("users").document(otherUserId)
            .update("friends", FieldValue.arrayUnion(requestingUser))
        deleteFriendRequest(requestingUser, requestedUser)
    }

    override suspend fun deleteFriendRequest(requestingUser: String, requestedUser: String) {
        val currentUserId = getUser(requestingUser)?.id ?: ""
        val otherUserId = getUser(requestedUser)?.id ?: ""
        firestore.collection("users").document(currentUserId)
            .update("pendingRequestsSent", FieldValue.arrayRemove(requestedUser))
        firestore.collection("users").document(otherUserId)
            .update("pendingRequestsReceived", FieldValue.arrayRemove(requestingUser))
    }

    override suspend fun getUserFriends(username: String): List<String> {
        val user = getUser(username)
        return user?.friends ?: listOf()
    }

    override suspend fun isUsernameUsed(username: String): Boolean {
        val usernameList = mutableListOf<User>()
        firestore.collection("users").whereEqualTo("lowerCaseUsername", username.lowercase())
            .get().addOnSuccessListener { usernameList.addAll(it.toObjects<User>()) }.await()
        return usernameList.isNotEmpty()
    }

    override suspend fun getUser(username: String): User? {
        val user =
            firestore.collection("users").whereEqualTo("lowerCaseUsername", username.lowercase())
                .get().await().toObjects<User>()
        return if (user.isNotEmpty()) user[0] else return null
    }


}

