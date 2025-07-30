package com.app.afinal.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.afinal.model.LoanDetailModel
import com.app.afinal.model.LoanModel
import com.app.afinal.service.repository.LoanRepository
import com.app.afinal.utils.Coroutine
import com.app.afinal.utils.OnRequestResponse

class LoanViewModel(
    val repository : LoanRepository,
    id: String? = null
): ViewModel() {
    private val _isLoanList = MutableLiveData<List<LoanModel.Data.LoanData>>()
    val isLoanList : LiveData<List<LoanModel.Data.LoanData>>
        get() = _isLoanList
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _loanDetail = MutableLiveData<LoanDetailModel.item>()
    val loanDetail: LiveData<LoanDetailModel.item>
        get() = _loanDetail
    var listener: OnRequestResponse? = null

    init{
        _isLoading.value = true
        getAllLoan()
    }
    fun getAllLoan(){
        Coroutine.ioThanMain(
            {
                try{
                    repository.getAllLoan()

                }catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("LoanViewModel", "Error fetching loan data: ${e.message}")
                    null
                }
            },
            {
                if (it != null){
                    _isLoanList.value = it.data.loanData
                    _isLoading.value = false
                    Log.d("LoanViewModel", "${it.data.loanData}")
                }else{
                    listener?.onFailed("Failed to fetch loan data. Please try again later.")
                }
            }
        )
    }

    fun getProductById(id : String){
        Log.d("LoanViewModel", "Fetching loan detail for ID: $id")
        Coroutine.ioThanMain(
            {
                try{
                    repository.getLoanDetail(id.toInt())
                }catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("LoanViewModel", "Error fetching loan detail: ${e.message}")
                    null
                }
            },
            {
                if( it != null){
                    _loanDetail.value = it.data
                    _isLoading.value = false
                    Log.d("LoanViewModel", "Loan detail fetched successfully: ${it.data}")
                }else{
                    listener?.onFailed("Failed to fetch loan detail. Please try again later.")
                }
            }
        )
    }
}