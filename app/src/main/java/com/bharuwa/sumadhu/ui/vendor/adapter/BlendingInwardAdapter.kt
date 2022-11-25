package com.bharuwa.sumadhu.ui.vendor.adapter

import android.app.Activity
import android.content.Context
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.constants.Util.inflate
import com.bharuwa.sumadhu.ui.vendor.activity.BlendingActivity
import com.bharuwa.sumadhu.ui.vendor.activity.BlendingActivity.Companion.finalBlendedList
import com.bharuwa.sumadhu.ui.vendor.activity.BlendingActivity.Companion.totalHoneyWeigthV
import com.bharuwa.sumadhu.ui.vendor.activity.BlendingActivity.Companion.totalNetWeigthV
import com.bharuwa.sumadhu.ui.vendor.dialog.CustomizeWeigth
import com.bharuwa.sumadhu.ui.vendor.model.BlendingProceedModel
import com.bharuwa.sumadhu.ui.vendor.model.QualityCheckAllResult
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.android.synthetic.main.item_blending_inward.view.*
import javax.inject.Inject


class BlendingInwardAdapter @Inject constructor(@ActivityContext val context: Context) : RecyclerView.Adapter<BlendingInwardAdapter.MyViewHolder>() {
    
    private var listOfBlendingFiltered = listOf<QualityCheckAllResult>()
    private var itemClick: ((QualityCheckAllResult) -> Unit)? = null

    var onQuantityUpdate: ((Float) -> Unit)? = null

    fun setData(list: List<QualityCheckAllResult>, activity: BlendingActivity) {
        this.listOfBlendingFiltered = list
        this.activity= activity
        notifyDataSetChanged()
    }

    fun setOnItemClick(action: (QualityCheckAllResult) -> Unit) {
        this.itemClick = action
    }

    private var activity: BlendingActivity? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = parent.inflate(R.layout.item_blending_inward)
        return MyViewHolder(view)
    }

    private fun updateQty() {
        var sum = 0f
        listOfBlendingFiltered.filter { it.isChecked }.forEach {
            it.tempHoneyWeigth?.let { q ->
                sum += q
            }
        }
        onQuantityUpdate?.invoke(sum)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val blendingInWardList = listOfBlendingFiltered[position]

        holder.itemView.textViewTraderName.text = blendingInWardList.traderName
        holder.itemView.textViewFlora.text = blendingInWardList.floraName
        holder.itemView.textViewQuantity.text = blendingInWardList.traderName.toString()
        blendingInWardList.poNumber.let {
            holder.itemView.textViewPONumber.text = it
        }

       // holder.itemView.textViewAmount.text = blendingInWardList.totalAmount.toString()
        blendingInWardList.tempHoneyWeigth =  blendingInWardList.remainHoneyTotalWeight
        blendingInWardList.remainHoneyWeigth =  blendingInWardList.remainHoneyTotalWeight

        holder.itemView.textViewNetWeight.text = blendingInWardList.remainHoneyWeigth.toString()

        holder.itemView.checkBoxSelect.isChecked = listOfBlendingFiltered[position].isChecked

        holder.itemView.checkBoxSelect.setOnClickListener {
            if (BlendingActivity.desiredWeigth > 0F){


             if ( holder.itemView.checkBoxSelect.isChecked){

                 if (!finalBlendedList.any{it.inwardId == blendingInWardList.inwardId}){
                     blendingInWardList.remainHoneyTotalWeight?.let { totalNetWeigthV += it.toFloat() }
                     blendingInWardList.remainHoneyTotalWeight?.let{ totalHoneyWeigthV += it.toFloat() }
                     finalBlendedList.add(BlendingProceedModel( blendingInWardList.inwardId.toString(),blendingInWardList.remainHoneyTotalWeight.toInt(),"2022-06-24","Full",blendingInWardList.remainHoneyTotalWeight))
                     blendingInWardList.alreadyAddedHoney= blendingInWardList.remainHoneyTotalWeight
                 }

              /*  listOfBlendingFiltered[position].isChecked = true
                holder.itemView.textViewNetWeight.text = blendingInWardList.remainHoneyWeigth.toString()*/
            }else{

                 if (finalBlendedList.any{it.inwardId == blendingInWardList.inwardId}){
                     blendingInWardList.alreadyAddedHoney?.let { if (totalNetWeigthV > 0) totalNetWeigthV -= it.toFloat() }
                     blendingInWardList.alreadyAddedHoney?.let{ if (totalHoneyWeigthV > 0) totalHoneyWeigthV -= it.toFloat() }
                     finalBlendedList.removeIf { it.inwardId == (blendingInWardList.inwardId) }
                 }
                 holder.itemView.textViewNetWeight.text= "${blendingInWardList.remainHoneyTotalWeight.toString()} kg"
                 blendingInWardList.alreadyAddedHoney= 0f
                 blendingInWardList.tempHoneyWeigth =   blendingInWardList.remainHoneyTotalWeight
                 blendingInWardList.remainHoneyWeigth =   blendingInWardList.remainHoneyTotalWeight
                 
               /* blendingInWardList.tempHoneyWeigth =  blendingInWardList.remainHoneyTotalWeight
                blendingInWardList.remainHoneyWeigth =  blendingInWardList.remainHoneyTotalWeight

                holder.itemView.textViewNetWeight.text = blendingInWardList.remainHoneyWeigth.toString()
                listOfBlendingFiltered[position].isChecked = false*/
            }
                itemClick?.invoke(blendingInWardList)
                //updateQty()
            }else{
                holder.itemView.checkBoxSelect.isChecked= false
                Toast.makeText(holder.itemView.context, "First enter desired weight", Toast.LENGTH_SHORT).show()
            }

        }

        holder.itemView.setOnClickListener {
            itemClick?.let {
                it(listOfBlendingFiltered[position])
            }
        }

        holder.itemView.imgEdtWeigth.setOnClickListener {
            if (activity != null){
                if (BlendingActivity.desiredWeigth > 0F) {


                    showDialog(
                        listOfBlendingFiltered[position].remainHoneyTotalWeight.toString(),
                        holder.itemView.checkBoxSelect,
                        position,
                        holder.itemView.textViewNetWeight
                    )
                }
                    else{
                    holder.itemView.checkBoxSelect.isChecked= false
                        Toast.makeText(activity, "First enter desired weight", Toast.LENGTH_SHORT).show()
                    }

            }
        }
    }

  /*  private fun showDialog(
        weigth: String,
        checkBoxSelect: CheckBox,
        position: Int,
        textViewNetWeight: TextView
    ) {
        val dialog = CustomizeWeigth(activity!!,weigth)
        dialog.setDialogDismissListener{
                listOfBlendingFiltered[position].remainHoneyWeigth =
                    listOfBlendingFiltered[position].tempHoneyWeigth?.minus(it)
                listOfBlendingFiltered[position].tempHoneyWeigth = it
            val text =
                "<font color=#cc0029>${listOfBlendingFiltered[position].remainHoneyWeigth}</font> <font color=#0bab64>(-${listOfBlendingFiltered[position].tempHoneyWeigth})</font>"
            textViewNetWeight.setText(Html.fromHtml(text))
             //   textViewNetWeight.text = "${listOfBlendingFiltered[position].remainHoneyWeigth} (-${listOfBlendingFiltered[position].tempHoneyWeigth})"
                checkBoxSelect.isChecked = true
                listOfBlendingFiltered[position].isChecked = true

            updateQty()
        }
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

    }*/


    private fun showDialog(
        weigth: String,
        checkBoxSelect: CheckBox,
        position: Int,
        textViewNetWeight: TextView
    ) {
        val dialog = CustomizeWeigth(context as Activity,weigth)
        dialog.setDialogDismissListener{

            listOfBlendingFiltered[position].remainHoneyWeigth = weigth.toFloat().minus(it)
            listOfBlendingFiltered[position].tempHoneyWeigth = it
            val text =
                "<font color=#cc0029>${listOfBlendingFiltered[position].remainHoneyWeigth}</font> <font color=#0bab64>(-${listOfBlendingFiltered[position].tempHoneyWeigth})</font>"
            textViewNetWeight.setText(Html.fromHtml(text))
            checkBoxSelect.isChecked = true

            if (checkBoxSelect.isChecked){
                if (!finalBlendedList.any{it.inwardId == listOfBlendingFiltered[position].inwardId}){
                    listOfBlendingFiltered[position].tempHoneyWeigth?.let { totalNetWeigthV += it.toFloat() }
                    listOfBlendingFiltered[position].tempHoneyWeigth?.let{ totalHoneyWeigthV += it.toFloat() }

                    listOfBlendingFiltered[position].alreadyAddedHoney= listOfBlendingFiltered[position].tempHoneyWeigth
                    finalBlendedList.add(BlendingProceedModel( listOfBlendingFiltered[position].inwardId.toString(),listOfBlendingFiltered[position].remainHoneyTotalWeight.toInt(),"2022-06-24","PARTIAL",listOfBlendingFiltered[position].tempHoneyWeigth))

                }else {
                    if (finalBlendedList.any{it.inwardId == listOfBlendingFiltered[position].inwardId}){
                        listOfBlendingFiltered[position].alreadyAddedHoney?.let { if (totalNetWeigthV > 0) totalNetWeigthV -= it.toFloat() }
                        listOfBlendingFiltered[position].alreadyAddedHoney?.let{ if (totalHoneyWeigthV > 0) totalHoneyWeigthV -= it.toFloat() }
                        finalBlendedList.removeIf { it.inwardId == (listOfBlendingFiltered[position].inwardId) }
                    }

                    if (!finalBlendedList.any{it.inwardId == listOfBlendingFiltered[position].inwardId}){
                        listOfBlendingFiltered[position].tempHoneyWeigth?.let { totalNetWeigthV += it.toFloat() }
                        listOfBlendingFiltered[position].tempHoneyWeigth?.let{ totalHoneyWeigthV += it.toFloat() }
                        finalBlendedList.add(BlendingProceedModel( listOfBlendingFiltered[position].inwardId,listOfBlendingFiltered[position].remainHoneyTotalWeight.toInt(),"2022-06-24","PARTIAL",listOfBlendingFiltered[position].tempHoneyWeigth))

                        listOfBlendingFiltered[position].alreadyAddedHoney= listOfBlendingFiltered[position].tempHoneyWeigth
                    }

                    listOfBlendingFiltered[position].tempHoneyWeigth =  weigth.toFloat()
                    listOfBlendingFiltered[position].remainHoneyWeigth =   weigth.toFloat()
                }



            }

            itemClick?.invoke(listOfBlendingFiltered[position])
        }
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    override fun getItemCount(): Int {
        return listOfBlendingFiltered.size
    }

    override fun getItemId(position: Int): Long { return position.toLong() }

    override fun getItemViewType(position: Int): Int { return position }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {}

}