package com.redmango.profilecard.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.redmango.profilecard.data.local.db.dao.ProfileDetailsDAO
import com.redmango.profilecard.data.local.db.entitity.ProfileDetails

@Database(entities = [ProfileDetails::class], version = 1, exportSchema = false)
abstract class DatabaseService : RoomDatabase() {

    abstract fun getProfileDAO(): ProfileDetailsDAO

    companion object {
        @Volatile
        private var dbInstance: DatabaseService? = null

        fun getDatabase(context: Context): DatabaseService {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return dbInstance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseService::class.java,
                    "word_database"
                ).build()
                dbInstance = instance
                // return instance
                instance
            }
        }
    }
}