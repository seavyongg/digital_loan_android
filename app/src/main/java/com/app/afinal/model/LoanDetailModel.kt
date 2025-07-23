package com.app.afinal.model

import com.google.gson.annotations.SerializedName

data class LoanDetailModel(
    @SerializedName("data")
    var data: item = item()
)
data class item(
    @SerializedName("id")
    var loanId : Int = 0,
    @SerializedName("loan_duration")
    var loanDuration: Int = 0,
    @SerializedName("loan_repayment")
    var loanRepayment: String = "",
    @SerializedName("revenue")
    var interestAmount: String = "",
    @SerializedName("loan_interest_rate")
    var interestRate: String = "",
    @SerializedName("loan_amount")
    var loanAmount: String = "",
    @SerializedName("status")
    var status : String = "",
    @SerializedName("schedule_repayment")
    var scheduleRepayment: List<ScheduleRepayment> = emptyList(),
    @SerializedName("user")
    var userData : UserData = UserData(),

)
data class ScheduleRepayment(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("repayment_date")
    var repaymentDate: String = "",
    @SerializedName("emi_amount")
    var emiAmount: String = "",
    @SerializedName("status")
    var status: String = "",
    @SerializedName("paid_date")
    var paidDate: String? = null,
)
data class UserData(
    @SerializedName("email")
    var email: String = "",
    @SerializedName("phone")
    var phoneNumber : String = "",
)
