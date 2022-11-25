package com.bharuwa.sumadhu.ui.adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.constants.Util.inflate
import com.bharuwa.sumadhu.network.model.BoxesModel
import com.bharuwa.sumadhu.ui.farm.FarmRelocationActivity.Companion.checkboxAllSelected
import com.bharuwa.sumadhu.ui.farm.FarmRelocationActivity.Companion.textViewTotalCount
import kotlinx.android.synthetic.main.item_multiple_box_selction.view.*
import java.util.*
import kotlin.collections.ArrayList

class MultipleBoxSelectionAdapter : RecyclerView.Adapter<MultipleBoxSelectionAdapter.MyViewHolder>() {
    private var boxesList = listOf<BoxesModel>()
    private var searchedBoxesList = listOf<BoxesModel>().toMutableList()
    var selectedItems = listOf<BoxesModel>().toMutableList()
    private var tempBoxesList = listOf<BoxesModel>().toMutableList()
    private var itemClick: ((BoxesModel) -> Unit)? = null
    private var listVisibility: ((List<BoxesModel>) -> Unit)? = null

    fun setAdapterData(list: List<BoxesModel>) {
        this.boxesList = list
        this.searchedBoxesList = list as MutableList<BoxesModel>
        this.tempBoxesList.addAll(list)
        notifyDataSetChanged()
    }

    fun selectAll(){
        selectedItems.clear()
        selectedItems.addAll(searchedBoxesList)
        notifyDataSetChanged()
    }

    fun clearAll(){
        selectedItems.clear()
        notifyDataSetChanged()
    }

    fun setItemClick(action: (BoxesModel) -> Unit) {
        this.itemClick = action
    }
    fun onVisibilityCheck(action: (List<BoxesModel>) -> Unit) {
        this.listVisibility = action
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = parent.inflate(R.layout.item_multiple_box_selction)
        return MyViewHolder(view)
    }

    private fun BoxesModel.isExist(): Boolean{
        return selectedItems.find { it.barcode == barcode } != null
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val boxData = searchedBoxesList[position]
        holder.itemView.tvBarcode.text = "${boxData.barcode}"
        holder.itemView.appCompatCheckBox.isChecked= boxData.isExist()
        holder.itemView.constraintLayout.setOnClickListener {
            holder.itemView.appCompatCheckBox.isChecked= !holder.itemView.appCompatCheckBox.isChecked
            val item = selectedItems.find { it.barcode ==  searchedBoxesList[position].barcode}
            if (!selectedItems.contains(item)){
                selectedItems.add(searchedBoxesList[position])
            }else{
                selectedItems.remove(item)
            }
            textViewTotalCount?.text ="${textViewTotalCount!!.context.getString(R.string.total_scaned)}  ${selectedItems.size}"
            checkboxAllSelected?.let { checkbox->
                checkbox.isChecked=false
            }
        }
    }

    override fun getItemCount(): Int {
        return searchedBoxesList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) { }

    fun setFilter(charText: String) {
        var charText = charText
        charText = charText.toLowerCase(Locale.getDefault())
        //   this.items.addAll(list)
        this.searchedBoxesList.clear()
        if (charText.length == 0) {
            searchedBoxesList.addAll(tempBoxesList)
        } else {
            for (agent in tempBoxesList) {
                if (agent.barcode?.toLowerCase(Locale.getDefault())!!.contains(charText)) {
                    searchedBoxesList.add(agent)
                }
            }
        }
        notifyDataSetChanged()
    }

}