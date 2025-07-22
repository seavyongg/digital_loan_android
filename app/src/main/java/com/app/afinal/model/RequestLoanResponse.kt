package com.app.afinal.model

import com.google.gson.annotations.SerializedName

data class RequestLoanResponse(
    @SerializedName("data")
   var data: Data = Data()
){
   data class Data(
       @SerializedName("data")
       var data: List<DataDetails> = emptyList(),
       @SerializedName("pagination")
       var pagination: Pagination = Pagination(),
       @SerializedName("summary")
       var summary: Summary = Summary()
   ){
       data class DataDetails(
           @SerializedName("id")
           var requestLoanId: Int = -1,
           @SerializedName("loan_amount")
           var loanAmount: String = "",
           @SerializedName("loan_duration")
           var loanDuration: String = "",
           @SerializedName("loan_type")
           var loanType: String = "",
           @SerializedName("rejection_reason")
           var rejectionReason: String = "",
           @SerializedName("user_id")
           var userId: Int = -1,
           @SerializedName("status")
           var status: String = "",
           @SerializedName("update_at")
           var updateAt: String = "",
           @SerializedName("created_at")
           var createdAt: String? = "",

       )
       data class Pagination(
           @SerializedName("current_page")
           var currentPage: Int = 0,
           @SerializedName("last_page")
           var lastPage: Int = 0,
           @SerializedName("per_page")
           var perPage: Int = 0,
           @SerializedName("total")
           var total: Int = 0
       )
       data class Summary(
           @SerializedName("total_request")
           var totalRequessts: Int = 0,
           @SerializedName("total_request_amount")
           var totalRequestAmount: String = "",
           @SerializedName("credit_score")
           var creditScore: Int = 0,
           @SerializedName("status_counts")
           var totalCounts: StatusCounts = StatusCounts()
       ){
              data class StatusCounts(
                @SerializedName("pending")
                var pending: Int = 0,
                @SerializedName("eligible")
                var eligible: Int = 0,
                @SerializedName("approved")
                var approved: Int = 0,
                @SerializedName("rejected")
                var rejected: Int = 0
              )
       }
   }
}
