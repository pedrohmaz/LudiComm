package com.ludicomm.di


import android.app.Application
import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.ludicomm.data.repository.AuthRepository
import com.ludicomm.data.repository.implementation.AuthRepositoryImpl
import com.ludicomm.data.repository.FirestoreRepository
import com.ludicomm.data.repository.implementation.FirestoreRepositoryImpl
import com.ludicomm.data.repository.Repository
import com.ludicomm.data.repository.implementation.RepositoryImpl
import com.ludicomm.data.source.BGGApi
import com.ludicomm.util.ConnectivityObserver
import com.ludicomm.util.ConnectivityObserverImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesBGGAPIRepository(bggApi: BGGApi): Repository {
        return RepositoryImpl(bggApi)
    }

    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun providesFirestoreInstance() = Firebase.firestore

    @Provides
    @Singleton
    fun providesFirestoreRepository(firestore: FirebaseFirestore): FirestoreRepository {
        return FirestoreRepositoryImpl(firestore)
    }


}