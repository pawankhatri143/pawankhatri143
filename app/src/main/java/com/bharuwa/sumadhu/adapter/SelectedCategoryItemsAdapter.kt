package com.bharuwa.sumadhu.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.databinding.SelectedCategoryItemLayoutBinding
import com.bharuwa.sumadhu.model.CategoryModel
import com.bumptech.glide.Glide

data class ChoosCatModel(
   val cat_code : String,
   val sub_cat_code : String
)
class SelectedCategoryItemsAdapter : RecyclerView.Adapter<SelectedCategoryItemsAdapter.ViewHolder>(),
    Filterable {
    private var items = listOf<CategoryModel>()
    private var searchItemsList = listOf<CategoryModel>()
    var selectedItems = listOf<PreferredCategoryModel>().toMutableList()
    var chooseItems = listOf<ChoosCatModel>().toMutableList()
    var onItemChanged: ((CategoryModel)-> Unit)? = null

    fun setData(list: List<CategoryModel>) {
        items = list
        searchItemsList = list
        notifyDataSetChanged()
    }

    fun getSetectedItemsList() : MutableList<ChoosCatModel> = chooseItems

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SelectedCategoryItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.checkbox.text = searchItemsList[position].name
        Glide.with(holder.itemView.context)
            .load(searchItemsList[position].subCategoryImage)
            .into(holder.binding.imageViewBrand)

        onItemChanged?.invoke(searchItemsList[position])

        holder.binding.checkbox.isChecked = selectedItems.find { it.masterCode == searchItemsList[position].masterCode }?.categoryList?.any { it.name == searchItemsList[position].name } == true

        holder.binding.layoutSubCategory.setOnClickListener {
            val item = selectedItems.find { it.masterCode ==  searchItemsList[position].masterCode}
            if (!selectedItems.contains(item)){
                val subCategoryModel = PreferredSubCategoryModel(
                    searchItemsList[position].masterCode,
                    searchItemsList[position].name
                )
                if (item == null){
                    var categoryModel=PreferredCategoryModel(searchItemsList[position].masterCode,searchItemsList[position].name,
                        listOf(subCategoryModel))
                    selectedItems.add(categoryModel)
                    chooseItems.add(ChoosCatModel(searchItemsList[position].cmasterCode.toString(), searchItemsList[position].masterCode.toString()))
                }else{
                    item.categoryList?.toMutableList()?.add(subCategoryModel)
                    chooseItems.add(ChoosCatModel(searchItemsList[position].cmasterCode.toString(), searchItemsList[position].masterCode.toString()))
                }
            }else{
                selectedItems.remove(item)
                chooseItems.remove(ChoosCatModel(searchItemsList[position].cmasterCode.toString(), searchItemsList[position].masterCode.toString()))
            }


          //  onItemClick?.invoke(selectedItems)
            notifyDataSetChanged()


        }

    }

    override fun getItemCount(): Int {
        return searchItemsList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(val binding: SelectedCategoryItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                searchItemsList = if (charString.isEmpty()) {
                    items
                } else {
                    Log.e("searchVillage", charString)
                    val filteredList: MutableList<CategoryModel> = ArrayList()
                    for (sampleData in items) {
                        if (sampleData.name?.toLowerCase()!!.startsWith(charString.toLowerCase())
                        ) {
                            filteredList.add(sampleData)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = searchItemsList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                searchItemsList = filterResults.values as List<CategoryModel>
                notifyDataSetChanged()
            }
        }
    }


}