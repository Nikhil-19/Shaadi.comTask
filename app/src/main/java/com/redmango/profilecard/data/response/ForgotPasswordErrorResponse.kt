package com.example.drsmarineservices.nikhil.data.response

import com.google.gson.annotations.SerializedName

data class ForgotPasswordErrorResponse(
    @SerializedName("StatusCode")
    val statusCode: Long,

    @SerializedName("StatusMessage")
    val statusMessage: String,

    @SerializedName("message")
    val message: String,
)