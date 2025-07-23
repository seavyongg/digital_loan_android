package com.app.afinal.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.app.afinal.databinding.ListLoanBinding
import com.app.afinal.model.LoanModel
import com.app.afinal.ui.viewHolder.LoanViewHolder
import com.app.afinal.utils.OnItemClick

class LoanAdapter(
    private val allData : List<LoanModel.Data.LoanData>
): ListAdapter<LoanModel.Data.LoanData, LoanViewHolder>(LoanDiffCallBack())
{
    var onItemClickListener: ((position: Int) -> Unit)? = null
    init{
        submitList(allData)
    }
    private lateinit var context : Context
    private lateinit var itemClick: OnItemClick<LoanModel.Data.LoanData>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListLoanBinding.inflate(layoutInflater, parent, false)
        context = parent.context
        return LoanViewHolder(binding){
            item : LoanModel.Data.LoanData ->
            val position = allData.indexOf(item)
            onItemClickListener?.invoke(position) // Invoke the listener when item is clicked
        }
    }

    override fun onBindViewHolder(holder: LoanViewHolder, position: Int) {
       holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(position) // Invoke the listener when item is clicked
            Log.d("LoanAdapter", "Item: ${getItem(position).loanId} clicked at position $position")
        }
    }
}

class LoanDiffCallBack: DiffUtil.ItemCallback<LoanModel.Data.LoanData>(){
    override fun areItemsTheSame(
        oldItem: LoanModel.Data.LoanData,
        newItem: LoanModel.Data.LoanData
    ): Boolean {
        return oldItem.loanId == newItem.loanId
    }

    override fun areContentsTheSame(
        oldItem: LoanModel.Data.LoanData,
        newItem: LoanModel.Data.LoanData
    ): Boolean {
        return oldItem == newItem
    }

}