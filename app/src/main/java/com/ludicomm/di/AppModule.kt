package com.ludicomm.di


import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.ludicomm.data.repository.AuthRepository
import com.ludicomm.data.repository.AuthRepositoryImpl
import com.ludicomm.data.repository.FirestoreRepository
import com.ludicomm.data.repository.FirestoreRepositoryImpl
import com.ludicomm.data.repository.Repository
import com.ludicomm.data.repository.RepositoryImpl
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