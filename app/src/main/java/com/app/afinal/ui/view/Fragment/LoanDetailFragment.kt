package com.app.afinal.ui.view.Fragment

import android.os.Bundle
import android.os.NetworkOnMainThreadException
import android.util.Log
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.app.afinal.R
import com.app.afinal.databinding.FragmentLoanDetailBinding
import com.app.afinal.model.LoanDetailModel
import com.app.afinal.service.APIClient
import com.app.afinal.service.intercepter.NetworkConnectionInterceptor
import com.app.afinal.service.repository.LoanRepository
import com.app.afinal.ui.view.BaseFragment
import com.app.afinal.ui.view.Fragment.LoanFragment.Companion.LOAN_ID
import com.app.afinal.utils.OnRequestResponse
import com.app.afinal.utils.dateFormat
import com.app.afinal.utils.toastHelper
import com.app.afinal.viewModel.LoanViewModel
import com.app.afinal.viewModel.factory.LoanViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab

class LoanDetailFragment: BaseFragment<FragmentLoanDetailBinding>(), OnRequestResponse {
    override fun provideBinding(): FragmentLoanDetailBinding {
        return FragmentLoanDetailBinding.inflate(layoutInflater)
    }
    private lateinit var networkConnectionInterceptor: NetworkConnectionInterceptor
    private lateinit var api : APIClient
    private lateinit var repository: LoanRepository
    private lateinit var factory : LoanViewModelFactory
    private var viewModel: LoanViewModel? = null
    private var LoanItem : LoanDetailModel.item? = LoanDetailModel.item()
    private var scheduleItem : List<LoanDetailModel.item.ScheduleRepayment?> = emptyList()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        onBack()
        showSketleton()
       val id = arguments?.getString(LOAN_ID).toString()
        Log.d("LoanDetailFragmentGetId", "Loan ID: $id")
        viewModel?.getProductById(id)
        viewModel?.loanDetail?.observe(viewLifecycleOwner) { loanDetail ->
            if (loanDetail != null) {
                hideSkeleton()
                LoanItem = loanDetail
                scheduleItem = loanDetail.scheduleRepayment ?: emptyList()
                initView(loanDetail)
            } else {
                binding?.root?.context?.toastHelper("Failed to load loan details.")
            }
        }

    }
    private fun onBack(){
        binding?.tvBack?.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
    private fun init(){
        networkConnectionInterceptor = NetworkConnectionInterceptor()
        api = APIClient(requireContext(), networkConnectionInterceptor)
        repository = LoanRepository(api, requireContext())
        factory = LoanViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(LoanViewModel::class.java)
        viewModel?.listener = this
    }
    private fun initView(item : LoanDetailModel.item){
       binding?.apply {
           tvLoanAmount.text = "$ ${item.loanAmount}"
           tvLoanRateAmount.text = "$ ${item.interestAmount}"
           tvLoanRepaymentAmount.text = "$ ${item.loanRepayment}"
           tvLoanDuration.text = item.loanDuration.toString()
           tvLoanStatus.text = if(item.status == "0") "Unpaid" else "Paid"
           tvInterestRate.text = "${item.interestRate} %"
           tvEmail.text = "Email ${item.userData.email}"
           tvPhone.text = "Phone ${item.userData.phoneNumber}"
       }
        initSchedule(scheduleItem)
    }
    fun initSchedule(items : List<LoanDetailModel.item.ScheduleRepayment?>){
        binding?.apply {
            tlLoanSchedule.removeViews(1, tlLoanSchedule.childCount - 1) // remove old view except header
            for ((index, item) in items.withIndex()) {
                val tableRow = TableRow(requireContext())

                val tvId = TextView(requireContext())
                tvId.text = (index + 1).toString()
                tvId.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                val tvDueDate = TextView(requireContext())
                tvDueDate.text = dateFormat(item?.repaymentDate.toString())
                tvDueDate.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                tvDueDate.textAlignment = View.TEXT_ALIGNMENT_CENTER
                val tvScheduleAmount = TextView(requireContext())
                tvScheduleAmount.text = "$ ${item?.emiAmount}"
                tvScheduleAmount.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                tvScheduleAmount.textAlignment = View.TEXT_ALIGNMENT_CENTER
                val tvStatus = TextView(requireContext())
                tvStatus.text = if(item?.status == "0") "Unpaid" else if (item?.status == "1") "Paid" else "Late"
                tvStatus.setTextColor(
                    when (item?.status) {
                        "0" -> ContextCompat.getColor(requireContext(), R.color.error)
                        "2" -> ContextCompat.getColor(requireContext(), R.color.warning)
                        else -> ContextCompat.getColor(requireContext(), R.color.success)
                    }
                )
                tvStatus.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                tvStatus.textAlignment = View.TEXT_ALIGNMENT_CENTER
                val repaid = TextView(requireContext())
                repaid.text = if (item?.paidDate != null) "${dateFormat(item.paidDate.toString())}" else "X"
                repaid.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                repaid.textAlignment = View.TEXT_ALIGNMENT_CENTER
                tableRow.addView(tvId)
                tableRow.addView(tvDueDate)
                tableRow.addView(tvScheduleAmount)
                tableRow.addView(tvStatus)
                tableRow.addView(repaid)
                tableRow.setPadding(8, 8, 8, 8)
                tableRow.setBackgroundResource(R.color.light)
                tlLoanSchedule.addView(tableRow)
            }
        }
    }
    private fun showSketleton() {
        binding?.progressBar?.visibility = View.VISIBLE
        binding?.llLoanDetail?.visibility = View.GONE
    }
    private fun hideSkeleton() {
        binding?.progressBar?.visibility = View.GONE
        binding?.llLoanDetail?.visibility = View.VISIBLE
    }
    override fun onFailed(message: String) {
        showDialog("Error", message,).let {
            binding?.root?.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
        binding?.root?.context?.toastHelper(message)
    }
}