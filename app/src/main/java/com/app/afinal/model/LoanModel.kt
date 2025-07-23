package com.app.afinal.model

import com.google.gson.annotations.SerializedName

data class LoanModel(
    @SerializedName("data")
    var data : Data = Data()
){
    data class Data(
        @SerializedName("data")
        var loanData: List<LoanData> = emptyList(),
        @SerializedName("Pagination")
        var pagination: Pagination = Pagination(),
        @SerializedName("summary")
        var summary: Summary = Summary()
    ){
        data class LoanData(
            @SerializedName("id")
            var loanId: Int = 0,
            @SerializedName("loan_duration")
            var loanDuration: Int = 0,
            @SerializedName("status")
            var status: String = "",
            @SerializedName("request_loan")
            var requestLoan: RequestLoan = RequestLoan(),
            @SerializedName("created_at")
            var date: String = "",
            @SerializedName("updated_at")
            var updatedAt: String = ""
        ){
            data class RequestLoan(
                @SerializedName("id")
                var requestLoanId: Int = 0,
                @SerializedName("loan_amount")
                var loanAmount: String = "",
                @SerializedName("loan_type")
                var loanType: String = "",
                @SerializedName("created_at")
                var createdAt: String? = null
            )
        }
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
            @SerializedName("total_loan")
            var totalLoan: Double = 0.0,
            @SerializedName("total_repayment_amount")
            var totalRepaymentCount: Double = 0.0,
        )
    }
}
