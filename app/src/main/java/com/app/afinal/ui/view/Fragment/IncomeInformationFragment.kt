package com.app.afinal.ui.view.Fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.app.afinal.R
import com.app.afinal.databinding.FragmentIncomeInformationBinding
import com.app.afinal.ui.view.BaseFragment

class IncomeInformationFragment: BaseFragment<FragmentIncomeInformationBinding>() {
    private var employeeType :  String = ""
    private var position : String = ""
    private var occupation : String = ""
    private var companyName : String = ""
    private var bankStatement : Uri? = null
    private var income : Double = 0.0
    private val pickPdfLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result -> if( result.resultCode == Activity.RESULT_OK){
        result.data?.data?.let {
            uri ->
            bankStatement = uri
            binding?.btnChoose?.text = "Bank Statement Selected(PDF)"
        }
    }else{
        bankStatement = null
        binding?.btnChoose?.text = "Choose Bank Statement(PDF)"
    }
    }
    override fun provideBinding(): FragmentIncomeInformationBinding {
        val binding = FragmentIncomeInformationBinding.inflate(layoutInflater)
        return binding
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBack()
        onPickFile()
        ShowIncomeInformation()
        onSubmit()
    }
    private fun onBack(){
        binding?.btnBack?.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
    fun onPickFile(){
        binding?.btnChoose?.setOnClickListener {
            openPdfPicker()
        }
    }
    private fun onSubmit(){
        binding?.btnContinue?.setOnClickListener {
            Log.d("IncomeInformationFragment", "Show Income Information ${ShowIncomeInformation()}")
            if(!checkInvalidInput(employeeType))return@setOnClickListener
            submit(employeeType, position, income, bankStatement)
        }
    }
    fun openPdfPicker(){
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply{
            type = "application/pdf" // Set the MIME type to PDF
            addCategory(Intent.CATEGORY_OPENABLE) // Ensure the intent can open a file
        }
        if(intent.resolveActivity(requireActivity().packageManager) != null) {
            pickPdfLauncher.launch(intent)
        }else{
            Log.e("IncomeInformationFragment", "No application available to handle PDF selection")
            showDialog(
                "Error",
                "No application available to select PDF files.",
                "OK"
            )
        }
    }
    fun checkInvalidInput(employeeType: String):Boolean{
        if(employeeType == "employer") {
            if (occupation.isEmpty()) {
                binding?.editTextOccupation?.error = "Occupation is required for employed individuals"
                return false
            } else if (bankStatement == null) {
                binding?.btnChoose?.error = "Bank statement is required for employed individuals"
                return false
            } else if( companyName.isEmpty()) {
                binding?.editTextCompanyName?.error =
                    "Company name is required for employed individuals"
                return false
            }
        }
        if(position.isEmpty()){
            binding?.editTextPosition?.error = "Position is required"
            return false
        }
        else if (income <= 0) {
            binding?.editTextIncome?.error = "Income must be greater than zero"
            return false
        }
        return true
    }
    //show income information based on the selected radio button
    fun ShowIncomeInformation(){
        employeeType = checkEmployeeType()
        position = binding?.editTextPosition?.text.toString()
        income = binding?.editTextIncome?.text.toString().toDoubleOrNull() ?: 0.0
        occupation = binding?.editTextOccupation?.text.toString()
        companyName = binding?.editTextCompanyName?.text.toString()

    }
    private fun checkEmployeeType(): String {
        binding?.radioGroupIncomeType?.setOnCheckedChangeListener{ _, checkedId ->
         when (checkedId) {
                binding?.radioButtonEmployed?.id -> {
                    binding?.llOccupationCompany?.visibility = View.VISIBLE
                    binding?.llBankStatement?.visibility = View.VISIBLE
                    employeeType = "employer"
                }
                binding?.radioButtonSelfEmployed?.id -> {
                    binding?.llOccupationCompany?.visibility = View.GONE
                    binding?.llBankStatement?.visibility = View.GONE
                    employeeType = "self-employer"
                }
             else -> employeeType = "employer" // Default to employer if none selected
            }
            }
        return employeeType
    }
    //submit data
    fun submit(employeeType: String,
               position: String,
               income: Double, bankStatement: Uri? = null) {
        try{
            //bundle the data and pass it to the next fragment
            val bundle = Bundle().apply {
                putString(EMPLOYEE_TYPEE, employeeType)
                putString(POSITION, position)
                putDouble(INCOME, income)
                putParcelable(BANK_STATEMENT, bankStatement)
            }
            // replace the current fragment with RequestLoanFragment
            val requestLoanFragment = RequestLoanInformationFragment()
            requestLoanFragment.arguments = bundle
            replaceFragment(requestLoanFragment, R.id.fragment)
            Log.d("IncomeInformationFragment", "Submitting data: ${requestLoanFragment.arguments}")
        }catch (e: Exception) {
            Log.e("IncomeInformationFragment", "Error submitting data: ${e.message}")
            showDialog(
                "Error",
                "An error occurred while submitting your data. Please try again.",
                "OK"
            )
        }
    }
    companion object{
        val EMPLOYEE_TYPEE = "employeeType"
        val POSITION = "position"
        val INCOME = "income"
        val BANK_STATEMENT = "bankStatement"
    }
}