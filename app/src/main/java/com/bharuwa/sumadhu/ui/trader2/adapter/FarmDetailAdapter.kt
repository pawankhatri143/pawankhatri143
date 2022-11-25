package com.bharuwa.sumadhu.ui.trader2.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.databinding.FarmItemBinding
import com.bharuwa.sumadhu.databinding.ItemAllDrumBinding
import com.bharuwa.sumadhu.ui.trader.adapter.AllDrumsAdapter
import com.bharuwa.sumadhu.ui.trader.adapter.DemoAdapter
import com.bharuwa.sumadhu.ui.trader.viewModel.DrumItem
import com.bharuwa.sumadhu.ui.trader2.model.Result2Item
import javax.inject.Inject

class FarmDetailAdapter  @Inject constructor(): RecyclerView.Adapter<FarmDetailAdapter.ViewHolder>() {

    private var selectedPosition = -1
    private var list = mutableListOf<Result2Item>()
    private var itemClick: ((Result2Item) -> Unit)? = null

    fun setItemClick(action: (Result2Item) -> Unit) {
        this.itemClick = action
    }

    fun setData(result: List<Result2Item>) {
        this.list.clear()
        this.list.addAll(result)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding=
            FarmItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        //holder.itemView.textTitle.text = "${position+1}"
        holder.apply {
            binding.txtfieldName.text= list[position].name
            binding.txtAddressName.text= "${list[position].address.toString()}, ${list[position].city.toString()}, (${list[position].district.toString()}) "
            binding.txtFlora.text= list[position].flora?.joinToString()
            binding.txtBeeName.text= list[position].beeName
            binding.appCompatCheckBox2.isChecked = selectedPosition == position

            binding.appCompatCheckBox2.setOnClickListener {
                if ( binding.appCompatCheckBox2.isChecked){
                    list[position].clickedOnLocation= false
                    itemClick?.invoke(list[position])

                    val copyOfLastCheckedPosition = selectedPosition
                    selectedPosition = position
                    notifyItemChanged(copyOfLastCheckedPosition);
                    notifyItemChanged(selectedPosition)
//                notifyDataSetChanged()
                }

            }

            binding.imgOpenMap.setOnClickListener {
                list[position].clickedOnLocation= false
                itemClick?.invoke(list[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val binding : FarmItemBinding) : RecyclerView.ViewHolder(binding.root)

}