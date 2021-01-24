package com.example.drsmarineservices.nikhil.data.response

import com.google.gson.annotations.SerializedName

data class LoginErrorResponse(
        @SerializedName("StatusCode")
        val statusCode: Long,

        @SerializedName("StatusMessage")
        val statusMessage: String,

        @SerializedName("message")
        val message: String,

        @SerializedName("error")
        val error: Error

)

data class Error (

        @SerializedName("sc_password")
        val scPassword: String
)
