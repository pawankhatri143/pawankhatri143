package com.bharuwa.sumadhu.ui.trader.adapter

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.databinding.ItemAllDrumBinding
import com.bharuwa.sumadhu.databinding.ItemDrumSelectBinding
import com.bharuwa.sumadhu.ui.trader.fragment.DrumsFragment.Companion.currentDate
import com.bharuwa.sumadhu.ui.trader.fragment.DrumsFragment.Companion.orderList
import com.bharuwa.sumadhu.ui.trader.model.DrumDetailsModel
import com.bharuwa.sumadhu.ui.trader.viewModel.DrumItem
import kotlinx.android.synthetic.main.item_drum.view.*
import javax.inject.Inject

class DemoAdapter3 @Inject constructor(): RecyclerView.Adapter<DemoAdapter3.ViewHolder>() {
    private var itemClick: ((Float) -> Unit)? = null
    fun setItemClick(action: (Float) -> Unit) {
        this.itemClick = action
    }
    private var list = listOf<DrumItem>()
    private var totalCount = 0.0f

    fun setData(result: List<DrumItem>) {
        this.list = result
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        /*val v = LayoutInflater.from(parent.context).inflate(R.layout.item_drum_select, parent, false)
        return ViewHolder(v)*/

        val binding=
            ItemDrumSelectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.binding.txtDrumNumber.text = list[position].drumID
        holder.binding.txtDrumCapacity.text = "${list[position].drumCapacity} Kg"

        holder.itemView.checkbox.setOnClickListener {

            if (holder.itemView.checkbox.isChecked) {
                list[position].drumCapacity?.let {
                    totalCount += it
                    itemClick?.invoke(totalCount)

                    /*
                     "drumID": "625ff15f771c9a824e56032d",
            "traderId": "62541b2c450348129223297a",
            "drumCode": "123",
            "drumCapacity": "123456",
            "drumStatus": "Empty",
            "drumValidity": "30",
            "status": "1",
            "createdAt": "2022-03-15T15:59:22.78+05:30"*/

                    orderList.add(DrumDetailsModel(drumId= list[position].drumID,
                        drumNetWeight= list[position].drumCapacity?.plus(10f),
                        drumHoneyWeight= list[position].drumCapacity,
                        emptyDrumWeight= 10f,
                        drumAmount= 0f,
                        drumFillFarmId= "623067294655a41d25577b4b",
                        drumFillFarmerId= "623067294655a41d25577b4b",
                        honeyPerKgAmount= 0f,
                        drumEmptyDate= currentDate,
                        drumFillDate= currentDate,
                        drumEmptyStatus= "Fill",
                        drumFillStatus= "Empty",
                        traderId= list[position].traderId,
                        drumCode= list[position].drumCode,
                        status= "1"
                    ))
                }

                //orderList.add()
            }else{
                if (totalCount > 0){
                    list[position].drumCapacity?.let {
                        totalCount -= it
                        itemClick?.invoke(totalCount)
                        orderList.removeIf { it.drumId == list[position].drumID }
                    }
//                    orderList.remove()
                }

            }
          //  holder.itemView.context.startActivity(Intent( holder.itemView.context, PurchaseCompleteActivity::class.java))

        }
    }

    override fun getItemCount() = list.size

    fun getTotalDrumCatacityCount() = totalCount

    class ViewHolder(val binding : ItemDrumSelectBinding) : RecyclerView.ViewHolder(binding.root)

}