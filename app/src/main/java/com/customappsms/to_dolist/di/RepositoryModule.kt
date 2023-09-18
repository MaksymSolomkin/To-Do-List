package com.customappsms.to_dolist.di

import com.customappsms.to_dolist.data.TaskRepository
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
        database: FirebaseFirestore
    ): TaskRepository {
        return TaskRepository(database)
    }
}