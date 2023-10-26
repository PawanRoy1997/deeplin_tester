package com.nextxform.deeplinktester.utils.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DeepLinkDao {

    @Query("SELECT * FROM DEEP_LINK ORDER BY id DESC")
    suspend fun getAllDeepLinks(): List<DeepLinkEntity>

    @Insert
    suspend fun insertDeepLink(deepLink: DeepLinkEntity)

    @Query("DELETE FROM DEEP_LINK WHERE url = :deepLinkURL")
    suspend fun deleteLastItem(deepLinkURL: String)

    @Query("DELETE FROM DEEP_LINK WHERE last_used = :time")
    suspend fun deleteEntry(time: Long)

    @Query("DELETE FROM DEEP_LINK")
    suspend fun deleteAllLinks()

}