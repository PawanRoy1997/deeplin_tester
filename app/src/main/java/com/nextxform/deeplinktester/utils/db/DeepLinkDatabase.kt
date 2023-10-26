package com.nextxform.deeplinktester.utils.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


private const val DEEP_LINK_DATABASE_NAME = "deep_link_database"

@Database(entities = [DeepLinkEntity::class], version = 1)
abstract class DeepLinkDatabase : RoomDatabase() {
    companion object {
        private lateinit var database: DeepLinkDatabase

        fun initDatabase(context: Context) {
            database = Room.databaseBuilder(
                context.applicationContext,
                DeepLinkDatabase::class.java,
                DEEP_LINK_DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }

        fun getDatabase(): DeepLinkDatabase = database
    }

    abstract fun getDeepLinkDao(): DeepLinkDao
}