package com.bharuwa.sumadhu.adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Util.capitalizeWords
import com.bharuwa.sumadhu.constants.Util.inflate
import com.bharuwa.sumadhu.constants.Util.name
import com.bharuwa.sumadhu.model.Flora
import com.bharuwa.sumadhu.ui.farm.FloraSelectionActivity.Companion.selectedItems
import kotlinx.android.synthetic.main.item_flora_multiple_selction.view.*

class FloraSelectionAdapter : RecyclerView.Adapter<FloraSelectionAdapter.MyViewHolder>(), Filterable {
    private var floraList = listOf<Flora>()
    private var searchedFloraList = listOf<Flora>()
    private var listVisibility: ((List<Flora>) -> Unit)? = null
    fun setAdapterData(list: List<Flora>/*,selected: List<Flora>*/) {
        this.floraList = list
        this.searchedFloraList = list
        notifyDataSetChanged()
    }
    fun onVisibilityCheck(action: (List<Flora>) -> Unit) {
        this.listVisibility = action
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = parent.inflate(R.layout.item_flora_multiple_selction)
        return MyViewHolder(view)
    }

    private fun Flora.isExist(): Boolean{
        return selectedItems.find { it.code == code } != null
    }

    private fun getCatName(pos: Int): String{
        return if(MyApp.get().getLanguage()=="Hindi") searchedFloraList[pos].catHi.toString() else searchedFloraList[pos].category.toString()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val flora = searchedFloraList[position]
        holder.itemView.txtName.text =  if(MyApp.get().getLanguage()=="Hindi") flora.nameHi else flora.nameEn
        holder.itemView.appCompatCheckBox.isChecked= flora.isExist()
        holder.itemView.tvFloraCat.text =  getCatName(position)
        if (position == 0) {
            holder.itemView.tvFloraCat.visibility = View.VISIBLE
        }else{
            val preCat = getCatName(position-1)
            val curCat = getCatName(position)
            holder.itemView.tvFloraCat.visibility = if (preCat == curCat) View.GONE else View.VISIBLE
        }

        holder.itemView.constraintLayout.setOnClickListener {
                holder.itemView.appCompatCheckBox.isChecked =
                    !holder.itemView.appCompatCheckBox.isChecked
                val item = selectedItems.find { it.code == flora.code }
                if (!selectedItems.contains(item)) {
                 //   selectedItems.add(flora)
                } else {
                    selectedItems.remove(item)
                }
        }
    }

    override fun getItemCount(): Int {
        return searchedFloraList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {}

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                searchedFloraList = if (charString.isEmpty()) {
                    floraList
                } else {
                    Log.e("searchVillage", charString)
                    val filteredList: MutableList<Flora> = ArrayList()
                    for (flora in floraList) {
                     /*   if (flora.name().lowercase()!!.startsWith(charString.lowercase())) {
                            filteredList.add(flora)
                        }*/
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = searchedFloraList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                searchedFloraList = filterResults.values as List<Flora>
                listVisibility?.invoke(searchedFloraList)
                notifyDataSetChanged()
            }
        }
    }

}