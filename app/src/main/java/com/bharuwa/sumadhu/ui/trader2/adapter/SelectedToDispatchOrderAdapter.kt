package com.bharuwa.sumadhu.ui.trader2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.databinding.DispatchedOrderItemsBinding
import com.bharuwa.sumadhu.databinding.SelectToDispatchItemBinding
import com.bharuwa.sumadhu.ui.trader2.AddDispatchActivity
import com.bharuwa.sumadhu.ui.trader2.LoadDispatchActivity.Companion.selectedDispatchedOrderIdList
import com.bharuwa.sumadhu.ui.trader2.LoadDispatchActivity.Companion.vendorID
import com.bharuwa.sumadhu.ui.trader2.LoadDispatchActivity.Companion.vendorName
import com.bharuwa.sumadhu.ui.trader2.model.DispatchedOrder
import javax.inject.Inject

class SelectedToDispatchOrderAdapter@Inject constructor(): RecyclerView.Adapter<SelectedToDispatchOrderAdapter.ViewHolder>() {


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
    class ViewHolder(val binding : SelectToDispatchItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=
            SelectToDispatchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            txtWeigth.text= "${list[position].dispatchNetWeight.toString()} Kg"
            txtFlora.text= if (list[position].flora.isNullOrBlank()) "Mango" else list[position].flora
            txtVName.text= list[position].traderName
            txtDispatchID.text= "Dispatch ID:  ${list[position].disptchId.toString()}"

            val date= list[position].dispatchDate?.split("T")
            txtDate.text= date?.get(0)?.toString() ?: "09-06-2022"

            checkbox.setOnClickListener {
                if (checkbox.isChecked){
                    if (!selectedDispatchedOrderIdList.contains(list[position].traderDispatchId.toString())){
                        selectedDispatchedOrderIdList.add(list[position].traderDispatchId.toString())

                        vendorID= list[position].vendorId.toString()
                        vendorName= list[position].vendorName.toString()
                    }
                }else{
                    if (selectedDispatchedOrderIdList.contains(list[position].traderDispatchId.toString())){
                        selectedDispatchedOrderIdList.remove(list[position].traderDispatchId.toString())
                    }

                }

              //  itemClick?.invoke(list[position])
            }
        }
    }

    override fun getItemCount()= list.size
}