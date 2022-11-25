package com.bharuwa.sumadhu.ui.vendor.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.constants.Util.inflate
import com.bharuwa.sumadhu.ui.vendor.model.BlendedItem
import kotlinx.android.synthetic.main.item_already_blended.view.*
import javax.inject.Inject

class VendorDispatchAdapter@Inject constructor() : RecyclerView.Adapter<VendorDispatchAdapter.MyViewHolder>() {
    var listOfBlending = listOf<BlendedItem>()
    private var itemClick: ((BlendedItem) -> Unit)? = null
    private var selectedPosition = -1
    private var showCheckBox= false

    fun setData(list: List<BlendedItem>?, showCheckBox: Boolean = false) {
        selectedPosition = -1
        this.showCheckBox = showCheckBox
        list?.also { this.listOfBlending = it }
        notifyDataSetChanged()
    }

    fun onItemClick(action: (BlendedItem) -> Unit) {
        this.itemClick = action
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = parent.inflate(R.layout.item_already_blended)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val blendingInWardList = listOfBlending[position]

        if (showCheckBox)  holder.itemView.checkBox.visibility= View.VISIBLE

    //    holder.itemView.imgEdtWeigth.visibility= View.GONE
        holder.itemView.txtBatchID.text = "Batch Id: ${blendingInWardList.batchId}"
        holder.itemView.txtFlora.text = blendingInWardList.flora
        holder.itemView.txtNetWeight.text = blendingInWardList.netWeight.toString()
        holder.itemView.txtAddress.text = blendingInWardList.manufacturerName.toString()
        holder.itemView.checkBox.isChecked = selectedPosition == position
        holder.itemView.checkBox.setOnClickListener {

            if (it.checkBox.isChecked){
                this.selectedPosition = position
                itemClick?.invoke(blendingInWardList)
            }
            notifyDataSetChanged()
        }

    }

    override fun getItemCount()= listOfBlending.size

    override fun getItemId(position: Int): Long { return position.toLong() }

    override fun getItemViewType(position: Int): Int { return position }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

}