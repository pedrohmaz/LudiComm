package com.ludicomm.data.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.ludicomm.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun loginUser(email: String, password: String): Flow<Resource<AuthResult>>
    suspend fun registerUser(username: String, email: String, password: String): Flow<Resource<AuthResult>>
    suspend fun updateUser(name: String): String
    fun signOutUser()
    fun currentUser(): FirebaseUser?
    fun isUserLoggedIn(): Boolean

}