package com.ludicomm.data.source

import com.ludicomm.data.model.BoardGames
import com.ludicomm.data.model.Collection
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface BGGApi {

    @GET("search")
    suspend fun getBoardGames(@Query("search") search: String): BoardGames

    @GET("collection/{username}")
    suspend fun getCollection(
        @Path("username") username: String,
        @Query("minrating") minRating: Float = 3.5f
    ): Collection

}