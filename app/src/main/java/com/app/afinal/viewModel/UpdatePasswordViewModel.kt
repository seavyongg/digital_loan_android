package com.app.afinal.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.afinal.model.PasswordModel
import com.app.afinal.model.ResponseErrorModel
import com.app.afinal.service.repository.UpdatePasswordRepository
import com.app.afinal.utils.Coroutine
import com.app.afinal.utils.OnBackResponse

class UpdatePasswordViewModel(
    private val repository: UpdatePasswordRepository
): ViewModel() {
    private var _updatePasswordResponse = MutableLiveData<ResponseErrorModel>()
    val updatePasswordResponse: MutableLiveData<ResponseErrorModel>
        get() = _updatePasswordResponse
    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean>
        get() = _isLoading
    var listener: OnBackResponse<ResponseErrorModel>? = null

    fun updatePassword(
         request: PasswordModel
    ){
      _isLoading.value = true
       Coroutine.main {
            try {
                val response = repository.updatePassword(request)
                _updatePasswordResponse.value = response
                _isLoading.value = false
                listener?.success(response)
            } catch (e: Exception) {
                _isLoading.value = false
                listener?.fail("Failed to update password: ${e.message}")
            }
       }
    }
}