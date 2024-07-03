package com.ludicomm.data.repository

import com.ludicomm.data.model.Match
import com.ludicomm.util.stateHandlers.Resource
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {

   suspend fun registerUser(id: String, username: String): Flow<Resource<Unit>>

   suspend fun submitMatch(match: Match): Flow<Resource<Unit>>

   suspend fun getAllUserMatches(user: String): List<Match>

}