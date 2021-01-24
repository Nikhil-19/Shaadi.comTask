package com.example.drsmarineservices.nikhil.utility

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.DisplayMetrics
import java.net.ConnectException
import java.net.SocketException


object  Utils {

    fun handleVolleyException(argVolleyExceptions: Throwable) : String {

        return when(argVolleyExceptions){

            is ConnectException -> "Failed To Connect To Api"

            is SocketException -> "Couldmt Connect"

            else -> argVolleyExceptions.message
        }?:"SomeThing went wrong"
    }


    fun checkHasInternetConnection(context: Context): Boolean? {
        var isConnected: Boolean? = false // Initial Value
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }



}