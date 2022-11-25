package com.bharuwa.sumadhu.ui.vendor.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.constants.Util.inflate
import com.bharuwa.sumadhu.ui.vendor.model.InwardStockResult
import kotlinx.android.synthetic.main.item_inward_list.view.*
import javax.inject.Inject

class InwardStockAdapter @Inject constructor() : RecyclerView.Adapter<InwardStockAdapter.MyViewHolder>() {
    private var listOfInward = listOf<InwardStockResult>()
    private var listOfInwardFiltered = listOf<InwardStockResult>()
    private var itemClick: ((InwardStockResult) -> Unit)? = null

    fun setInwardData(list: List<InwardStockResult>) {
        this.listOfInward = list
        this.listOfInwardFiltered = list
        notifyDataSetChanged()
    }

    fun onItemClick(action: (InwardStockResult) -> Unit) {
        this.itemClick = action
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = parent.inflate(R.layout.item_inward_list)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val inWardList = listOfInwardFiltered[position]
        holder.itemView.textViewDispatchId.text = if (!inWardList.disptchId.isNullOrEmpty()) inWardList.disptchId else inWardList.orderNo
        holder.itemView.textViewTraderName.text = if (!inWardList.traderName.isNullOrEmpty()) inWardList.traderName else inWardList.farmerId

        holder.itemView.textViewVendorWarehouse.text = if (!inWardList.vendorWarehouse.isNullOrEmpty()) inWardList.vendorWarehouse else "Farm"
        holder.itemView.textViewFlora.text = inWardList.flora
        if (inWardList.invoiceNumber == null) holder.itemView.ll_ponumber.visibility= View.GONE
        holder.itemView.textViewPONumber.text = inWardList.invoiceNumber.toString()
        holder.itemView.txtWeight.text = if (inWardList.dispatchNetWeight !=null) "${inWardList.dispatchNetWeight} Kg" else "${inWardList.actualHoneyNetWeight} Kg"
//        holder.itemView.textViewDriverNumber.text = inWardList.driverNumber.toString()

        holder.itemView.textViewDriverNumber.text = if (!inWardList.driverNumber.isNullOrEmpty()) inWardList.driverNumber else inWardList.extraRemark1

       if (inWardList.driverName.isNullOrEmpty()){
           holder.itemView.ll_ponumber.visibility= View.GONE
           holder.itemView.llDriverName.visibility= View.GONE
           holder.itemView.llTransportDetail.visibility= View.GONE

       }
       else{
           holder.itemView.textViewDriverName.text = inWardList.driverName
           holder.itemView.textViewDriverNumber.text = inWardList.driverNumber.toString()
           holder.itemView.textViewVehicalNumber.text = inWardList.vehicalNumber.toString()
       }


        holder.itemView.setOnClickListener {
            itemClick?.let {
                it(listOfInwardFiltered[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return listOfInwardFiltered.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {}

}