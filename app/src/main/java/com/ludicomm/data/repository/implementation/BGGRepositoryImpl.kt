package com.ludicomm.data.repository.implementation

import com.ludicomm.data.model.BoardGames
import com.ludicomm.data.model.Collection
import com.ludicomm.data.model.SingleBoardGame
import com.ludicomm.data.model.SingleBoardGameList
import com.ludicomm.data.repository.BGGRepository
import com.ludicomm.data.source.BGGApi
import com.ludicomm.util.stateHandlers.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class BGGRepositoryImpl @Inject constructor(
    private val bggApi: BGGApi
) : BGGRepository {

    override suspend fun getBoardGames(search: String): Flow<Resource<BoardGames>> {
        return try {
            flow {
                emit(Resource.Loading())
                val result = bggApi.getBoardGames(search)
                emit(Resource.Success(result))
            }
        } catch (e: Exception) {
            return flowOf(Resource.Error("$e"))
        }

    }

    override suspend fun getCollection(search: String): Collection {
        return bggApi.getCollection(search)
    }

    override suspend fun getBoardGame(id: String): SingleBoardGameList {
        return try {
            bggApi.getBoardGame(id)
        } catch (e: Exception){
            SingleBoardGameList("", boardGames = listOf(SingleBoardGame()))
        }
    }
}