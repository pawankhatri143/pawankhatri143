package com.bharuwa.sumadhu.ui.vendor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.R

class QCApprovedAdapter : RecyclerView.Adapter<QCApprovedAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // val v = LayoutInflater.from(parent.context).inflate(R.layout.item_demo, parent, false)
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_approved, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 10
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}