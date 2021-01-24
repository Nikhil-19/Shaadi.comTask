package com.redmango.profilecard.data.local.db.entitity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Name(
        var first: String,
        var title: String
)


@Entity(tableName = "profile_details")
data class ProfileDetails(

        @PrimaryKey(autoGenerate = true)
        val _id: Long,
        var gender: String,
        @Embedded val name: Name,
        @Embedded val location: Location,
        var email: String,
        @Embedded val login: Login,
        @Embedded val dob: Dob,
        @Embedded(prefix = "registered_") val registered: Registered,
        var phone:String,
        var cell:String,
        @Embedded(prefix = "id_") val id: Id,
        @Embedded val picture: Picture,
        var nat:String,
        var isAcceptedDeclined:Boolean,
        var acceptDeclineMessage:String
        )




data class Location(
        var city: String,
        var country: String,
        var postcode: String,
        var state: String,
        @Embedded(prefix = "street_") val street: Street,
        @Embedded val timezone: Timezone,
        @Embedded val coordinates: Coordinates,
)


data class Coordinates(
        var latitude: String,
        var longitude: String
)


data class Street(
        var name: String,
        var number: Int
)

data class Timezone(
        var description: String,
        var offset: String
)

data class Login(
        var md5: String,
        var password: String,
        var salt: String,
        var sha1: String,
        var sha256: String,
        var username: String,
        var uuid: String
)

data class Dob(
        var age: Int,
        var date: String
)

data class Registered(
        var age: Int,
        var date: String
)

data class Id(
        var name: String,
        var value: String?
)

data class Picture(
        var large: String,
        var medium: String,
        var thumbnail: String
)