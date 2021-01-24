package com.redmango

import android.app.Application
import android.content.SharedPreferences
import com.example.drsmarineservices.nikhil.data.remote.NetworkService
import com.facebook.stetho.Stetho
import com.redmango.profilecard.data.local.db.DatabaseService
import com.redmango.profilecard.data.remote.Networking
import com.redmango.profilecard.utility.Constants

class ProfileCardApplication:Application() {

    lateinit var networkService: NetworkService;
    lateinit var db: DatabaseService
    lateinit var preferences:SharedPreferences
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        networkService=Networking.create(this)
        db= DatabaseService.getDatabase(this)
        preferences=getSharedPreferences(Constants.SHARED_PREFERNCE_NAME, MODE_PRIVATE)
    }
}