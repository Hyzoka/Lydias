package com.test.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_key")
data class RemoteKeys(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "email")
    val contactEmail: String,
    val prevKey: Int?,
    val currentPage: Int,
    val nextKey: Int?,
)