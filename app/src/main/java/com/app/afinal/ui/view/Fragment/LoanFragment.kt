package com.app.afinal.ui.view.Fragment

import com.app.afinal.databinding.FragmentLoanBinding
import com.app.afinal.ui.view.BaseFragment

class LoanFragment: BaseFragment<FragmentLoanBinding>() {
    override fun provideBinding(): FragmentLoanBinding {
        return FragmentLoanBinding.inflate(layoutInflater)
    }

}