package com.app.afinal.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.afinal.service.repository.RequestLoanRepository
import com.app.afinal.viewModel.RequestLoanViewModel

class RequestLoanViewModelFactory(
    private val repository: RequestLoanRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RequestLoanViewModel::class.java)) {
            return RequestLoanViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}