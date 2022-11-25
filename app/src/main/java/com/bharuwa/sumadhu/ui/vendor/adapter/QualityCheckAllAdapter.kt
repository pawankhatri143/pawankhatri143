package com.bharuwa.sumadhu.ui.vendor.adapter

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.constants.Util.formatTo
import com.bharuwa.sumadhu.constants.Util.inflate
import com.bharuwa.sumadhu.constants.Util.toDate

import com.bharuwa.sumadhu.ui.vendor.model.QualityCheckAllResult
import kotlinx.android.synthetic.main.custom_dialog_box.*
import kotlinx.android.synthetic.main.item_quality_all_check.view.*

import javax.inject.Inject

class QualityCheckAllAdapter @Inject constructor() : RecyclerView.Adapter<QualityCheckAllAdapter.MyViewHolder>() {
    private var listOfQualityCheck = listOf<QualityCheckAllResult>()
    private var listOfQualityCheckFiltered = listOf<QualityCheckAllResult>()
    var onActionInvoked: ((QualityCheckAllResult, String, Int) -> Unit)? = null

    fun setData(list: List<QualityCheckAllResult>) {
        this.listOfQualityCheck = list
        this.listOfQualityCheckFiltered = list

        notifyDataSetChanged()
    }

    fun updateData(adpPosition: Int) {
        listOfQualityCheckFiltered[adpPosition].reportUpload = true
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = parent.inflate(R.layout.item_quality_all_check)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val inWardList = listOfQualityCheckFiltered[position]

        holder.itemView.txtUploadReport.visibility= if (inWardList.qualityTestStatus=="PENDING") View.VISIBLE else View.GONE
        holder.itemView.linearLayoutButton.visibility = if (inWardList.qualityTestStatus=="PENDING") View.VISIBLE else View.GONE

      //  Log.e("djfkdsfkd", "onBindViewHolder: "+inWardList.reportUpload )

        if (inWardList.qualityTestStatus=="PENDING" && inWardList.reportUpload){
            holder.itemView.txtUploadReport.text= "Report Uploaded"
            holder.itemView.txtUploadReport.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.colorPrimary))
          //  holder.itemView.txtUploadReport.backgroundTintList= ContextCompat.getColorStateList(holder.itemView.context, R.color.colorPrimary)
        }else{
            holder.itemView.txtUploadReport.text= "Upload Report"
            holder.itemView.txtUploadReport.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
          //  holder.itemView.txtUploadReport.backgroundTintList= ContextCompat.getColorStateList(holder.itemView.context, R.color.red)

        }

        holder.itemView.textViewInwardId.text = inWardList.inwardCode
        holder.itemView.textViewWeight.text = inWardList.netWeight.toString()
        holder.itemView.textViewDate.text = inWardList.inwardDate.toDate("yyyy-MM-dd'T'HH:mm:ss").formatTo("dd-MM-yyyy")
        holder.itemView.textViewName.text = inWardList.traderName
        holder.itemView.textViewFlora.text = inWardList.floraName


        if (inWardList.reportUpload)
        holder.itemView.linearLayoutReject.setOnClickListener {
            onActionInvoked?.invoke(inWardList, "Reject",position)
        }


        //for approve
        holder.itemView.linearLayoutApprove.setOnClickListener {
            val customDialog = Dialog(holder.itemView.context)
            customDialog.setContentView(R.layout.custom_dialog_box)
            customDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            customDialog.setCancelable(false)

            customDialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
            customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            customDialog.textView8.text = "Are you sure you want to approve the \n                selected material?"

            customDialog.buttonCancel.setOnClickListener {
                customDialog.dismiss()
            }
            customDialog.buttonYes.setOnClickListener {
                onActionInvoked?.invoke(inWardList, "Approve",position)
                customDialog.dismiss()
            }
            customDialog.show()
        }

        holder.itemView.setOnClickListener {
            onActionInvoked?.invoke(inWardList, "Details",position)
        }

        holder.itemView.txtUploadReport.setOnClickListener {
            onActionInvoked?.invoke(inWardList, "uploadReport",position)
        }
    }

    override fun getItemCount(): Int {
        return listOfQualityCheckFiltered.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {}

}