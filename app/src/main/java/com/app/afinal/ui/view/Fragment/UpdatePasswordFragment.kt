package com.app.afinal.ui.view.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.app.afinal.databinding.FragmentUpdatePasswordBinding
import com.app.afinal.model.PasswordModel
import com.app.afinal.model.ResponseErrorModel
import com.app.afinal.service.APIClient
import com.app.afinal.service.intercepter.NetworkConnectionInterceptor
import com.app.afinal.service.repository.UpdatePasswordRepository
import com.app.afinal.ui.view.BaseFragment
import com.app.afinal.utils.OnBackResponse
import com.app.afinal.viewModel.AuthViewModel
import com.app.afinal.viewModel.UpdatePasswordViewModel
import com.app.afinal.viewModel.factory.UpdatePasswordViewModelFactory

class UpdatePasswordFragment : BaseFragment<FragmentUpdatePasswordBinding>(), OnBackResponse<ResponseErrorModel> {
    private lateinit var api : APIClient
    private lateinit var networkConnectionInterceptor: NetworkConnectionInterceptor
    private lateinit var factory: UpdatePasswordViewModelFactory
    private lateinit var viewModel: UpdatePasswordViewModel
    private lateinit var repository: UpdatePasswordRepository
    override fun provideBinding(): FragmentUpdatePasswordBinding {
        return FragmentUpdatePasswordBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBack()
        init()
        binding?.btnUpdate?.setOnClickListener {
           val oldPassword = binding?.editTextCurrentPassword?.text.toString().trim()
            val newPassword = binding?.editTextNewPassword?.text.toString().trim()
            val confirmPassword = binding?.editTextConfirmPassword?.text.toString().trim()

            if (validateData(oldPassword, newPassword, confirmPassword)) {
                val passwordModel = getData(oldPassword, newPassword)
                viewModel.updatePassword(passwordModel)
            }
        }

    }
    fun getData(
        oldPassword: String,
        newPassword: String,
    ): PasswordModel {
        return PasswordModel(
            currentPassword = oldPassword,
            newPassword = newPassword
        )
    }
    fun validateData(
        oldPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Boolean {
        if (oldPassword.isEmpty()) {
            binding?.editTextCurrentPassword?.error = "Old password is required"
            return false
        }
        if (newPassword.isEmpty()) {
            binding?.editTextNewPassword?.error = "New password is required"
            return false
        }
        if (confirmPassword.isEmpty()) {
            binding?.editTextConfirmPassword?.error = "Confirm password is required"
            return false
        }
        if (newPassword != confirmPassword) {
            binding?.editTextConfirmPassword?.error = "Passwords do not match"
            return false
        }
        return true
    }
    fun onBack(){
        binding?.llHeader?.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
    //init viewModel
    fun init(){
        networkConnectionInterceptor = NetworkConnectionInterceptor()
        api = APIClient(requireContext(), networkConnectionInterceptor)
        repository = UpdatePasswordRepository(api)
        factory = UpdatePasswordViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[UpdatePasswordViewModel::class.java]
        viewModel.listener = this
    }
    override fun success(message: ResponseErrorModel) {
        showDialog("Success", "Password updated successfully.",
            ){
            binding?.root?.postDelayed({
                requireActivity().supportFragmentManager.popBackStack()
            }, 1000) // Delay for 1 second before popping the back stack
        }
    }
    override fun fail(message: String) {
        showDialog("Error", message)
        Log.d("UpdatePasswordFragment", "Error: $message")
    }
}

