package com.example.drsmarineservices.nikhil.data.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginRequest(
        @SerializedName("API_ACCESS_KEY")
        @Expose
        val apiAcessKey: String,
        @SerializedName("sc_indos")
        @Expose
        val sc_indos: String,
        @SerializedName("sc_password")
        @Expose
        val sc_password: String,
)