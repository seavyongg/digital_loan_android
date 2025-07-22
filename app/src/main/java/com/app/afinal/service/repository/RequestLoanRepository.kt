package com.app.afinal.service.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.app.afinal.model.RequestLoanModel
import com.app.afinal.model.RequestLoanResponse
import com.app.afinal.model.ResponseErrorModel
import com.app.afinal.service.APIClient
import com.app.afinal.service.SafeApiRequest
import com.app.afinal.service.intercepter.NetworkConnectionInterceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class RequestLoanRepository(
    val api : APIClient,
    private val context: Context,
): SafeApiRequest() {

    //get all request Loan
    suspend fun getAllRequestLoan(): RequestLoanResponse {
        return apiRequest { api.getRequestLoan() }
    }

    //creates a new loan request
    suspend fun requestLoan( request: RequestLoanModel ): ResponseErrorModel {
        return apiRequest {
            // Convert regular fields to RequestBody
            val employeeType = request.employeeType.toRequestBody(MultipartBody.FORM)
            val position = request.position.toRequestBody(MultipartBody.FORM)
            val income = request.income.toString().toRequestBody(MultipartBody.FORM)
            val loanAmount = request.loanAmount.toString().toRequestBody(MultipartBody.FORM)
            val loanDuration = request.loanDuration.toString().toRequestBody(MultipartBody.FORM)
            val loanType = request.loanType.toRequestBody(MultipartBody.FORM)

            // Convert bank statement URI to MultipartBody.Part if not null
            val bankStatementPart = request.bankStatement?.let { uri ->
                createMultipartFromUri("bank_statement", uri)
            }
            api.requestLoan(
                employeeType, position, income, loanAmount, loanDuration, loanType, bankStatementPart
            )
        }
    }
    // Creates a MultipartBody.Part from a URI
    private fun createMultipartFromUri(partName: String, uri: Uri): MultipartBody.Part? {
        try{
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val bytes = inputStream.readBytes()
            inputStream.close()

            val mimeType = contentResolver.getType(uri) ?: "application/pdf" // Default to PDF if type not found
            val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
            val fileName = getFileName(uri)

            return MultipartBody.Part.createFormData(partName, fileName, requestBody)
        }catch (e: Exception) {
            Log.d("RequestLoanRepository", "Error creating multipart from URI: ${e.message}")
            return null
        }
    }
    // Retrieves the file name from the URI
    private fun getFileName(uri: Uri): String{
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndexOrThrow(android.provider.MediaStore.MediaColumns.DISPLAY_NAME)
            if (nameIndex != - 1 && cursor.moveToFirst()) {
                return cursor.getString(nameIndex)
            }
        }
        return "bank_statement.pdf" // Default name if not found
    }

}