package com.app.afinal.service

import android.util.Log
import com.app.afinal.model.ResponseErrorModel
import com.app.afinal.utils.ApiException
import com.app.afinal.utils.NoInternetException
import com.google.gson.GsonBuilder

import org.json.JSONObject
import retrofit2.Response

abstract class SafeApiRequest {
    suspend fun <T : Any> apiRequest(
        call : suspend () -> Response<T>
    ): T{
        val response = call.invoke()
        if(response.isSuccessful){
            return response.body() ?: throw Exception("Empty response body")
        }else{

            val error = response.errorBody()?.string()
            val message = StringBuilder()
            error?.let{
                try{
                    val errorResponse = JSONObject(it).toString()
                    val gsonBuilder = GsonBuilder()
                    val gson = gsonBuilder.create()
                    val errorData = gson.fromJson(errorResponse, ResponseErrorModel::class.java) as ResponseErrorModel
                    message.append(errorData.messages ?: "Unknown error")
                }catch (e : NoInternetException){
                    message.append("No internet connection")
                }
            }
            throw ApiException(message.toString())
        }
    }
}