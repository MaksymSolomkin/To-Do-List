package com.customappsms.to_dolist.di

import com.customappsms.to_dolist.data.AuthRepository
import com.customappsms.to_dolist.data.TaskRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTaskRepository(
        database: FirebaseFirestore,
        firebaseAuth: FirebaseAuth,
    ): TaskRepository {
        return TaskRepository(database, firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth
    ): AuthRepository {
        return AuthRepository(firebaseAuth)
    }
}