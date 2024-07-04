package com.ludicomm.di


import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.ludicomm.data.repository.AuthRepository
import com.ludicomm.data.repository.implementation.AuthRepositoryImpl
import com.ludicomm.data.repository.FirestoreRepository
import com.ludicomm.data.repository.implementation.FirestoreRepositoryImpl
import com.ludicomm.data.repository.BGGRepository
import com.ludicomm.data.repository.implementation.BGGRepositoryImpl
import com.ludicomm.data.source.BGGApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesBGGAPIRepository(bggApi: BGGApi): BGGRepository {
        return BGGRepositoryImpl(bggApi)
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