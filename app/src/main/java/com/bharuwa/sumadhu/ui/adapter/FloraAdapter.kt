package com.bharuwa.sumadhu.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.constants.Util.name
import com.bharuwa.sumadhu.databinding.FloraItemLayoutBinding
import com.bharuwa.sumadhu.network.model.Flora
import javax.inject.Inject

class FloraAdapter @Inject constructor(): RecyclerView.Adapter<FloraAdapter.ViewHolder>()  {
    inner class ViewHolder(val binding: FloraItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)
    private var itemClick: ((FloraAdapter) -> Unit)? = null
    private var floraList = listOf<Flora>()
    fun setData(list: List<Flora>) {
        floraList = list
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FloraAdapter.ViewHolder {
        val binding = FloraItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    fun setItemClick(action: (FloraAdapter) -> Unit) {
        this.itemClick = action
    }

    override fun getItemCount(): Int {
        return floraList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flora = floraList[position]
        holder.binding.categoryName.text = flora.name()
    }
}
/*

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.databinding.FloraItemLayoutBinding
import com.bharuwa.sumadhu.network.model.CategoryModel
import com.bharuwa.sumadhu.network.model.Flora

class FloraAdapter: RecyclerView.Adapter<FloraAdapter.ViewHolder>()  {
    inner class ViewHolder(val binding: FloraItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)
//    private var categories: List<String>?= null
    private var itemClick: ((FloraAdapter) -> Unit)? = null
    private var selectedFlora = mutableListOf<String>()
    private var selectedFloraCodes = mutableListOf<String>()

    private var floraList = listOf<Flora>()
    private var searchItemsList = listOf<CategoryModel>()

   // private var itemSelected : ((selectedFlora) -> Unit)? = null
   */
/* init {
        if (formSkill)  categories =   listOf("1", "2", "3", "4", "5", "6", "78", "87")
        else categories =   listOf("Clothing", "Bags", "Festivals", "Paintings", "Stationery", "Artifacts", "Tribal Jewellery", "Home & Living", "Cane & Bamboo", "Gifts & Novelties", "Organic & Natural Products", "Longpi Pottery", "Tribal Textiles", "Tribal Metal Craft", "Terracotta & Stone Pottery")

    }*//*

    fun setData(list: List<Flora>){
       floraList = list
       notifyDataSetChanged()
    }
    fun getAllSelectedItems() : MutableList<String> = selectedFlora
    fun getAllSelectedItemsCode() : MutableList<String> = selectedFloraCodes
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FloraAdapter.ViewHolder {
        val binding = FloraItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    fun setItemClick(action: (FloraAdapter) -> Unit) {
        this.itemClick = action
    }

    override fun getItemCount(): Int {
        return floraList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flora = floraList[position]
        holder.binding.categoryName.text = if(MyApp.get().getLanguage()=="Hindi") flora.name_hi else flora.name

        holder.binding.langContainer.setBackgroundResource(if(selectedFlora.contains(flora.name)) R.drawable.selected_bg else R.drawable.app_edit_bg)
        holder.binding.categoryName.setTextColor(if(selectedFlora.contains(flora.name)) ContextCompat.getColor(holder.binding.categoryName.context, R.color.white) else ContextCompat.getColor(holder.binding.categoryName.context, R.color.black))
        holder.itemView.setOnClickListener {
            val c = selectedFlora.contains(flora.name.toString())
            if (c){
                selectedFlora.remove(flora.name.toString())
                selectedFloraCodes.remove(flora.code.toString())
            }else{
                selectedFlora.add(flora.name!!.toString())
                selectedFloraCodes.add(flora.code!!.toString())
            }
            itemClick?.invoke(this)
        }
    }
}

*/
