package com.ludicomm.data.repository.implementation

import android.util.Log
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

    override suspend fun isUsernameUsed(username: String): Boolean {
        val usernameList = mutableListOf<User>()
        firestore.collection("users").whereEqualTo("lowerCaseUsername", username.lowercase())
                .get().addOnSuccessListener { usernameList.addAll(it.toObjects<User>()) }.await()
        Log.i("TAG", "isUsernameUsed: $usernameList")
        return usernameList.isNotEmpty()
    }
}

