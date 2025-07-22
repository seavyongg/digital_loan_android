package com.app.afinal.ui.view.Fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.afinal.R
import com.app.afinal.databinding.FragmentLoanRequestBinding
import com.app.afinal.model.RequestLoanResponse
import com.app.afinal.service.APIClient
import com.app.afinal.service.intercepter.NetworkConnectionInterceptor
import com.app.afinal.service.repository.RequestLoanRepository
import com.app.afinal.ui.adapter.RequestLoanAdapter
import com.app.afinal.ui.view.BaseFragment
import com.app.afinal.utils.OnRequestResponse
import com.app.afinal.utils.toastHelper
import com.app.afinal.viewModel.RequestLoanViewModel
import com.app.afinal.viewModel.factory.RequestLoanViewModelFactory
import com.google.android.material.tabs.TabLayout

class RequestLoanFragment: BaseFragment<FragmentLoanRequestBinding>(), OnRequestResponse {
    override fun provideBinding(): FragmentLoanRequestBinding {
        return FragmentLoanRequestBinding.inflate(layoutInflater)
    }
    private lateinit var networkConnectionInterceptor: NetworkConnectionInterceptor
    private lateinit var api : APIClient
    private lateinit var requestLoanRepository: RequestLoanRepository
    private lateinit var factory: RequestLoanViewModelFactory
    private var viewModel: RequestLoanViewModel? = null
    private var pendingList : List<RequestLoanResponse.Data.DataDetails> = emptyList()
    private var rejectedList : List<RequestLoanResponse.Data.DataDetails> = emptyList()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        showSkeleton()
        binding?.ivAddRequest?.setOnClickListener{
            replaceFragment(
                IncomeInformationFragment(),
                R.id.fragment
            )
        }
       val tab = binding?.tabLayoutLoanRequests ?: TabLayout(requireContext())
        viewModel?.requestLoanList?.observe(viewLifecycleOwner) { response ->
            response?.let {
                hideSkeleton()
                showRequestByStatus(it)
                showRequest(pendingList)
                setupTab(tab)
            }

        }
    }

    fun showRequestByStatus(list : List<RequestLoanResponse.Data.DataDetails>){
        pendingList = list.filter { it.status.equals("eligible" , true) }
        rejectedList = list.filter { it.status.equals("rejected", true) }
    }
    private fun setupTab(tabLayout: TabLayout){
        if(tabLayout.tabCount == 0){
            tabLayout.addTab(tabLayout.newTab().setText("Pending"))
            tabLayout.addTab(tabLayout.newTab().setText("Rejected"))
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        when (it.position) {
                            0 -> showRequest(pendingList)
                            1 -> showRequest(rejectedList)
                            else -> showRequest(pendingList)
                        }
                    }
                }
                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }
    private fun showRequest(item : List<RequestLoanResponse.Data.DataDetails>){
        binding?.tvCount?.text = "Total: ${item.size}"
        binding?.recyclerViewLoanRequests?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = RequestLoanAdapter(item).apply {
                onItemClickListener = { position ->
                    val request = item[position]
                    val intent = Bundle().apply {
                        putInt(REQUEST_LOAN_ID, request.requestLoanId)
                    }
                    intent.let {
                        replaceFragment(
                            LoanRequestDetailFragment().apply {
                                arguments = it
                            },
                            R.id.fragment
                        )
                    }
                }
            }
        }
    }
    private fun showSkeleton(){
        binding?.progressBar?.visibility = View.VISIBLE
        binding?.recyclerViewLoanRequests?.visibility = View.GONE
    }
    private fun hideSkeleton(){
        binding?.progressBar?.visibility = View.GONE
        binding?.recyclerViewLoanRequests?.visibility = View.VISIBLE
    }

    override fun onFailed(message: String) {
        requireContext().toastHelper("Failed to load requests: $message")
    }
   private  fun init(){
        networkConnectionInterceptor = NetworkConnectionInterceptor()
        api = APIClient(requireContext(), networkConnectionInterceptor)
        requestLoanRepository = RequestLoanRepository(api, requireContext())
        factory = RequestLoanViewModelFactory(requestLoanRepository)
        viewModel = ViewModelProvider(this, factory)[RequestLoanViewModel::class.java]
        viewModel?.listener_B = this
    }
    companion object{
        const val REQUEST_LOAN_ID = "requestLoanId"
    }
}