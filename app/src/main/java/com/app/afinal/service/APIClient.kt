package com.app.afinal.service

import android.content.Context
import com.app.afinal.model.LoanDetailModel
import com.app.afinal.model.LoanModel
import com.app.afinal.service.intercepter.NetworkConnectionInterceptor
import com.app.afinal.service.intercepter.TokenInterceptor
import com.app.afinal.model.LoginModel
import com.app.afinal.model.LoginSuccessResponse
import com.app.afinal.model.PasswordModel
import com.app.afinal.model.ProfileModel
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
import retrofit2.http.Path

interface APIClient{
    @POST("login")
    suspend fun login(
        @Body info: LoginModel
    ): Response<LoginSuccessResponse>

    //profile
    @GET("borrower/me")
    suspend fun getProfile(): Response<ProfileModel>
    //update profile
    @Multipart
    @POST("profile/update")
    suspend fun updateProfile(
        @Part("first_name") firstName: RequestBody,
        @Part("last_name") lastName: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("email") email: RequestBody,
        @Part("address") address: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part profilePicture: MultipartBody.Part? = null
    ): Response<ResponseErrorModel>

    //update password
    @POST("change-password")
    suspend fun updatePassword(
        @Body info: PasswordModel
    ): Response<ResponseErrorModel>
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
        @Path("id") id: Int
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
