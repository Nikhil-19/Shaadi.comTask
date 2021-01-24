package com.redmango.profilecard.data.remote

import android.content.Context
import com.example.drsmarineservices.nikhil.data.remote.NetworkService
import com.example.drsmarineservices.nikhil.utility.LogUtil
import com.example.drsmarineservices.nikhil.utility.Utils
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.redmango.profilecard.BuildConfig
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


object Networking {

    val NETWORK_CALL_TIMEOUT = 60
    internal lateinit var API_KEY: String

    private val BASE_URL = "https://randomuser.me/"

    //  private val BASE_URL = "https://jsonplaceholder.typicode.com"
    val cacheSize: Long = 10 * 1024 * 1024.toLong()


    fun create(apiKey: String, baseUrl: String, cacheDir: File, cacheSize: Long): NetworkService {
        API_KEY = apiKey
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(
                OkHttpClient.Builder()
                    .cache(Cache(File(cacheDir, "someIdentifier"), cacheSize))
                    .addInterceptor(HttpLoggingInterceptor()
                        .apply {
                            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                            else HttpLoggingInterceptor.Level.NONE
                        })
                    .readTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
                    .writeTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NetworkService::class.java)
    }


    fun create(argContext: Context): NetworkService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .cache(Cache(argContext.cacheDir, cacheSize))
                    .addInterceptor(HttpLoggingInterceptor()
                        .apply {
                            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                            else HttpLoggingInterceptor.Level.NONE
                        })
                    .addNetworkInterceptor(object : Interceptor {
                        override fun intercept(chain: Interceptor.Chain): Response {
                            LogUtil.error("Interceptor rewrite")
                            val originalResponse = chain.proceed(chain.request());
                            var cacheControl = originalResponse.header("Cache-Control");
                            if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains(
                                    "no-cache"
                                ) ||
                                cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")
                            ) {
                                return originalResponse.newBuilder()
                                    .removeHeader("Pragma")
                                    .header("Cache-Control", "public, max-age=" + 5000)
                                    .build();
                            } else {
                                return originalResponse;
                            }
                        }
                    })

                    .addInterceptor(object : Interceptor {
                        override fun intercept(chain: Interceptor.Chain): Response {
                            LogUtil.error("Interceptor offline")
                            var request = chain.request()
                            if (!Utils.checkHasInternetConnection(argContext)!!) {
                                request = request.newBuilder()
                                    .removeHeader("Pragma")
                                  //  .header("Cache-Control", "public, only-if-cached")
                                    .header("Cache-Control", "public,max-stale=${60*60*24*1}")
                                    .build()
                            }
                            LogUtil.error("Request ${request.toString()}")

                            return chain.proceed(request).also { LogUtil.error(it.toString()) }

                        }
                    })


                    //    .readTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
                    //    .writeTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(
                NetworkService::
                class.java
            )
    }


    /*
       Retrofit with Rx Java
     */


    fun create(): NetworkService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor()
                        .apply {
                            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                            else HttpLoggingInterceptor.Level.NONE
                        })
                    .readTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
                    .writeTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(NetworkService::class.java)
    }
}

