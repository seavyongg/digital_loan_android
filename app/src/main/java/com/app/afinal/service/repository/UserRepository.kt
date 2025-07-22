package com.app.afinal.service.repository

import com.app.afinal.Application.MySharePreferences
import com.app.afinal.service.APIClient
import com.app.afinal.service.SafeApiRequest
import com.app.afinal.model.LoginModel
import com.app.afinal.model.LoginSuccessResponse

class UserRepository(
    val api : APIClient,
    val sharePreferences: MySharePreferences
): SafeApiRequest() {
   suspend fun userLogin(email : String, password : String): LoginSuccessResponse{
       return apiRequest { api.login(LoginModel(email, password))}
   }
}