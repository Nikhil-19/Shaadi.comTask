package com.example.drsmarineservices.nikhil.data.remote

import com.example.drsmarineservices.nikhil.data.request.ForgotPasswordRequest
import com.example.drsmarineservices.nikhil.data.request.LoginRequest
import com.example.drsmarineservices.nikhil.data.response.ForgotPasswordResponse
import com.example.drsmarineservices.nikhil.data.response.LoginResponse
import com.redmango.profilecard.data.response.ProfileDetailsResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NetworkService {

    @GET(EndPoints.DETAILS)
    fun profileDetails(
            @Query(EndPoints.QUERY_PARAM_NAME) result: String
    ):Call<ProfileDetailsResponse>

    @GET(EndPoints.DETAILS)
    fun profileDetailsRx(
        @Query(EndPoints.QUERY_PARAM_NAME) result: String
    ):Single<ProfileDetailsResponse>
}