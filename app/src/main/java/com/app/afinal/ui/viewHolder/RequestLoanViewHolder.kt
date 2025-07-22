package com.app.afinal.ui.viewHolder

import android.icu.text.Transliterator.Position
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.app.afinal.databinding.FragmentLoanRequestBinding
import com.app.afinal.databinding.ListRequestLoanBinding
import com.app.afinal.model.RequestLoanResponse
import com.app.afinal.utils.OnItemClick
import com.app.afinal.utils.dateFormat

class RequestLoanViewHolder(
    private val binding: ListRequestLoanBinding,
    private val onItemClick:(RequestLoanResponse.Data.DataDetails) -> Unit
): RecyclerView.ViewHolder(binding.root) {
    fun  bind(
        item : RequestLoanResponse.Data.DataDetails
    ){
        binding.apply {
            textViewLoanId.text = item.requestLoanId.toString()
            textViewLoanDate.text = dateFormat(item.createdAt!!)
            textViewLoanAmount.text = item.loanAmount
            textViewLoanStatus.text = item.status
            if (item.status.equals("rejected", true)) {
                textViewLoanStatus.setTextColor(binding.root.context.getColor(com.app.afinal.R.color.error))
            } else {
                textViewLoanStatus.setTextColor(binding.root.context.getColor(com.app.afinal.R.color.primary))
            }
        }
        binding.root.setOnClickListener{
            onItemClick(item)
        }
    }

}