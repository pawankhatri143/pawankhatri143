package com.bharuwa.sumadhu.ui.trader2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat.setBackgroundTintList
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.constants.Constants.*
import com.bharuwa.sumadhu.databinding.DispatchAdapterItemsBinding
import com.bharuwa.sumadhu.databinding.DispatchedOrderItemsBinding
import com.bharuwa.sumadhu.ui.trader2.model.DispatchedOrder
import javax.inject.Inject

class MyDispatchOrderAdapter @Inject constructor(): RecyclerView.Adapter<MyDispatchOrderAdapter.ViewHolder>() {

    private var list = mutableListOf<DispatchedOrder>()
    private var itemClick: ((DispatchedOrder) -> Unit)? = null

    fun setItemClick(action: (DispatchedOrder) -> Unit) {
        this.itemClick = action
    }

    fun setData(result: List<DispatchedOrder>) {
        this.list.clear()
        this.list.addAll(result)
        notifyDataSetChanged()
    }
    class ViewHolder(val binding : DispatchedOrderItemsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=
            DispatchedOrderItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            txtWeigth.text= "${list[position].dispatchNetWeight.toString()} Kg"
            txtFlora.text= if (list[position].flora.isNullOrBlank()) "Mango" else list[position].flora
            txtVName.text= list[position].vendorName
            txtStatus.text= list[position].dispatchStatus
            when(list[position].dispatchStatus){
                INWARDED -> txtStatus.backgroundTintList= ContextCompat.getColorStateList(txtStatus.context, R.color.colorPrimary)
                PENDING ->    txtStatus.backgroundTintList= ContextCompat.getColorStateList(txtStatus.context, R.color.yellow_1000)
                DISPATCHED -> {
                    txtStatus.text="IN TRANSIT"
                    txtStatus.backgroundTintList =ContextCompat.getColorStateList(txtStatus.context, R.color.dashboard_color)
                }
                else ->  txtStatus.backgroundTintList= ContextCompat.getColorStateList(txtStatus.context, R.color.red)
            }

            txtDispatchID.text= "Dispatch ID:  ${list[position].disptchId.toString()}"

            val date= list[position].dispatchDate?.split("T")
            txtDate.text= date?.get(0)?.toString() ?: "09-06-2022"
        }
    }

    override fun getItemCount()= list.size
}