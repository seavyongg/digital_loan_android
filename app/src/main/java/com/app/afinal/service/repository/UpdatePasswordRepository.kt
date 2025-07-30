package com.app.afinal.service.repository

import com.app.afinal.model.PasswordModel
import com.app.afinal.model.ResponseErrorModel
import com.app.afinal.service.APIClient
import com.app.afinal.service.SafeApiRequest

class UpdatePasswordRepository(
    private var api : APIClient
): SafeApiRequest() {
    suspend fun updatePassword(
        request: PasswordModel
    ): ResponseErrorModel {
        return apiRequest { api.updatePassword(request) }
    }
}