package com.redmango.profilecard.data.local.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.redmango.profilecard.data.local.db.entitity.ProfileDetails
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ProfileDetailsDAO {

    @Insert
    fun insertProfileDetail(argProfileDetails: ProfileDetails): Long


    @Query("select * from  profile_details")
    fun getProfileDetails(): LiveData<List<ProfileDetails>>


    @Query("select * from  profile_details")
    fun getProfileInfo(): Single<List<ProfileDetails>>


    @Query("update profile_details set isAcceptedDeclined=:flag ,acceptDeclineMessage=:message where profile_details.title=:name")
    fun update(flag: Boolean, message: String, name: String): Single<Int>
}