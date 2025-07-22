package com.app.afinal.ui.view.Fragment

import android.os.Bundle
import android.view.View
import com.app.afinal.R
import com.app.afinal.databinding.FragmentHomeLoginBinding
import com.app.afinal.ui.view.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeLoginBinding>() {
    override fun provideBinding(): FragmentHomeLoginBinding {
        return FragmentHomeLoginBinding.inflate(layoutInflater)
    }

    //set event listener, load/display data
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.btnRequestLoan?.setOnClickListener{
            replaceFragment(
                IncomeInformationFragment(),
                R.id.fragment
            )
        }
    }
}