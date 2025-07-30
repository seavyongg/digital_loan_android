package com.app.afinal.ui.view

import android.os.Bundle
import com.app.afinal.R
import com.app.afinal.databinding.ActivityMainBinding
import com.app.afinal.ui.view.Fragment.ProfileFragment
import com.app.afinal.ui.view.Fragment.HomeFragment
import com.app.afinal.ui.view.Fragment.LoanFragment
import com.app.afinal.ui.view.Fragment.RequestLoanFragment

class MainActivity: BaseActivity<ActivityMainBinding>() {
    override fun provideBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar()
        replaceFragment(HomeFragment(), binding.fragment.id)
        handleBottomNavigation()
        }
    fun handleBottomNavigation() {
       binding.navigationView.setOnItemSelectedListener{
           menuItem ->
           when(menuItem.itemId){
                R.id.llHome -> replaceFragment(HomeFragment(), binding.fragment.id)
                R.id.llRequestLoan -> replaceFragment(RequestLoanFragment(), binding.fragment.id)
                R.id.llLoan -> replaceFragment(LoanFragment(), binding.fragment.id)
                else -> replaceFragment(ProfileFragment(), binding.fragment.id)
           }
           true
       }

    }

}