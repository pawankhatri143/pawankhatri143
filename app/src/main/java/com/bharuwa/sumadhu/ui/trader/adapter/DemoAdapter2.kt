package com.bharuwa.sumadhu.ui.trader.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.R
import kotlinx.android.synthetic.main.item_bees_present.view.*

class DemoAdapter2 : RecyclerView.Adapter<DemoAdapter2.ViewHolder>() {

    private var list = listOf<String>()

    fun setData(flora: List<String>) {
        this.list = flora

        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_bees_present, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            txtName.text = list[position]
        }
    }

    override fun getItemCount() = list.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}