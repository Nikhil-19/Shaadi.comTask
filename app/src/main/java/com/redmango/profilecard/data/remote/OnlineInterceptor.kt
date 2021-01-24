package com.redmango.profilecard.data.remote

import android.content.Context
import com.example.drsmarineservices.nikhil.utility.LogUtil
import com.example.drsmarineservices.nikhil.utility.Utils
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit


class OnlineInterceptor(val ctx: Context) : Interceptor {

    private val HEADER_CACHE_CONTROL = "Cache-Control";
    private val HEADER_PRAGMA = "Pragma";

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        LogUtil.error("Offline Interceptor")

        if (!Utils.checkHasInternetConnection(ctx)!!) {
            val cacheControl: CacheControl = CacheControl.Builder()
                .maxAge(5, TimeUnit.SECONDS)
                .build()
            response.newBuilder()
                .removeHeader(HEADER_PRAGMA)
                .removeHeader(HEADER_CACHE_CONTROL)
                .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                .build()
        }

        return response
    }


}