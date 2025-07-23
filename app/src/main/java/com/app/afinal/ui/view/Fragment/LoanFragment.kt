package com.app.afinal.ui.view.Fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.afinal.databinding.FragmentLoanBinding
import com.app.afinal.model.LoanModel
import com.app.afinal.service.APIClient
import com.app.afinal.service.intercepter.NetworkConnectionInterceptor
import com.app.afinal.service.repository.LoanRepository
import com.app.afinal.ui.adapter.LoanAdapter
import com.app.afinal.ui.view.BaseFragment
import com.app.afinal.utils.OnRequestResponse
import com.app.afinal.utils.toastHelper
import com.app.afinal.viewModel.LoanViewModel
import com.app.afinal.viewModel.factory.LoanViewModelFactory
import com.google.android.material.tabs.TabLayout

class LoanFragment: BaseFragment<FragmentLoanBinding>(), OnRequestResponse {
    override fun provideBinding(): FragmentLoanBinding {
        return FragmentLoanBinding.inflate(layoutInflater)
    }
    private lateinit var networkConnectionInterceptor: NetworkConnectionInterceptor
    private lateinit var api : APIClient
    private lateinit var loanRepository: LoanRepository
    private  var viewModel : LoanViewModel? = null
    private lateinit var factory : LoanViewModelFactory
    private var unpaidLoanList : List<LoanModel.Data.LoanData> = emptyList()
    private var paidLoanList : List<LoanModel.Data.LoanData> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        val tab = binding?.tabLayoutLoan ?: TabLayout(requireContext())
        showSkeleton()
       viewModel?.isLoanList?.observe(viewLifecycleOwner) { response ->
           response?.let {
                hideSkeleton()
               ItemByStatus(it)
               setUpTab(tab)
               displayItem(unpaidLoanList)
           }
       }
    }
    fun ItemByStatus(list : List<LoanModel.Data.LoanData>){
        unpaidLoanList = list.filter { it.status == "0"}
        paidLoanList = list.filter { it.status == "1"}
        Log.d("LoanFragment", "Unpaid Loans: ${unpaidLoanList.size}, Paid Loans: ${paidLoanList.size}")
    }
    fun displayItem(item : List<LoanModel.Data.LoanData>?){
        val items = item  ?: emptyList()
        binding?.tvTotalLoan?.text = " ${items.size}"
       if(items.isEmpty()){
           emptyLoan()
           binding?.rvLoanList?.adapter = null
       }else{
           showLoan()
           binding?.rvLoanList?.apply {
               layoutManager = LinearLayoutManager(requireContext())
               adapter = LoanAdapter(items)
           }
       }
    }
    fun setUpTab(tab : TabLayout){
        if (tab.tabCount == 0) {
            tab.addTab(tab.newTab().setText("Unpaid"))
            tab.addTab(tab.newTab().setText("Paid"))
            tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let{
                        when(it.position){
                            0 -> displayItem(unpaidLoanList)
                            1 -> displayItem(paidLoanList)
                        }
                    }
                }
                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }
    fun hideSkeleton() {
        binding?.progressBarLoan?.visibility = View.GONE
        binding?.rvLoanList?.visibility = View.VISIBLE
    }
    fun showSkeleton() {
        binding?.progressBarLoan?.visibility = View.VISIBLE
        binding?.rvLoanList?.visibility = View.GONE
    }
    fun emptyLoan(){
        val currentTab = binding?.tabLayoutLoan?.selectedTabPosition ?: 0
        binding?.tvNoLoans?.text = if(currentTab == 0){
            "No Unpaid Loans"
        } else {
            "No Paid Loans"
        }
        binding?.tvNoLoans?.visibility = View.VISIBLE
        binding?.rvLoanList?.visibility = View.GONE
    }
    fun showLoan(){
        binding?.tvNoLoans?.visibility = View.GONE
        binding?.rvLoanList?.visibility = View.VISIBLE
    }
    private fun init(){
        networkConnectionInterceptor = NetworkConnectionInterceptor()
        api = APIClient(requireContext(), networkConnectionInterceptor)
        loanRepository = LoanRepository(api, requireContext())
        factory = LoanViewModelFactory(loanRepository)
        viewModel = ViewModelProvider(this, factory)[LoanViewModel::class.java]
        viewModel?.listener = this

    }

    override fun onFailed(message: String) {
        context?.toastHelper("Failed to load loans: $message")
        Log.e("LoanFragment", "Error: $message")
    }
    companion object{
        const val LOAN_ID = "loanId"
    }

}