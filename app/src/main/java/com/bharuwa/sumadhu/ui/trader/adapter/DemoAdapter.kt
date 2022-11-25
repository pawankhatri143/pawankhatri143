package com.bharuwa.sumadhu.ui.trader.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.R


class DemoAdapter : RecyclerView.Adapter<DemoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_network, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.itemView.textTitle.text = "${position+1}"
    }

    override fun getItemCount(): Int {
        return 20
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}