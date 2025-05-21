package com.test.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.data.model.entity.RemoteKeys

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("Select * From remote_key Where email = :email")
    suspend fun getRemoteKeyByContactEmail(email: String): RemoteKeys?

    @Query("Delete From remote_key")
    suspend fun clearRemoteKeys()

}