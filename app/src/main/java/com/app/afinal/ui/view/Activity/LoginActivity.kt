package com.app.afinal.ui.view.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.app.afinal.Application.MySharePreferences
import com.app.afinal.service.APIClient
import com.app.afinal.service.intercepter.NetworkConnectionInterceptor
import com.app.afinal.service.repository.UserRepository
import com.app.afinal.databinding.ActivityLoginBinding
import com.app.afinal.model.LoginSuccessResponse
import com.app.afinal.viewModel.AuthViewModel
import android.util.Patterns
import androidx.core.content.ContentProviderCompat.requireContext
import com.app.afinal.utils.OnBackResponse
import com.app.afinal.ui.view.BaseActivity
import com.app.afinal.ui.view.MainActivity

class LoginActivity: BaseActivity<ActivityLoginBinding>() , OnBackResponse<LoginSuccessResponse> {
    private lateinit var viewModel: AuthViewModel
    private lateinit var userRepository: UserRepository
    private lateinit var networkConnectionInterceptor: NetworkConnectionInterceptor
    private lateinit var mySharePreferences: MySharePreferences
    private lateinit var api: APIClient
    override fun provideBinding(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar()
        init()
        binding.btnLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (email.isEmpty()) {
                binding.editTextEmail.error = "Email is  empty"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.editTextPassword.error = "Password is empty"
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.editTextEmail.error = "Invalid email format"
                return@setOnClickListener
            }
            if (password.length < 6) {
                binding.editTextPassword.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }
                viewModel.login(email, password)
        }
    }
    private fun init(){
        networkConnectionInterceptor = NetworkConnectionInterceptor()
        mySharePreferences = MySharePreferences(applicationContext)
        api = APIClient(applicationContext, networkConnectionInterceptor)
        userRepository = UserRepository(api, applicationContext ,mySharePreferences)
        viewModel = AuthViewModel(userRepository)
    }

    override fun success(message: LoginSuccessResponse) {
        mySharePreferences.saveToken(message.accessToken ?: "")
        mySharePreferences.saveUserId(message.user?.userId ?: 0)

        Handler(Looper.getMainLooper()).postDelayed({
            Log.d("LoginActivity", "Login successful, navigating to MainActivity")
            Toast.makeText(this , "Login successfully.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }, 1000)
    }

    override fun fail(message: String) {
        showDialog(
            "Login Failed",
            message,
        )
        Log.d("LoginActivity", "Login failed: $message")
    }


}
