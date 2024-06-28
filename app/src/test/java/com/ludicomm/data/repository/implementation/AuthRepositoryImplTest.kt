package com.ludicomm.data.repository.implementation

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.ludicomm.data.repository.AuthRepository
import com.ludicomm.util.stateHandlers.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.mockito.Mockito


class AuthRepositoryImplTest : AuthRepository {

    override suspend fun loginUser(email: String, password: String): Flow<Resource<AuthResult>> {
        return flow { }
    }

    override suspend fun registerUser(
        username: String,
        email: String,
        password: String
    ): Flow<Resource<AuthResult>> {
        return flow<Resource<AuthResult>> {
                val mockResult = Mockito.mock(AuthResult::class.java)
                emit(Resource.Success(mockResult))
            }.catch {
                emit(Resource.Error(it.message.toString()))
            }
        }


    override suspend fun updateUser(name: String): String {
        return "user"
    }

    override fun signOutUser() {

    }

    override fun currentUser(): FirebaseUser? {
        return null
    }

    override fun isUserLoggedIn(): Boolean {
        return false
    }

}
