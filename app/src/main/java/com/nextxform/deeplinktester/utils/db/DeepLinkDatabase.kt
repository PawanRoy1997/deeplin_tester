package com.nextxform.deeplinktester.utils.db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [DeepLinkEntity::class], version = 1)
abstract class DeepLinkDatabase : RoomDatabase() {
    abstract fun getDeepLinkDao(): DeepLinkDao
}