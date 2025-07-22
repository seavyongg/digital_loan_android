package com.app.afinal.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.afinal.service.repository.UserRepository
import com.app.afinal.model.LoginSuccessResponse
import com.app.afinal.utils.OnBackResponse
import com.app.afinal.utils.Coroutine
import com.app.afinal.utils.NoInternetException

class AuthViewModel(
    private val userRepository: UserRepository,

): ViewModel() {

    private var _isLogin = MutableLiveData<LoginSuccessResponse>()
    val isLogin : LiveData<LoginSuccessResponse>
        get() = _isLogin

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean>
        get() = _isLoading

    var listener : OnBackResponse<LoginSuccessResponse>? = null
    fun login( email: String, password: String){
        if(email.isEmpty() || password.isEmpty()){
            Log.d("AuthViewModel", "Invalid Email or Password")
            return
        }
        Coroutine.main{
            try{
                _isLoading.value = true
                val userLogin = userRepository.userLogin(email, password)
               userLogin.user?.let{
                   _isLogin.value = userLogin
                   listener?.success(userLogin)
                   Log.d("AuthViewModel", "Login successful: ${userLogin.user?.email}")

               }
                return@main
            }catch (e : NoInternetException){
                listener?.fail("No Internet Connection")
            }catch (e: Exception){
                listener?.fail("Please try again.")
            }
        }
    }


}