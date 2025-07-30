package com.app.afinal.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.afinal.model.RequestLoanModel
import com.app.afinal.model.RequestLoanResponse
import com.app.afinal.model.ResponseErrorModel
import com.app.afinal.service.repository.RequestLoanRepository
import com.app.afinal.utils.ApiException
import com.app.afinal.utils.Coroutine
import com.app.afinal.utils.NoInternetException
import com.app.afinal.utils.OnBackResponse
import com.app.afinal.utils.OnRequestResponse
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class RequestLoanViewModel(
    var repository: RequestLoanRepository
): ViewModel() {
    private val _isRequestLoan = MutableLiveData<ResponseErrorModel>()
    val isRequestLoan : LiveData<ResponseErrorModel>
        get() = _isRequestLoan

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _requestLoanList = MutableLiveData<List<RequestLoanResponse.Data.DataDetails>>()
    val requestLoanList : LiveData<List<RequestLoanResponse.Data.DataDetails>> get () = _requestLoanList
    var listener : OnBackResponse<ResponseErrorModel>? = null
    var listener_B : OnRequestResponse? = null
    fun requestLoan(request : RequestLoanModel){
        Log.d("requestloanfragment ", "Response: $request")
        _isLoading.value = true
        Coroutine.ioThanMain(
            {
                 try{
                       val response = repository.requestLoan(request)
                          Log.d("requestloanfragment", "Response: $response")
                          response
                   }catch (e: ApiException) {
                       Log.d("requestloanfragment", "Response: ${e.message}")
                       null
                   }
            },
            {response ->
                if (response != null) {
                    response.also { _isRequestLoan.value = it }
                    _isLoading.value = false
                    listener?.success(response)
                } else {
                    _isLoading.value = false
                    listener?.fail( "You are already requested a loan or your request is still pending.")
                    Log.d("requestloanfragment", "Response: ")
                }
            }
        )

    }
    init {
        _isLoading.value = true
        getAllRequestLoan()
    }
    fun getAllRequestLoan(){
        Coroutine.ioThanMain(
            {
                try{
                    repository.getAllRequestLoan()
                }catch (e: NoInternetException){
                    Log.d("RequestLoanViewModel", "No internet connection: ${e.message}")
                    null
                }
            },
            {
                if(it != null){
                    _requestLoanList.value = it.data.data ?: emptyList()
                    _isLoading.value = false
                }else{
                    listener?.fail("Failed to load loan requests")
                }
            }
        )
    }

}

