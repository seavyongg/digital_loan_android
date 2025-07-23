package com.app.afinal.service

import android.content.Context
import com.app.afinal.model.LoanDetailModel
import com.app.afinal.model.LoanModel
import com.app.afinal.service.intercepter.NetworkConnectionInterceptor
import com.app.afinal.service.intercepter.TokenInterceptor
import com.app.afinal.model.LoginModel
import com.app.afinal.model.LoginSuccessResponse
import com.app.afinal.model.RequestLoanModel
import com.app.afinal.model.RequestLoanResponse
import com.app.afinal.model.ResponseErrorModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface APIClient{
    @POST("login")
    suspend fun login(
        @Body info: LoginModel
    ): Response<LoginSuccessResponse>

    @Multipart
    @POST("borrower/request-loan")
    suspend fun requestLoan(
//        @Body info : RequestLoanModel
        @Part("employee_type") employeeType: RequestBody,
        @Part("position") position: RequestBody,
        @Part("income") income: RequestBody,
        @Part("loan_amount") loanAmount: RequestBody,
        @Part("loan_duration") loanDuration: RequestBody,
        @Part("loan_type") loanType: RequestBody,
        @Part bankStatement: MultipartBody.Part? = null

    ) : Response<ResponseErrorModel>

    //request Loan
    @GET("borrower/request-loan")
    suspend fun getRequestLoan(): Response<RequestLoanResponse>

    //laon
    @GET("borrower/loan")
    suspend fun getAllLoan(): Response<LoanModel>
    //loan detail
    @GET("borrower/loan/{id}")
    suspend fun getLoanDetail(
        @Part("id") id: Int
    ): Response<LoanDetailModel>



    companion object {
        operator fun invoke(
            context: Context,
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): APIClient {
            val tokenInterceptor = TokenInterceptor(context)
            val okHttpClient = okhttp3.OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .addInterceptor(tokenInterceptor)
                .build()
            return Retrofit.Builder()
                .baseUrl("https://api.jorngka.online/api/")
                .client(okHttpClient)
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build()
                .create(APIClient::class.java)
        }
    }

}
