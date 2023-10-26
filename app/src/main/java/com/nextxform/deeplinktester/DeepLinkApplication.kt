package com.nextxform.deeplinktester

import android.app.Application
import com.nextxform.deeplinktester.utils.db.DeepLinkDatabase

class DeepLinkApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DeepLinkDatabase.initDatabase(this)
    }
}