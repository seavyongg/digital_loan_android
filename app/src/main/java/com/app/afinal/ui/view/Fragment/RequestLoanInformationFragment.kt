package com.app.afinal.ui.view.Fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.app.afinal.R
import com.app.afinal.databinding.FragmentLoanInformationBinding
import com.app.afinal.model.RequestLoanModel
import com.app.afinal.model.RequestLoanResponse
import com.app.afinal.model.ResponseErrorModel
import com.app.afinal.service.APIClient
import com.app.afinal.service.intercepter.NetworkConnectionInterceptor
import com.app.afinal.service.repository.RequestLoanRepository
import com.app.afinal.ui.view.BaseFragment
import com.app.afinal.utils.OnBackResponse
import com.app.afinal.viewModel.RequestLoanViewModel
import com.app.afinal.viewModel.factory.RequestLoanViewModelFactory

class RequestLoanInformationFragment : BaseFragment<FragmentLoanInformationBinding>(), OnBackResponse<ResponseErrorModel> {
    private var employeeType: String = "personal"
    private var position: String = ""
    private var income: Double = 0.0
    private var bankStatement: Uri? = null
    private var loanType : String = ""
    private var duration: Int = 0
    private var amount: Double = 0.0

    private lateinit var requestLoanRepository: RequestLoanRepository
    private lateinit var viewModel: RequestLoanViewModel
    private lateinit var networkConnectionInterceptor: NetworkConnectionInterceptor
    private lateinit var api : APIClient
    private lateinit var viewModelFactory : RequestLoanViewModelFactory

    override fun provideBinding(): FragmentLoanInformationBinding {
        val binding = FragmentLoanInformationBinding.inflate(layoutInflater)
        return binding
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBack()
        init()
        onSubmit()
    }
    private fun onBack(){
        binding?.btnBack?.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
    private fun init( ){
        networkConnectionInterceptor = NetworkConnectionInterceptor()
        api = APIClient(requireContext(), networkConnectionInterceptor)
        requestLoanRepository = RequestLoanRepository(api,requireContext())
        viewModelFactory = RequestLoanViewModelFactory(requestLoanRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[RequestLoanViewModel::class.java]
        viewModel.listener = this
    }
    private fun onSubmit(){
        binding?.btnSubmit?.setOnClickListener {
            val loanInfo = getLoanInformation()
            if (!checkInvalidInput()) {
                return@setOnClickListener
            }
            Log.d("RequestLoanFragment", "Loan Information: $loanInfo")
            viewModel.requestLoan(loanInfo)
        }
    }
    private fun checkInvalidInput(): Boolean {
        if (amount <= 0 || amount >= 1000000) {
            binding?.editTextLoanAmount?.error = "Please enter a valid loan amount."
            return false
        }
        if (duration <= 0 || duration >= 12) {
            binding?.editTextDuration?.error = "Please enter the loan amount."
            return false
        }
        return true
    }
    private fun getLoanInformation(): RequestLoanModel{
        employeeType = arguments?.getString(IncomeInformationFragment.EMPLOYEE_TYPEE) ?: ""
        position = arguments?.getString(IncomeInformationFragment.POSITION) ?: ""
        income  = arguments?.getDouble(IncomeInformationFragment.INCOME) ?: 0.0
        //get bank statement from arguments as a Uri
        if( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            bankStatement = arguments?.getParcelable(IncomeInformationFragment.BANK_STATEMENT, Uri::class.java)
        } else {
            @Suppress("DEPRECATION") //suppress deprecation warning for older Android versions
            bankStatement = arguments?.getParcelable(IncomeInformationFragment.BANK_STATEMENT)
        }
        duration = binding?.editTextDuration?.text.toString().toIntOrNull() ?: 0
        amount = binding?.editTextLoanAmount?.text.toString().toDoubleOrNull() ?: 0.0
        loanType = checkedLoanType()
        Log.d("RequestLoanFragment", "Employee Type: $employeeType, Position: $position, Income: $income, Bank Statement: $bankStatement, Loan Type: $loanType, Amount: $amount, Duration: $duration")
        return RequestLoanModel(
            loanType = loanType,
            loanAmount = amount,
            loanDuration = duration,
            employeeType = employeeType,
            position = position,
            income = income,
            bankStatement = bankStatement
        )
    }
    private fun checkedLoanType(): String {
        when (binding?.radioGroupIncomeType?.checkedRadioButtonId) {
            binding?.rbPersonalLoan?.id -> loanType = "personal loan"
            binding?.rbBusinessLoan?.id -> loanType = "business loan"
            else -> loanType = "personal loan" // Default to personal loan if none selected
        }
        return loanType
    }
    override fun success(message: ResponseErrorModel) {
        replaceFragment(HomeFragment(), R.id.fragment)
        Toast.makeText(requireContext(), "Loan request submitted successfully.", Toast.LENGTH_SHORT).show()
    }

    override fun fail(message: String) {
            showDialog("Error", "Failed to submit loan request: $message")
    }
}