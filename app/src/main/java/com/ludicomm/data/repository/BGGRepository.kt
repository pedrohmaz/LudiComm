package com.ludicomm.data.repository

import com.ludicomm.data.model.BoardGames
import com.ludicomm.data.model.Collection

interface BGGRepository {

    suspend fun getBoardGames(search: String): BoardGames

    suspend fun getCollection(search: String): Collection

}