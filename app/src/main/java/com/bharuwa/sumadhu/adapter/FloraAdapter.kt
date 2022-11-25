package com.bharuwa.sumadhu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Util.name
import com.bharuwa.sumadhu.databinding.FloraItemLayoutBinding
import com.bharuwa.sumadhu.model.Flora

class FloraAdapter: RecyclerView.Adapter<FloraAdapter.ViewHolder>()  {
    inner class ViewHolder(val binding: FloraItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)
    private var itemClick: ((FloraAdapter) -> Unit)? = null
    private var floraList = listOf<Flora>()
    fun setData(list: List<Flora>){
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
       // holder.binding.categoryName.text = flora.name()
    }
}

