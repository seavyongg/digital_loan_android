package com.app.afinal.model

import android.net.Uri
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import java.util.function.BinaryOperator

data class RequestLoanModel(
    @SerializedName("employee_type")
    var employeeType: String ,
    @SerializedName("position")
    var position: String ,
    @SerializedName("income")
    var income:Double,
    @SerializedName("loan_amount")
    var loanAmount : Double,
    @SerializedName("loan_duration")
    var loanDuration : Int ,
    @SerializedName("loan_type")
    var loanType : String,
//    @SerializedName("bank_statement")
     var bankStatement: Uri?= null
)
