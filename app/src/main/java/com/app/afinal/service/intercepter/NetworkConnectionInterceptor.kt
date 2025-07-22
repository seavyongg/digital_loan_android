package com.app.afinal.service.intercepter

import com.app.afinal.Application.DigitalLoanApplication
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
       //check is internet is connected
        if(DigitalLoanApplication.isConnected){
            throw NotImplementedError("No Internet Connection")
        }else{
            return chain.proceed(chain.request())
        }
    }
}