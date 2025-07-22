package com.app.afinal.ui.view.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.app.afinal.Application.MySharePreferences
import com.app.afinal.databinding.FragmentProfileBinding
import com.app.afinal.ui.view.BaseFragment
import com.app.afinal.ui.view.Activity.HomeActivity

class AccountFragment : BaseFragment<FragmentProfileBinding>() {
    private lateinit var mySharePreferences: MySharePreferences
    override fun provideBinding(): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.llLogout?.setOnClickListener{
            showDialog(
                "Logout",
                "Are you sure you want to logout?",
                "Logout",
                "Cancel",
            ) {
                showLogout()
            }
        }
    }
    // This function is called when the user confirms logout
    fun showLogout(){
        mySharePreferences = MySharePreferences(requireContext())
        mySharePreferences.clearToken()
        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish() //finish MainActivity that host this fragment
    }
}