package com.app.afinal.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.afinal.service.repository.UserRepository
import com.app.afinal.model.LoginSuccessResponse
import com.app.afinal.model.ProfileModel
import com.app.afinal.model.ProfileUpdateModel
import com.app.afinal.model.ResponseErrorModel
import com.app.afinal.utils.OnBackResponse
import com.app.afinal.utils.Coroutine
import com.app.afinal.utils.NoInternetException
import com.app.afinal.utils.OnRequestResponse

class AuthViewModel(
    private val userRepository: UserRepository,

): ViewModel() {

    private var _isLogin = MutableLiveData<LoginSuccessResponse>()
    val isLogin : LiveData<LoginSuccessResponse>
        get() = _isLogin

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean>
        get() = _isLoading

    var listener : OnBackResponse<ResponseErrorModel>? = null
    var listener_B : OnRequestResponse? = null
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
    private var _profileData = MutableLiveData<ProfileModel>()
    val profileData : LiveData<ProfileModel> get() = _profileData
    fun getProfile() {
        Coroutine.ioThanMain(
            {
                try {
                    userRepository.getProfile()
                } catch (e: NoInternetException) {
                    null
                }
            },
            {
                if (it != null) {
                    _profileData.value = it
                } else {
                    listener?.fail("Failed to load profile data")
                }
            }
        )
    }
    private var _updateData = MutableLiveData<ResponseErrorModel>()
    val updateData: LiveData<ResponseErrorModel> get() = _updateData
    fun updateProfile(profileUpdateModel: ProfileUpdateModel) {
        _isLoading.value = true
        Coroutine.main {
            try {
                val response = userRepository.updateProfile(profileUpdateModel)
                _updateData.value = response
                _isLoading.value = false
            }catch (e: NoInternetException) {
                _isLoading.value = false
                listener?.fail("No Internet Connection")
            } catch (e: Exception) {
                _isLoading.value = false
                listener?.fail("Failed to update profile. Please try again.")
            }
        }
    }

}