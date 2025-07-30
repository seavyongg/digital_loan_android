package com.app.afinal.ui.view.Fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.app.afinal.Application.MySharePreferences
import com.app.afinal.R
import com.app.afinal.databinding.FragmentInformationBinding
import com.app.afinal.model.ProfileUpdateModel
import com.app.afinal.service.APIClient
import com.app.afinal.service.intercepter.NetworkConnectionInterceptor
import com.app.afinal.service.repository.UserRepository
import com.app.afinal.ui.view.BaseFragment
import com.app.afinal.utils.OnBackResponse
import com.app.afinal.utils.OnRequestResponse
import com.app.afinal.utils.dateFormat
import com.app.afinal.utils.toastHelper
import com.app.afinal.viewModel.AuthViewModel
import com.app.afinal.viewModel.factory.UserViewModelFactory
import com.squareup.picasso.Picasso

class UserInformationUpdateFragment: BaseFragment<FragmentInformationBinding>(),
    OnBackResponse<String> , OnRequestResponse {
        private lateinit var api : APIClient
        private lateinit var networkConnectionInterceptor: NetworkConnectionInterceptor
        private lateinit var mySharePreferences: MySharePreferences
        private lateinit var repository: UserRepository
        private lateinit var viewModel: AuthViewModel
        private var pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result -> if( result.resultCode == Activity.RESULT_OK){
                result.data?.data?.let { uri ->
                    imageUrl = uri
                    binding?.ivProfile?.setImageURI(uri)
                    binding?.ivProfile?.let {
                        Picasso.get().load(uri).into(it)
                    }
                }
        }
        }
        var firstname: String? = null
    var lastname : String? = null
    var gender : String? = null
    var dateOfBirth : String? = null
    var email : String? = null
    var phone : String? = null
    var address : String? = null
    var image : String? = null
    var imageUrl : Uri? = null
    private lateinit var factory: UserViewModelFactory
    override fun provideBinding(): FragmentInformationBinding {
        return FragmentInformationBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        showUserInformation()
        showImageProfile()
        getDob()
        binding?.tvUpdated?.setOnClickListener {
            getUserInformation()
            Log.d("UserInformationUpdateFragment", "onViewCreated called ${getUserInformation()}")
            context?.toastHelper("Profile Updated Successfully")
        }
    }

    //show image in profile
    fun showImageProfile(){
       binding?.ivProfile?.setOnClickListener {
            getImageFromGallery()
        }
    }

    //get image from gallery
    fun getImageFromGallery() {
       val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
           type = "image/png,image/jpeg,image/jpg"
           addCategory(Intent.CATEGORY_OPENABLE)
       }
        if(intent.resolveActivity(requireActivity().packageManager) != null) {
            pickImageLauncher.launch(intent)
        } else {
            context?.toastHelper("No application available to select image")
        }
    }
    //get date of birth
    fun getDob() {
        binding?.editTextDateOfBirth?.setOnClickListener {
            val calendar = java.util.Calendar.getInstance()
            val year = calendar.get(java.util.Calendar.YEAR)
            val month = calendar.get(java.util.Calendar.MONTH)
            val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)
            val binding = binding ?: return@setOnClickListener
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                   // binding.editTextDateOfBirth.setText("$selectedDay-${selectedMonth + 1}-$selectedYear")
                    dateOfBirth = dateFormat("$selectedDay-${selectedMonth + 1}-$selectedYear")
                    binding.editTextDateOfBirth.setText(dateOfBirth)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }
    fun getUserInformation(): ProfileUpdateModel {
        return ProfileUpdateModel().apply {
            firstname = binding?.editTextFirstname?.text.toString()
            lastname = binding?.editTextLastname?.text?.toString()
            gender = binding?.editTextGender?.text?.toString()
            dob = binding?.editTextDateOfBirth?.text?.toString()
            address = binding?.editTextAddress?.text?.toString()
            phone = binding?.editTextPhone?.text?.toString()
            image = imageUrl
            email = binding?.editTextEmail?.text?.toString()
        }
    }
    //get user information
    fun showUserInformation(){
        viewModel.getProfile()
        viewModel.profileData.observe(viewLifecycleOwner) { profileData ->
            firstname = profileData.data.profile?.firstName
            lastname = profileData.data.profile?.lastName
            gender = profileData.data.profile?.gender
            dateOfBirth = dateFormat(profileData.data.profile?.dob.toString())
            email = profileData.data.email
            phone = profileData.data.phone
            address = profileData.data.profile?.address
            image = profileData.data.profile?.image
            imageUrl = if (!image.isNullOrEmpty()) Uri.parse(image) else null
            if (profileData != null) {
                binding?.apply {
                   editTextFirstname.setText(firstname)
                    editTextLastname.setText(lastname)
                    editTextGender.setText(gender)
                    editTextDateOfBirth.setText(dateOfBirth)
                    editTextEmail.setText(email)
                    editTextPhone.setText(phone)
                    editTextAddress.setText(address)
                    tvName.text = "${firstname} ${lastname}"
                    Picasso.get().load(image).into(ivProfile)
                }
            } else {
                context?.toastHelper("Failed to load profile data")
            }
        }
    }
    //init function
    fun init(){
        networkConnectionInterceptor = NetworkConnectionInterceptor()
        api = APIClient(requireContext(),networkConnectionInterceptor)
        mySharePreferences = MySharePreferences(requireContext())
        repository = UserRepository(api, mySharePreferences)
        factory = UserViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
      //  viewModel.getProfile()
        viewModel.listener_B = this
    }
    // handle back response
    override fun success(message: String) {
        // Handle success response
    }

    // Handle failure response
    override fun fail(message: String) {
        // Handle failure response
    }

    //on request response
    override fun onFailed(message: String) {
        TODO("Not yet implemented")
    }
}