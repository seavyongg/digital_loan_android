package com.app.afinal.ui.view.Fragment

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.app.afinal.Application.MySharePreferences
import com.app.afinal.R
import com.app.afinal.databinding.FragmentProfileBinding
import com.app.afinal.model.ProfileModel
import com.app.afinal.model.ResponseErrorModel
import com.app.afinal.service.APIClient
import com.app.afinal.service.intercepter.NetworkConnectionInterceptor
import com.app.afinal.service.repository.UserRepository
import com.app.afinal.ui.view.BaseFragment
import com.app.afinal.ui.view.Activity.HomeActivity
import com.app.afinal.utils.OnBackResponse
import com.app.afinal.utils.OnRequestResponse
import com.app.afinal.utils.dateFormat
import com.app.afinal.utils.toastHelper
import com.app.afinal.viewModel.AuthViewModel
import com.app.afinal.viewModel.factory.UserViewModelFactory
import com.squareup.picasso.Picasso

class ProfileFragment : BaseFragment<FragmentProfileBinding>(), OnRequestResponse, OnBackResponse<ResponseErrorModel> {
    private lateinit var mySharePreferences: MySharePreferences
    private lateinit var networkConnectionInterceptor: NetworkConnectionInterceptor
    private lateinit var api: APIClient
    private lateinit var repository : UserRepository
    private lateinit var viewModel: AuthViewModel
    private lateinit var factory: UserViewModelFactory
    override fun provideBinding(): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLogout()
        init()
        showSkeleton()
        viewModel.profileData.observe(viewLifecycleOwner) { profileData ->
            if(profileData != null){
                hideSkeleton()
                getInfo(profileData.data)
            }else{
                context?.toastHelper("Failed to load profile data")
            }
        }
        goToAccountInformation()
        gotToChangePassword()
    }
    fun goToAccountInformation(){
        binding?.llInformation?.setOnClickListener {
            val userInformationUpdateFragment = UserInformationUpdateFragment()
            replaceFragment(
                userInformationUpdateFragment,
                R.id.fragment
            )
        }
    }
//    fun goToAccountInformation(item: ProfileModel.user) {
//        val bundle = Bundle()
//        bundle.putString(FIRST_NAME, item.profile?.firstName)
//        bundle.putString(LAST_NAME, item.profile?.lastName)
//        bundle.putString(EMAIL, item.email)
//        bundle.putString(IMAGE, item.profile?.image)
//        bundle.putString(DOB, dateFormat(item.profile?.dob.toString()))
//        bundle.putString(PHONE, item.phone)
//        bundle.putString(ADDRESS, item.profile?.address)
//        bundle.putString(GENDER, item.profile?.gender)
//        binding?.llInformation?.setOnClickListener {
//            val userInformationUpdateFragment = UserInformationUpdateFragment()
//            userInformationUpdateFragment.arguments = bundle
//            replaceFragment(
//                userInformationUpdateFragment,
//                R.id.fragment
//            )
//        }
//    }
    fun gotToChangePassword(){
        binding?.llUpdatePassword?.setOnClickListener{
            replaceFragment(
                UpdatePasswordFragment(),
                R.id.fragment
            )
        }
    }
    fun hideSkeleton() {
        binding?.progressBar?.visibility = View.GONE
        binding?.llProfile?.visibility = View.VISIBLE
    }
    fun showSkeleton() {
        binding?.progressBar?.visibility = View.VISIBLE
        binding?.llProfile?.visibility = View.GONE
    }
    fun getInfo(item : ProfileModel.user){
        binding?.tvName?.text = "${item.profile?.lastName} ${item.profile?.firstName}"
        binding?.tvEmail?.text = item.email
        binding?.ivProfile?.let {
            Picasso.get()
                .load(item.profile?.image)
                .placeholder(R.drawable.ic_profile)
                .into(it)
        }
    }
    fun init(){
        networkConnectionInterceptor = NetworkConnectionInterceptor()
        api = APIClient(requireContext(), networkConnectionInterceptor)
        mySharePreferences = MySharePreferences(requireContext())
        repository = UserRepository(api, mySharePreferences)
        factory = UserViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)
        viewModel.getProfile()
        viewModel.listener_B = this
    }
    // This function is called when the user confirms logout
    fun showLogout(){
        binding?.llLogout?.setOnClickListener {
            showDialog(
                "Logout",
                "Are you sure you want to logout?",
                "Logout",
                "Cancel",
            ){
                mySharePreferences = MySharePreferences(requireContext())
                mySharePreferences.clearToken()
                val intent = Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
                requireActivity().finish() //finish MainActivity that host this fragment
            }
        }
    }

    override fun onFailed(message: String) {
        context?.toastHelper(message)
        Log.d("ProfileFragment", "onFailed: $message")
    }


    override fun success(message: ResponseErrorModel) {
        context?.toastHelper("Profile updated successfully")
        Log.d("ProfileFragment", "success: ${message.message}")
    }

    override fun fail(message: String) {
        context?.toastHelper(message)
        Log.d("ProfileFragment", "fail: $message")
    }
    companion object{
        const val TAG = "ProfileFragment"
        const val CURRENT_PASSWORD = "currentPassword"
        const val NEW_PASSWORD = "newPassword"
        const val FIRST_NAME = "firstname"
        const val LAST_NAME = "lastname"
        const val EMAIL = "email"
        const val IMAGE = "image"
        const val DOB = "dob"
        const val PHONE = "phone"
        const val ADDRESS = "address"
        const val GENDER = "gender"
    }
}