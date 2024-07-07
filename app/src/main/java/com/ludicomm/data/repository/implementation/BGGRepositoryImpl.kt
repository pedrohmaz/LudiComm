package com.ludicomm.data.repository.implementation

import com.ludicomm.data.model.BoardGame
import com.ludicomm.data.source.BGGApi
import com.ludicomm.data.model.BoardGames
import com.ludicomm.data.model.Collection
import com.ludicomm.data.model.SingleBoardGameList
import com.ludicomm.data.repository.BGGRepository
import com.ludicomm.util.stateHandlers.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class BGGRepositoryImpl(
    private val bggApi: BGGApi
) : BGGRepository {

    override suspend fun getBoardGames(name: String): Flow<Resource<BoardGames>> {
        return try {
            flow {
                emit(Resource.Loading())
                val result = bggApi.getBoardGames(name)
                emit(Resource.Success(result))
            }
        } catch (e: Exception) {
            return flowOf(Resource.Error(""))
        }

    }

    override suspend fun getCollection(search: String): Collection {
        return bggApi.getCollection(search)
    }

    override suspend fun getBoardGame(id: String): SingleBoardGameList {
        return bggApi.getBoardGame(id)
    }
}