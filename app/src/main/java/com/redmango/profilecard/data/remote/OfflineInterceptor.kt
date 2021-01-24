package com.redmango.profilecard.data.remote

import android.content.Context
import com.example.drsmarineservices.nikhil.utility.LogUtil
import com.example.drsmarineservices.nikhil.utility.Utils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class OfflineInterceptor(val ctx: Context) : Interceptor {

    private val HEADER_CACHE_CONTROL = "Cache-Control";
    private val HEADER_PRAGMA = "Pragma";

//    override fun intercept(chain: Interceptor.Chain): Response {
//        LogUtil.error("Offline Interceptor")
//        val request = chain.request()
//        if (!Utils.checkHasInternetConnection(ctx)!!) {
//            LogUtil.error("No Internet Connection")
//            val cacheControl: CacheControl = CacheControl.Builder()
//                .onlyIfCached()
//                .maxStale(7, TimeUnit.DAYS)
//                .build()
//            request.newBuilder()
//                .removeHeader(HEADER_PRAGMA)
//                .removeHeader(HEADER_CACHE_CONTROL)
//                .cacheControl(cacheControl)
//                .build()
//        }
//        return chain.proceed(request)
//    }

    override fun intercept(chain: Interceptor.Chain): Response {
        LogUtil.error("Offline Interceptor")
        var request: Request = chain.request()
        if (!Utils.checkHasInternetConnection(ctx)!!) {
            val maxStale = 60 * 60 * 24 * 28 // tolerate 4-weeks stale \
            request = request
                .newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .build()
        }
        return chain.proceed(request)
    }

}