package com.bharuwa.sumadhu.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.databinding.GroupLocationViewBinding
import com.bharuwa.sumadhu.network.model.BoxesModel
import com.bharuwa.sumadhu.network.model.LocationsModel
import com.bharuwa.sumadhu.ui.dashboard.view.DashboardActivity
import javax.inject.Inject

class GroupLocationAdapter @Inject constructor() : RecyclerView.Adapter<GroupLocationAdapter.MyViewHolder>() {
    private var myBoxesList = listOf<GroupLocationModel>().toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = GroupLocationViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    fun bindWith(activity: DashboardActivity){
        activity.farmList.forEach {
            it.boxes?.forEach { box ->
                myBoxesList.add(GroupLocationModel(it.farm, box))
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = myBoxesList[position]
        holder.binding.groupHeader.text = "${holder.binding.groupHeader.context.getString(R.string.farm)} ${item.farm?.name}"
        holder.binding.txtBarcode.text = "${item.model.barcode}"
        if (position == 0){
            holder.binding.groupHeader.visibility = View.VISIBLE
        }else{
            val preForm = myBoxesList[position - 1].farm?._id
            holder.binding.groupHeader.visibility = if (preForm == item.farm?._id) View.GONE else View.VISIBLE
        }
    }

    override fun getItemCount() = myBoxesList.size
    override fun getItemId(position: Int) = position.toLong()
    override fun getItemViewType(position: Int) = position

    class MyViewHolder(val binding : GroupLocationViewBinding) : RecyclerView.ViewHolder(binding.root)
    data class GroupLocationModel(val farm: LocationsModel?, val model: BoxesModel)
}