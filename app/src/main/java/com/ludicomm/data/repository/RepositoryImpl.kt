package com.ludicomm.data.repository

import com.ludicomm.data.source.BGGApi
import com.ludicomm.data.model.BoardGames
import com.ludicomm.data.model.Collection

class RepositoryImpl(
    private val bggApi: BGGApi
) : Repository {

    override suspend fun getBoardGames(search: String): BoardGames {
        return bggApi.getBoardGames(search)
    }
    override suspend fun getCollection(search: String): Collection {
        return bggApi.getCollection(search)
    }
}