package com.bharuwa.sumadhu.ui.trader2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.constants.Util.formatTo
import com.bharuwa.sumadhu.constants.Util.toDate
import com.bharuwa.sumadhu.databinding.MyorderAdapterItemsBinding
import com.bharuwa.sumadhu.ui.trader2.model.ResultItem12
import javax.inject.Inject

class MyOrderAdapter @Inject constructor(): RecyclerView.Adapter<MyOrderAdapter.ViewHolder>()  {
    private var itemClick: ((Float) -> Unit)? = null
    fun setItemClick(action: (Float) -> Unit) {
        this.itemClick = action
    }

    private var list = listOf<ResultItem12>()

    fun setData(result: List<ResultItem12?>?) {
        this.list = result as List<ResultItem12>
        notifyDataSetChanged()
    }

    class ViewHolder(val binding : MyorderAdapterItemsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding= MyorderAdapterItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.binding.apply {
           txtWeigth.text= "${list[position].actualHoneyNetWeight.toString()} Kg"
           txtName.text= list[position].farmerId
           txtMobile.text= list[position].extraRemark1

           if(list[position].extraRemarkarr6.isNullOrEmpty()){

               if (!list[position].flora.isNullOrEmpty()) txtFolra.text=  list[position].flora
               else txtFolra.text= "N/A"

           } else txtFolra.text= list[position].extraRemarkarr6?.joinToString()


        //   txtFolra.text=  if(list[position].extraRemarkarr6.isNullOrEmpty()) "N/A" else list[position].extraRemarkarr6?.joinToString()
           txtOrderID.text= "Order Id: ${list[position].orderNo}"
//           val date= list[position].orderDate?.split("T")
//           txtDate.text= date?.get(0)?.toString() ?: "25-04-2022"
           txtDate.text= if(!list[position].orderDate.isNullOrEmpty()) list[position].orderDate?.toDate("yyyy-MM-dd")?.formatTo("dd/MM/yyyy") else ""
       }
    }



    override fun getItemCount()= list.size
}