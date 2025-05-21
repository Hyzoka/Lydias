package com.test.data.di

import android.content.Context
import androidx.room.Room
import com.test.data.local.AppDatabase
import com.test.data.local.RemoteKeysDao
import com.test.data.local.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @javax.inject.Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "lydias_database"
        ).build()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideKeysDao(database: AppDatabase): RemoteKeysDao {
        return database.getRemoteKeysDao()
    }

}