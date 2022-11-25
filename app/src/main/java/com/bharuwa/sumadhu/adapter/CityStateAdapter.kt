package com.bharuwa.sumadhu.adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.constants.Util.capitalizeWords
import com.bharuwa.sumadhu.constants.Util.inflate
import com.bharuwa.sumadhu.model.CityStateVillageModel
import kotlinx.android.synthetic.main.city_state_list_item.view.*

class CityStateAdapter : RecyclerView.Adapter<CityStateAdapter.MyViewHolder>(), Filterable {
    private var allList = listOf<CityStateVillageModel>()
    private var searchedList = listOf<CityStateVillageModel>()
    private var itemClick: ((CityStateVillageModel) -> Unit)? = null
    private var listVisibility: ((List<CityStateVillageModel>) -> Unit)? = null

    fun setVillageData(list: List<CityStateVillageModel>) {
        this.allList = list
        this.searchedList = list
        notifyDataSetChanged()
    }

    fun setItemClick(action: (CityStateVillageModel) -> Unit) {
        this.itemClick = action
    }
    fun onVisibilityCheck(action: (List<CityStateVillageModel>) -> Unit) {
        this.listVisibility = action
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = parent.inflate(R.layout.city_state_list_item)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val villageVillageCode = searchedList[position]
        holder.itemView.tvName.text = "${villageVillageCode.name?.capitalizeWords()}"
        holder.itemView.tvName.setOnClickListener {
            itemClick?.invoke(searchedList[position])
        }
    }

    override fun getItemCount(): Int {
        return searchedList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                searchedList = if (charString.isEmpty()) {
                    allList
                } else {
                    Log.e("searchVillage", charString)
                    val filteredList: MutableList<CityStateVillageModel> = ArrayList()
                    allList.forEach { villageList ->
                        if (villageList.name!!.lowercase().startsWith(charString.lowercase())) {
                            filteredList.add(villageList)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = searchedList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                searchedList = filterResults.values as List<CityStateVillageModel>
                listVisibility?.invoke(searchedList)
                notifyDataSetChanged()
            }
        }
    }

}