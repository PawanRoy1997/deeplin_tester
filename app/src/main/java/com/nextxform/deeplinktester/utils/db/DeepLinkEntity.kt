package com.nextxform.deeplinktester.utils.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deep_link")
data class DeepLinkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "url")
    val url: String,
    @ColumnInfo("last_used")
    val lastUsed: Long,
    @ColumnInfo("is_favourite")
    val isFavourite: Boolean
)

data class DeepLinkItem(val id: Int, val url: String, val lastUsed: String, val isFavourite: Boolean)