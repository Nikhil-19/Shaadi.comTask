package com.redmango.profilecard.data.repository

import android.annotation.SuppressLint
import com.example.drsmarineservices.nikhil.utility.LogUtil
import com.redmango.profilecard.data.local.db.dao.ProfileDetailsDAO
import com.redmango.profilecard.data.local.db.entitity.*
import com.redmango.profilecard.data.remote.Networking
import com.redmango.profilecard.data.response.Result
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class ProfileRepository(val profileDetailsDAO: ProfileDetailsDAO) {
    val networkService = Networking.create()

    @SuppressLint("CheckResult")
    fun insertProfileDetailsToDb(argData: List<Result>): Single<List<ProfileDetails>> {
        var profileDetailsList = mutableListOf<ProfileDetails>()
        return Single.create<List<ProfileDetails>> {
            for (data in argData) {
                val profileDetails = ProfileDetails(
                        0,
                        data.gender,
                        Name(data.name.first, data.name.last),
                        Location(
                                data.location.city,
                                data.location.country,
                                data.location.postcode.toString(),
                                data.location.state,
                                Street(data.location.street.name, data.location.street.number),
                                Timezone(data.location.timezone.description, data.location.timezone.offset),
                                Coordinates(data.location.coordinates.latitude, data.location.coordinates.longitude),
                        ),
                        data.email,
                        Login(data.login.md5,
                                data.login.password,
                                data.login.salt,
                                data.login.sha1,
                                data.login.sha256,
                                data.login.username,
                                data.login.uuid),
                        Dob(data.dob.age, data.dob.date),
                        Registered(data.registered.age, data.registered.date),
                        data.phone,
                        data.cell,
                        // Id(data.id.name, data.id.value?:"null"),
                        Id(data.id.name, data.id.value ?: "null"),
                        Picture(data.picture.large, data.picture.medium, data.picture.thumbnail),
                        data.nat,
                        false,
                        ""
                )
                val insertionId = profileDetailsDAO.insertProfileDetail(profileDetails)
                LogUtil.error("Insertion Id $insertionId")

                if (insertionId < 0) {
                    it.onError(Throwable("Data Not Inserted"))
                } else {
                    profileDetailsList.add(profileDetails)
                }

            }
            if (argData.size == profileDetailsList.size) {
                LogUtil.error("Insertion Data Size  ${profileDetailsList.size}")
                it.onSuccess(profileDetailsList)
            } else {
                it.onError(Throwable("Data Not Inserted "))
            }

        }.subscribeOn(Schedulers.io())

    }


    fun loadProfileDetailsFromServer(): Single<List<Result>> {
        return networkService.profileDetailsRx("10").map { it.results }
    }

    fun getProfileDetails():Single<List<ProfileDetails>>{
     return profileDetailsDAO.getProfileInfo()
    }
}

