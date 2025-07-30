package com.app.afinal.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.afinal.service.repository.LoanRepository
import com.app.afinal.viewModel.LoanViewModel

class LoanViewModelFactory(
    private val repository: LoanRepository,
    private val id: String? = null
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoanViewModel::class.java)) {
            return LoanViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}