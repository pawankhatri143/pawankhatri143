package com.bharuwa.sumadhu.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.constants.Util.inflate
import com.bharuwa.sumadhu.model.LocationsModel
import com.bharuwa.sumadhu.ui.farm.FarmListActivity.Companion.locationId
import kotlinx.android.synthetic.main.deliver_address_item_layout.view.*


class LocationListAdapter : RecyclerView.Adapter<LocationListAdapter.MyViewHolder>() {
    private var items = listOf<LocationsModel>().toMutableList()
    private var itemClick: ((LocationsModel) -> Unit)? = null
    private var itemDeleteClick: ((LocationsModel) -> Unit)? = null
    private var itemEditeClick: ((LocationsModel) -> Unit)? = null
    var lastCheckedPosition : Int = -1
    private var isFirstTime = true

    fun setData(list: List<LocationsModel>) {
        this.items=list.toMutableList()
        isFirstTime = true
        lastCheckedPosition = -1
        notifyDataSetChanged()
    }

    fun clear()
    {
        items.clear()
        isFirstTime = true
        lastCheckedPosition = -1
        notifyDataSetChanged()
    }

    fun setItemClick(action: (LocationsModel) -> Unit) {
        this.itemClick = action
    }
    fun setOnDeleteClickListner(action: (LocationsModel) -> Unit) {
        this.itemDeleteClick = action
    }
    fun setOnEditClickListner(action: (LocationsModel) -> Unit) {
        this.itemEditeClick = action
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = parent.inflate(R.layout.deliver_address_item_layout)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if(items[position].default == true){
            holder.itemView.tvDefault.visibility = View.VISIBLE
            locationId = items[position]._id
        }
        else holder.itemView.tvDefault.visibility = View.GONE

        if(items[position].default == true && isFirstTime) lastCheckedPosition = position
        holder.itemView.radioButton.isChecked = position == lastCheckedPosition

        holder.apply {
            itemView.tvName.text = items[position].name
            val finalAddress = "${items[position].address}, ${items[position].city}, ${items[position].district}, \n${items[position].state} - ${items[position].pincode}"
            itemView.tvAddress.text = finalAddress
            itemView.tvPhoneNumber.text = items[position].mobileNumber
        }
        holder.itemView.layoutAddressItem.setOnClickListener {
            holder.itemView.radioButton.isChecked=true
            lastCheckedPosition=position
            itemClick?.invoke(items[position])
            isFirstTime=false
            notifyDataSetChanged()
        }
        holder.itemView.cardViewDelete.setOnClickListener {
            itemDeleteClick?.invoke(items[position])
        }
        holder.itemView.cardViewEdit.setOnClickListener {
            itemEditeClick?.invoke(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

}