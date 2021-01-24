package com.example.drsmarineservices.nikhil.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
        @SerializedName("StatusCode")
        val statusCode: Long,

        @SerializedName("StatusMessage")
        val statusMessage: String,

        @SerializedName("message")
        val message: String,

        @SerializedName("data")
        val data: List<LoginData>





)

data class LoginData(
        @SerializedName("sc_id")
        val scID: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("rank_name")
        val rankName: String,
        @SerializedName("vm_name")
        val vmName: String,
        @SerializedName("sc_indos")
        val scIndos: String,
        @SerializedName("sc_photopath")
        val scPhotopath: String,
        @SerializedName("acceess_key")
        val acceessKey: String
)

