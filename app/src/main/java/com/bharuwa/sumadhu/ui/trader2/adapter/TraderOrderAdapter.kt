package com.bharuwa.sumadhu.ui.trader2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.constants.Util.formatTo
import com.bharuwa.sumadhu.constants.Util.toDate
import com.bharuwa.sumadhu.databinding.MyorderAdapterItemsBinding
import com.bharuwa.sumadhu.databinding.TraderOrdersLayoutItemBinding
import com.bharuwa.sumadhu.ui.trader2.model.ResultItem12
import javax.inject.Inject

class TraderOrderAdapter @Inject constructor(): RecyclerView.Adapter<TraderOrderAdapter.ViewHolder>()  {
    private var itemClick: ((ResultItem12) -> Unit)? = null
    private var selectedPosition = -1
    fun setItemClick(action: (ResultItem12) -> Unit) {
        this.itemClick = action
    }

    private var list = listOf<ResultItem12>()

    fun setData(result: List<ResultItem12?>?) {
        this.list = result as List<ResultItem12>
        notifyDataSetChanged()
    }

    class ViewHolder(val binding : TraderOrdersLayoutItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding= TraderOrdersLayoutItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            txtWeigth.text= "${list[position].actualHoneyNetWeight.toString()} Kg"
            txtWeigth.text= "${list[position].remainHoneyWeight.toString()} Kg"
            txtName.text= list[position].farmerId
            txtMobile.text= list[position].extraRemark1
            if(list[position].extraRemarkarr6.isNullOrEmpty()){
                if (!list[position].flora.isNullOrEmpty()) txtFolra.text=  list[position].flora
                else txtFolra.text= "N/A"
            } else txtFolra.text=  list[position].extraRemarkarr6?.joinToString()

             txtOrderID.text= "Order Id: ${list[position].orderNo}"
            checkbox.isChecked = selectedPosition == position
            checkbox.setOnClickListener {
                if ( selectedPosition == position) checkbox.isChecked=true
                if (checkbox.isChecked){
                    itemClick?.invoke(list[position])
                    val copyOfLastCheckedPosition = selectedPosition
                    selectedPosition = position
                    notifyItemChanged(copyOfLastCheckedPosition);
                    notifyItemChanged(selectedPosition)
                }
            }
        }
    }

    override fun getItemCount()= list.size
}