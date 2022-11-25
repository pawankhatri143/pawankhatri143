package com.bharuwa.sumadhu.ui.vendor.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.constants.Util.formatTo
import com.bharuwa.sumadhu.constants.Util.inflate
import com.bharuwa.sumadhu.constants.Util.toDate
import com.bharuwa.sumadhu.ui.vendor.model.QualityCheckTabDetailResult
import kotlinx.android.synthetic.main.item_tab_detail.view.*
import javax.inject.Inject

class QualityCheckTabDetailAdapter @Inject constructor() : RecyclerView.Adapter<QualityCheckTabDetailAdapter.MyViewHolder>() {
    private var listOfDetail = listOf<QualityCheckTabDetailResult>()
    private var listOfDetailFiltered = listOf<QualityCheckTabDetailResult>()
    private var itemClick: ((QualityCheckTabDetailResult) -> Unit)? = null

    fun setData(list: List<QualityCheckTabDetailResult>) {
        this.listOfDetail= list
        this.listOfDetailFiltered = list
        notifyDataSetChanged()
    }

    fun onItemClick(action: (QualityCheckTabDetailResult) -> Unit) {
        this.itemClick = action
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = parent.inflate(R.layout.item_tab_detail)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val inWardList = listOfDetailFiltered[position]
        holder.itemView.textViewInwardId.text = inWardList.inwardId
        holder.itemView.textViewContainerCapacity.text = inWardList.containerCapacity.toString()
        holder.itemView.textViewContainerHoneyWeight.text = inWardList.containerHoneyWeight.toString()
        holder.itemView.textViewContainerEmptyWeight.text = inWardList.containerEmptyWeight.toString()
        holder.itemView.textViewContainerNetWeight.text = inWardList.containerNetWeight.toString()
        holder.itemView.textViewContainerAmount.text = inWardList.containerAmount.toString()
        holder.itemView.textViewContainerFlora.text = inWardList.containerFlora
        holder.itemView.textViewInwardDate.text = inWardList.inwardDetailDate.toDate("yyyy-MM-dd'T'HH:mm:ss").formatTo("dd-MM-yyyy")
        holder.itemView.textViewInwardStatus.text = inWardList.inwardDetailStatus

        holder.itemView.setOnClickListener {
            itemClick?.let {
                it(listOfDetailFiltered[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return listOfDetailFiltered.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {}

}