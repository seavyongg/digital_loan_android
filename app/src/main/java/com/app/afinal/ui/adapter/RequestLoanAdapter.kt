package com.app.afinal.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.app.afinal.databinding.ListRequestLoanBinding
import com.app.afinal.model.RequestLoanResponse
import com.app.afinal.ui.viewHolder.RequestLoanViewHolder
import com.app.afinal.utils.OnItemClick

class RequestLoanAdapter(
    private val allData : List<RequestLoanResponse.Data.DataDetails>
): ListAdapter<RequestLoanResponse.Data.DataDetails, RequestLoanViewHolder>(RequestLoanDiffCallBack()) {

    var onItemClickListener: ((position: Int) -> Unit)? = null

    init{
        submitList(allData) // Submit the list to the adapter
    }

    private lateinit var context: Context
    private lateinit var itemClick: OnItemClick<RequestLoanResponse.Data.DataDetails>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestLoanViewHolder {
       val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListRequestLoanBinding.inflate(layoutInflater, parent, false)
        context = parent.context
        return RequestLoanViewHolder(binding){
            item : RequestLoanResponse.Data.DataDetails -> onItemClickListener?.invoke(allData.indexOf(item)) // Invoke the listener when item is clicked
            Log.d("RequestLoanAdapter", "Item: ${item.requestLoanId} clicked at position ${allData.indexOf(item)}")
        }
    }

    override fun onBindViewHolder(holder: RequestLoanViewHolder, position: Int) {
       holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(position) // Invoke the listener when item is clicked
            Log.d("RequestLoanAdapter", "Item: ${getItem(position).requestLoanId} clicked at position $position")
        }
    }


}
class RequestLoanDiffCallBack: DiffUtil.ItemCallback<RequestLoanResponse.Data.DataDetails>(){
    override fun areContentsTheSame(
        oldItem: RequestLoanResponse.Data.DataDetails,
        newItem: RequestLoanResponse.Data.DataDetails
    ): Boolean {
       return oldItem == newItem
    }

    override fun areItemsTheSame(
        oldItem: RequestLoanResponse.Data.DataDetails,
        newItem: RequestLoanResponse.Data.DataDetails
    ): Boolean {
        return oldItem.requestLoanId == newItem.requestLoanId
    }
}