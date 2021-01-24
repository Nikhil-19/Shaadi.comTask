package com.example.drsmarineservices.nikhil.data.remote

interface ApiCallBackResult<T,P>{
   fun onSuccess(result: T)
   fun onFailure(error: String)
   fun onErrorResponse(error:P)
}