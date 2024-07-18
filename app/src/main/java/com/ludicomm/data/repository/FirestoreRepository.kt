package com.ludicomm.data.repository

import com.ludicomm.data.model.Match
import com.ludicomm.data.model.User
import com.ludicomm.util.stateHandlers.Resource
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {

    suspend fun registerUser(id: String, username: String): Flow<Resource<Unit>>

    suspend fun submitMatch(match: Match): Flow<Resource<Unit>>

    suspend fun getAllUserMatches(user: String): List<Match>

    suspend fun isUsernameUsed(username: String): Boolean

    suspend fun requestFriend(username: String, currentUsername: String): Flow<Resource<Unit>>

    suspend fun getUser(username: String): User?

    suspend fun deleteFriendRequest(requestingUser: String, requestedUser: String)

    suspend fun acceptFriendRequest(requestingUser: String, requestedUser: String)

    suspend fun isUserAFriend(username: String, currentUsername: String): Boolean

    suspend fun isRequestAlreadySent(username: String, currentUsername: String): Boolean

}