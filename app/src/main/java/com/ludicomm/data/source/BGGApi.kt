package com.ludicomm.data.source

import com.ludicomm.data.model.BoardGames
import com.ludicomm.data.model.Collection
import com.ludicomm.data.model.SingleBoardGameList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface BGGApi {

    @GET("search&type=boardgame")
    suspend fun getBoardGames(@Query("query") query: String): BoardGames

    @GET("thing?")
    suspend fun getBoardGame(@Query("id") id: String): SingleBoardGameList

    @GET("collection/{username}")
    suspend fun getCollection(
        @Path("username") username: String,
        @Query("minrating") minRating: Float = 3.5f
    ): Collection

}