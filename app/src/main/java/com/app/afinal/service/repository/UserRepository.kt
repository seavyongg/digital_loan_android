package com.app.afinal.service.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.customview.widget.Openable
import com.app.afinal.Application.MySharePreferences
import com.app.afinal.service.APIClient
import com.app.afinal.service.SafeApiRequest
import com.app.afinal.model.LoginModel
import com.app.afinal.model.LoginSuccessResponse
import com.app.afinal.model.ProfileModel
import com.app.afinal.model.ProfileUpdateModel
import com.app.afinal.model.ResponseErrorModel
import com.app.afinal.utils.resizeImage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

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
                Log.d("UserRepository", "Image URI: $uri")
                createMultipartFromUri("image", uri).also {
                    Log.d("UserRepository", "Created Multipart: ${it?.body?.contentType()}")
                }
            }
            api.updateProfile(
                firstname,
                lastname,
                gender,
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
            val resizeBitmap = resizeImage(context, uri, 300, 300) ?: return null //resize image using bitmap
            val outputSteam = ByteArrayOutputStream()
            resizeBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputSteam)
            val bytes = outputSteam.toByteArray()
            val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
            val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
            val fileName = getFileName(uri)
            return MultipartBody.Part.createFormData(
                image,
                fileName,
                requestBody
            )

        }catch(e: Exception){
            e.printStackTrace()
            return null
        }
    }
    private fun getFileName(uri : Uri): String{
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndexOrThrow(android.provider.MediaStore.MediaColumns.DISPLAY_NAME)
            if (cursor.moveToFirst() && nameIndex != -1) {
                return cursor.getString(nameIndex)
            }
        }
        return "image.jpg" // Default name if not found
    }
}