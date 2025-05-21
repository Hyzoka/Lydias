package com.test.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.data.model.entity.RemoteKeys
import com.test.data.model.entity.UserEntity

@Database(entities = [UserEntity::class, RemoteKeys::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun getRemoteKeysDao(): RemoteKeysDao

}
