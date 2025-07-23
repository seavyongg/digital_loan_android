package com.app.afinal.service.repository

import android.content.Context
import android.util.Log
import com.app.afinal.model.LoanModel
import com.app.afinal.service.APIClient
import com.app.afinal.service.SafeApiRequest

class LoanRepository(
    val api : APIClient,
    private val context : Context
):SafeApiRequest() {
    //get all loan
    suspend fun getAllLoan(): LoanModel{
        return apiRequest {
            api.getAllLoan()
        }
        Log.d("LoanRepository", "${api.getAllLoan()}")
    }

    //get loan detail
    suspend fun getLoanDetail(id: Int) = apiRequest { api.getLoanDetail(id) }
}