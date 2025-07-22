package com.app.afinal.service.intercepter

import android.content.Context
import android.util.Log
import com.app.afinal.Application.MySharePreferences
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class TokenInterceptor(
    context: Context,
) : Interceptor{
    private var token : String? = null
    private var sharePreferences = MySharePreferences(context)
    override fun intercept(chain: Interceptor.Chain): Response {
        token = sharePreferences.getToken()
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()
        if (!token.isNullOrEmpty()) {
            builder.header("Authorization", "Bearer $token")
        }
        Log.d("TokenInterceptor", "Token: $token")
        val modifiedRequest = builder.build()
        return chain.proceed(modifiedRequest)

    }
}