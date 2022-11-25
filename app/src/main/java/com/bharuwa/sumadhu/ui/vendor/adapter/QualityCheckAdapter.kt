package com.bharuwa.sumadhu.ui.vendor.adapter

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.R
import kotlinx.android.synthetic.main.custom_dialog_box.*
import kotlinx.android.synthetic.main.item_quality_check.view.*

class QualityCheckAdapter : RecyclerView.Adapter<QualityCheckAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // val v = LayoutInflater.from(parent.context).inflate(R.layout.item_demo, parent, false)
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_quality_check, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        /*holder.itemView.linearLayoutReject.setOnClickListener {
            val customDialog = Dialog(holder.itemView.context)
            customDialog.setContentView(R.layout.custom_dialog_box)
            customDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            customDialog.setCancelable(false)
            customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            customDialog.buttonProceed.setOnClickListener {
                customDialog.dismiss()
            }
            customDialog.buttonCancel.setOnClickListener {
                customDialog.dismiss()
            }
            customDialog.show()
        }*/

    }

    override fun getItemCount(): Int {
        return 20
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}