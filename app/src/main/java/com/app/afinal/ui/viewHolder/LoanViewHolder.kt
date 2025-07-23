package com.app.afinal.ui.viewHolder

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.app.afinal.databinding.ListLoanBinding
import com.app.afinal.model.LoanModel
import com.app.afinal.utils.dateFormat

class LoanViewHolder(
    private val binding: ListLoanBinding,
    private val onItemClick: (LoanModel.Data.LoanData) -> Unit
): RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item : LoanModel.Data.LoanData
    ){
        binding.apply {
            tvLoanId.text = item.loanId.toString()
            tvLoanDate.text = dateFormat(item.date)
            tvLoanAmount.text = item.requestLoan.loanAmount
            tvLoanStatus.text = item.status
            Log.d("LoanViewHolder", "Loan Status: ${item.status}")
            if(item.status.equals("0", true)){
                tvLoanStatus.text = "Unpaid"
            }else{
                tvLoanStatus.text = "Paid"
            }
        }
        binding.root.setOnClickListener{
            onItemClick(item)
        }
    }
}