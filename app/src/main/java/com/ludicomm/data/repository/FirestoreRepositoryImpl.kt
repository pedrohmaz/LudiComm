package com.ludicomm.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.ludicomm.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore) :
    FirestoreRepository {
    override suspend fun registerUser(id: String): Flow<Resource<Unit>> {
        val data = hashMapOf("id" to id)
        return flow {
            try {
                firestore.collection("users").document(id).set(data).await()
                emit(Resource.Success(Unit))
            } catch(e: Exception){ emit(Resource.Error(message = e.message.toString())) }
        }
    }

}