package com.example.drsmarineservices.nikhil.data.response

import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponse(
        @SerializedName("StatusCode")
        val statusCode: Long,

        @SerializedName("StatusMessage")
        val statusMessage: String,

        @SerializedName("message")
        val message: String,

        @SerializedName("data")
        val data: String
)



