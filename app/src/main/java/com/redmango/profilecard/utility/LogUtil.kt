package com.example.drsmarineservices.nikhil.utility

import android.util.Log

object  LogUtil {

    private val TAG="DRS"

    fun info(msg:String){
        Log.i(TAG,msg)
    }

    fun warn(msg:String){
        Log.w(TAG,msg)
    }

    fun debug(msg:String){
        Log.d(TAG,msg)
    }

    fun error(msg:String){
        Log.e(TAG,msg)
    }

}

