package com.ludicomm.di

import android.content.Context
import com.ludicomm.data.source.BGGApi
import com.ludicomm.util.ConnectivityObserver
import com.ludicomm.util.ConnectivityObserverImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BGG_BASE_URL = "https://boardgamegeek.com/xmlapi2/"
    private const val FCM_BASE_URL = "http://10.0.2.2:8080/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideBGGRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BGG_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideBGGApi(retrofit: Retrofit): BGGApi {
        return retrofit.create(BGGApi::class.java)
    }

    @Provides
    @Singleton
    fun providesConnectivityObserver(@ApplicationContext context: Context) : ConnectivityObserver {
        return ConnectivityObserverImpl(context)
    }
}


