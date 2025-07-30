package com.app.afinal.service.repository

import android.content.Context
import android.net.Uri
import com.app.afinal.Application.MySharePreferences
import com.app.afinal.service.APIClient
import com.app.afinal.service.SafeApiRequest
import com.app.afinal.model.LoginModel
import com.app.afinal.model.LoginSuccessResponse
import com.app.afinal.model.ProfileModel
import com.app.afinal.model.ProfileUpdateModel
import com.app.afinal.model.ResponseErrorModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class UserRepository(
    val api : APIClient,
    private val context: Context,
    val sharePreferences: MySharePreferences
): SafeApiRequest() {
   suspend fun userLogin(email : String, password : String): LoginSuccessResponse{
       return apiRequest { api.login(LoginModel(email, password))}
   }

    suspend fun getProfile(): ProfileModel {
        return apiRequest { api.getProfile() }
    }

    suspend fun updateProfile( request: ProfileUpdateModel): ResponseErrorModel{
        return apiRequest {
            val firstname = request.firstname?.toRequestBody(MultipartBody.FORM) ?: "".toRequestBody(MultipartBody.FORM)
            val lastname = request.lastname?.toRequestBody(MultipartBody.FORM)?: "".toRequestBody(MultipartBody.FORM)
            val gender = request.gender?.toRequestBody(MultipartBody.FORM) ?: "".toRequestBody(MultipartBody.FORM)
            val dob = request.dob?.toRequestBody(MultipartBody.FORM) ?: "".toRequestBody(MultipartBody.FORM)
            val address = request.address?.toRequestBody(MultipartBody.FORM) ?: "".toRequestBody(MultipartBody.FORM)
            val email = request.email?.toRequestBody(MultipartBody.FORM) ?: "".toRequestBody(MultipartBody.FORM)
            val phone = request.phone?.toRequestBody(MultipartBody.FORM) ?: "".toRequestBody(MultipartBody.FORM)
            val image = request.image?.let { uri ->
                createMultipartFromUri("profile_picture", uri)
            }
            api.updateProfile(
                firstname,
                lastname,
                phone ,
                email ,
                address ,
                dob,
                image
            )
        }
    }
    private fun createMultipartFromUri(image: String, uri : Uri): MultipartBody.Part?{
        try{
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val bytes = inputStream.readBytes()
            inputStream.close()

            val minType = contentResolver.getType(uri) ?: "image/jpeg" // Default to JPEG if type not found
            val requestBody = bytes.toRequestBody(minType.toMediaTypeOrNull())
            val fileName = getFileName(uri)
            return MultipartBody.Part.createFormData(image, fileName, requestBody)

        }catch(e: Exception){
            e.printStackTrace()
            return null
        }
    }
    private fun getFileName(uri : Uri): String{
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndexOrThrow("_display_name")
            if (cursor.moveToFirst()) {
                return cursor.getString(nameIndex)
            }
        }
        return uri.lastPathSegment ?: "unknown_file"
    }
}