package com.nextxform.deeplinktester.hilt

import android.content.Context
import androidx.room.Room
import com.nextxform.deeplinktester.utils.db.DeepLinkDao
import com.nextxform.deeplinktester.utils.db.DeepLinkDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {
    private val deepLinkDatabaseName = "deep_link_database"

    @Singleton
    @Provides
    fun getDeepLinkDatabase(@ApplicationContext context: Context): DeepLinkDatabase {
        return Room.databaseBuilder(context, DeepLinkDatabase::class.java, deepLinkDatabaseName)
            .fallbackToDestructiveMigration(true).build()
    }

    @Provides
    fun getDeeplinkDao(database: DeepLinkDatabase): DeepLinkDao{
        return database.getDeepLinkDao()
    }
}