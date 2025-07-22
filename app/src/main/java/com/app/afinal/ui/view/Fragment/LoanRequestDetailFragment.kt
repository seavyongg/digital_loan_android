package com.app.afinal.ui.view.Fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.app.afinal.databinding.FragmentLaonRequestDetailBinding
import com.app.afinal.model.RequestLoanResponse
import com.app.afinal.service.APIClient
import com.app.afinal.service.intercepter.NetworkConnectionInterceptor
import com.app.afinal.service.repository.RequestLoanRepository
import com.app.afinal.ui.view.BaseFragment
import com.app.afinal.ui.view.Fragment.RequestLoanFragment.Companion.REQUEST_LOAN_ID
import com.app.afinal.utils.dateFormat
import com.app.afinal.viewModel.RequestLoanViewModel
import com.app.afinal.viewModel.factory.RequestLoanViewModelFactory
import kotlinx.coroutines.launch

class LoanRequestDetailFragment: BaseFragment<FragmentLaonRequestDetailBinding>() {
    private lateinit var viewModel: RequestLoanViewModel
    private lateinit var networkConnectionInterceptor: NetworkConnectionInterceptor
    private lateinit var api: APIClient
    private lateinit var factory: RequestLoanViewModelFactory
    private lateinit var repository: RequestLoanRepository
    private val requestLoanId = arguments?.getInt(REQUEST_LOAN_ID)
    override fun provideBinding(): FragmentLaonRequestDetailBinding {
        return FragmentLaonRequestDetailBinding.inflate(layoutInflater)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        if (requestLoanId == null) {
            return
        }
        showSkeleton()
        onBack()
        viewModel.requestLoanList.observe(viewLifecycleOwner) { response ->
            response?.let {
                val requestLoanDetail = showProductDetail(requestLoanId)
                if (requestLoanDetail != null) {
                    hideSkeleton()
                    displayRequestLoanDetails(requestLoanDetail)
                } else {
                    showDialog("Error", "Request loan details not found.")
                }
            }
        }
    }
    private fun onBack() {
        binding?.btnClose?.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
    private fun showProductDetail(id: Int): RequestLoanResponse.Data.DataDetails? {
           try{
               val requestLoan = viewModel.requestLoanList.value
                val requestLoanDetail = requestLoan?.find { it.requestLoanId == id}
               return requestLoanDetail
           }catch (e: Exception) {
               e.printStackTrace()
               showDialog("Error", "Failed to load request loan details.")
                return null
           }

       }
    private fun displayRequestLoanDetails(requestLoan: RequestLoanResponse.Data.DataDetails) {
        binding?.apply {
           tvRequestLoanTitle.text = "RequestLoan ${requestLoan.requestLoanId}"
            tvDateRequested.text = dateFormat(requestLoan.createdAt!!)
            tvLoanAmount.text = requestLoan.loanAmount.toString()
            tvLoanType.text = requestLoan.loanType
            tvDuration.text = requestLoan.loanDuration
            tvStatus.text = requestLoan.status
        }
    }
    private  fun init(){
        networkConnectionInterceptor = NetworkConnectionInterceptor()
        api = APIClient(requireContext(), networkConnectionInterceptor)
        repository = RequestLoanRepository(api, requireContext())
        factory = RequestLoanViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[RequestLoanViewModel::class.java]
    }
    private fun showSkeleton() {
        binding?.progressBar?.visibility = View.VISIBLE
        binding?.llRequestLoanDetail?.visibility = View.GONE
    }
    private fun hideSkeleton() {
        binding?.progressBar?.visibility = View.GONE
        binding?.llRequestLoanDetail?.visibility = View.VISIBLE
    }
}