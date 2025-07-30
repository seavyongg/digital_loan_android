package com.app.afinal.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.afinal.service.repository.RequestLoanRepository
import com.app.afinal.service.repository.UpdatePasswordRepository
import com.app.afinal.viewModel.RequestLoanViewModel
import com.app.afinal.viewModel.UpdatePasswordViewModel

class UpdatePasswordViewModelFactory(
    private val repository: UpdatePasswordRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdatePasswordViewModel::class.java)) {
            return UpdatePasswordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}