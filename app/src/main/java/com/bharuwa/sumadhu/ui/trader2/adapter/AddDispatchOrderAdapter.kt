package com.bharuwa.sumadhu.ui.trader2.adapter

import android.app.Activity
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.databinding.DispatchAdapterItemsBinding
import com.bharuwa.sumadhu.databinding.FarmItemBinding
import com.bharuwa.sumadhu.ui.trader2.AddDispatchActivity
import com.bharuwa.sumadhu.ui.trader2.AddDispatchActivity.Companion.totalHoneyWeigth
import com.bharuwa.sumadhu.ui.trader2.AddDispatchActivity.Companion.totalNetWeigth
import com.bharuwa.sumadhu.ui.trader2.AddDispatchActivity.Companion.traderOrderIdList
import com.bharuwa.sumadhu.ui.trader2.model.Result2Item
import com.bharuwa.sumadhu.ui.trader2.model.ResultItem12
import com.bharuwa.sumadhu.ui.trader2.model.TraderOrderItem
import com.bharuwa.sumadhu.ui.vendor.dialog.CustomizeWeigth
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.android.synthetic.main.item_blending_inward.view.*
import javax.inject.Inject

class AddDispatchOrderAdapter @Inject constructor(): RecyclerView.Adapter<AddDispatchOrderAdapter.ViewHolder>() {

    private var list = mutableListOf<ResultItem12>()
    private var itemClick: ((ResultItem12) -> Unit)? = null
    private var context: Activity?= null

    fun setItemClick(action: (ResultItem12) -> Unit) {
        this.itemClick = action
    }

    fun setData(result: List<ResultItem12>, addDispatchActivity: AddDispatchActivity) {
        this.list.clear()
        this.list.addAll(result)
        context=addDispatchActivity
        notifyDataSetChanged()
    }
    class ViewHolder(val binding : DispatchAdapterItemsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding=
            DispatchAdapterItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            txtWeigth.text= "${list[position].remainHoneyWeightForDispatch.toString()} kg"
            if(list[position].extraRemarkarr6.isNullOrEmpty()){
                if (!list[position].flora.isNullOrEmpty()) txtFlora.text=  list[position].flora
                else txtFlora.text= "N/A"
            } else txtFlora.text= list[position].extraRemarkarr6?.joinToString()
            txtBeeKeeper.text= list[position].farmerId
            txtAddress.text= if (list[position].extraRemark3.isNullOrBlank()) " " else list[position].extraRemark3
            txtOrderID.text= "Order Id: ${list[position].orderNo}"
            val date= list[position].orderDate?.split("T")
            txtDate.text= date?.get(0)?.toString() ?: "09-06-2022"

            list[position].tempHoneyWeigth =  list[position].remainHoneyWeightForDispatch
            list[position].remainHoneyWeigth = list[position].remainHoneyWeightForDispatch

            checkbox.setOnClickListener {
                if (checkbox.isChecked){

                    if (traderOrderIdList?.any{it.dispatchOrderId == list[position].orderId.toString()} == false){
                        list[position].remainHoneyWeightForDispatch?.let { totalNetWeigth += it.toFloat() }
                        list[position].remainHoneyWeightForDispatch?.let{ totalHoneyWeigth += it.toFloat() }
                        traderOrderIdList?.add(TraderOrderItem( list[position].orderId.toString(), "Full",list[position].remainHoneyWeightForDispatch.toString()))
                        list[position].alreadyAddedHoney= list[position].remainHoneyWeightForDispatch
                    }
                }else{
                    if (traderOrderIdList?.any{it.dispatchOrderId == list[position].orderId.toString()} == true){
                        list[position].alreadyAddedHoney?.let { if (totalNetWeigth > 0)totalNetWeigth -= it.toFloat() }
                        list[position].alreadyAddedHoney?.let{ if (totalHoneyWeigth > 0) totalHoneyWeigth -= it.toFloat() }
                        traderOrderIdList?.removeIf { it.dispatchOrderId == (list[position].orderId.toString()) }
                    }
                    txtWeigth.text= "${list[position].remainHoneyWeightForDispatch.toString()} kg"
                    list[position].alreadyAddedHoney= 0f
                    list[position].tempHoneyWeigth =   list[position].remainHoneyWeightForDispatch
                    list[position].remainHoneyWeigth =   list[position].remainHoneyWeightForDispatch

                }

                itemClick?.invoke(list[position])
            }


            imgIconEditWeigth.setOnClickListener {
                showDialog( list[position].remainHoneyWeightForDispatch.toString(),
                    checkbox,
                    position,
                    txtWeigth)
            }
        }


    }

    override fun getItemCount()= list.size

    private fun showDialog(
        weigth: String,
        checkBoxSelect: CheckBox,
        position: Int,
        textViewNetWeight: TextView
    ) {
        val dialog = CustomizeWeigth(context!!,weigth)
        dialog.setDialogDismissListener{



            list[position].remainHoneyWeigth = weigth.toFloat().minus(it)
            list[position].tempHoneyWeigth = it
            val text =
                "<font color=#cc0029>${list[position].remainHoneyWeigth}</font> <font color=#0bab64>(-${list[position].tempHoneyWeigth})</font>"
            textViewNetWeight.setText(Html.fromHtml(text))
            checkBoxSelect.isChecked = true

            if (checkBoxSelect.isChecked){
                if (traderOrderIdList?.any{ it.dispatchOrderId == list[position].orderId.toString() } == false){
                    list[position].tempHoneyWeigth?.let { totalNetWeigth += it.toFloat() }
                    list[position].tempHoneyWeigth?.let{ totalHoneyWeigth += it.toFloat() }

                    list[position].alreadyAddedHoney= list[position].tempHoneyWeigth
                    traderOrderIdList?.add(TraderOrderItem( list[position].orderId.toString(),
                        "Partial",list[position].tempHoneyWeigth.toString()))

                }else {
                    if (traderOrderIdList?.any{it.dispatchOrderId == list[position].orderId.toString()} == true){
                        list[position].alreadyAddedHoney?.let { if (totalNetWeigth > 0)totalNetWeigth -= it.toFloat() }
                        list[position].alreadyAddedHoney?.let{ if (totalHoneyWeigth > 0) totalHoneyWeigth -= it.toFloat() }
                        traderOrderIdList?.removeIf { it.dispatchOrderId == list[position].orderId.toString() }
                    }

                    if (traderOrderIdList?.any{it.dispatchOrderId == list[position].orderId.toString()} == false){
                        list[position].tempHoneyWeigth?.let { totalNetWeigth += it.toFloat() }
                        list[position].tempHoneyWeigth?.let{ totalHoneyWeigth += it.toFloat() }
                        traderOrderIdList?.add(TraderOrderItem( list[position].orderId.toString(),
                            "Partial",list[position].tempHoneyWeigth.toString()))
                        list[position].alreadyAddedHoney= list[position].tempHoneyWeigth
                }

                    list[position].tempHoneyWeigth =  weigth.toFloat()
                    list[position].remainHoneyWeigth =   weigth.toFloat()
            }

            itemClick?.invoke(list[position])

        }


    }
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

}