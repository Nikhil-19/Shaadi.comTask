package com.redmango.profilecard.data.local.prefs

import android.content.SharedPreferences

class ProfilePreferences(val sharedPreferences: SharedPreferences) {
    companion object {
        val PREF_IS_DATA_SYNCED = "isDataSynced"
    }

    fun setIsDataSynced(argBoolean: Boolean) = sharedPreferences.edit().putBoolean(PREF_IS_DATA_SYNCED, argBoolean).apply()

    fun checkIsDataSynced():Boolean=sharedPreferences.getBoolean(PREF_IS_DATA_SYNCED,false)
}