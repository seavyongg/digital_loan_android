package com.app.afinal.ui.view.Fragment

import com.app.afinal.databinding.FragmentLoanBinding
import com.app.afinal.ui.view.BaseFragment

class LoanHistoryFragment : BaseFragment<FragmentLoanBinding>() {
    override fun provideBinding(): FragmentLoanBinding {
        val binding = FragmentLoanBinding.inflate(layoutInflater)
        return binding
    }
}