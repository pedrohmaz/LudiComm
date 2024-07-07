package com.ludicomm.data.repository

import com.ludicomm.data.model.BoardGame
import com.ludicomm.data.model.BoardGames
import com.ludicomm.data.model.Collection
import com.ludicomm.data.model.SingleBoardGameList
import com.ludicomm.util.stateHandlers.Resource
import kotlinx.coroutines.flow.Flow

interface BGGRepository {

    suspend fun getBoardGames(search: String): Flow<Resource<BoardGames>>

    suspend fun getCollection(search: String): Collection

    suspend fun getBoardGame(id: String): SingleBoardGameList
}