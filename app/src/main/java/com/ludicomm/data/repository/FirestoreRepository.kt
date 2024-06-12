package com.ludicomm.data.repository

import com.google.firebase.firestore.DocumentReference
import com.ludicomm.util.Resource
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {

   suspend fun registerUser(id: String): Flow<Resource<Unit>>

}