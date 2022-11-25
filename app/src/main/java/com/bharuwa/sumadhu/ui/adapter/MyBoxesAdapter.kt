package com.bharuwa.sumadhu.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.databinding.MyboxesListItemsBinding
import com.bharuwa.sumadhu.network.model.BoxesModel
import javax.inject.Inject

class MyBoxesAdapter @Inject constructor(): RecyclerView.Adapter<MyBoxesAdapter.MyViewHolder>(), Filterable {
    private var myBoxesList = listOf<BoxesModel>()
    private var searchedMyBoxesList = listOf<BoxesModel>()
    private var itemClick: ((BoxesModel) -> Unit)? = null
    private var listVisibility: ((List<BoxesModel>) -> Unit)? = null
    fun setAdapterData(list: List<BoxesModel>) {
        this.myBoxesList = list
        this.searchedMyBoxesList = list
        notifyDataSetChanged()
    }

    fun setItemClick(action: (BoxesModel) -> Unit) {
        this.itemClick = action
    }
    fun onVisibilityCheck(action: (List<BoxesModel>) -> Unit) {
        this.listVisibility = action
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding=
            MyboxesListItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val barcodeData = searchedMyBoxesList[position]
        holder.binding.tvBoxNo.text="${position+1}"
        holder.binding.tvBarcode.text="${barcodeData.barcode}"
        holder.binding.itemLayout.setOnClickListener {
//            itemClick?.invoke(searchedMyBoxesList[position])
        }
    }

    override fun getItemCount(): Int {
        return myBoxesList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class MyViewHolder(val binding : MyboxesListItemsBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                searchedMyBoxesList = if (charString.isEmpty()) {
                    myBoxesList
                } else {
                    Log.e("searchVillage", charString)
                    val filteredList: MutableList<BoxesModel> = ArrayList()
                    myBoxesList.forEach { villageList ->
                        /*if (villageList.bookingNumber!!.lowercase().startsWith(charString.lowercase())) {
                            filteredList.add(villageList)
                        }*/
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = searchedMyBoxesList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                searchedMyBoxesList = filterResults.values as List<BoxesModel>
                listVisibility?.invoke(searchedMyBoxesList)
                notifyDataSetChanged()
            }
        }
    }
}