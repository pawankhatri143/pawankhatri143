package com.bharuwa.sumadhu.ui.trader2.adapter

import android.R
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.databinding.ManualOrderLayoutBinding
import com.bharuwa.sumadhu.ui.trader2.model.ContainerDetails
import javax.inject.Inject


class ManualOrderAdapter @Inject constructor(): RecyclerView.Adapter<ManualOrderAdapter.ViewHolder>() {

    var list = listOf<ContainerDetails>().toMutableList()
    private var itemClick: (() -> Unit)? = null
    private var fromStoreHoney= false

    fun setItemClick(action: () -> Unit) {
        this.itemClick = action
    }
    var totalHonyCount= -0f

    fun setData(container: List<ContainerDetails>,fromStoreHoney: Boolean = false) {
        list.clear()
        this.fromStoreHoney= fromStoreHoney
        list.addAll(container)
        notifyDataSetChanged()
    }
    class ViewHolder (val binding : ManualOrderLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding= ManualOrderLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            if (fromStoreHoney){
                txtEqual.visibility= View.INVISIBLE
                txtWeigth.visibility= View.INVISIBLE
            }
            txtContainerType.text = list[position].containerType
            txtContainerCapacity.text = list[position].containerCapacity.toString()

            edtContainerCount.afterTextChanged {
                if (it.isNotEmpty()){
                    val mul = it.toInt() *  list[position].containerCapacity!!.toInt()
                    txtWeigth.setText(mul.toString())
                    list[position].numberOfBucket= it
                }else {
                    list[position].manualWeigth = "0"
                    list[position].numberOfBucket = "0"
                    txtWeigth.setText("")
                }
                itemClick?.invoke()
            }

            txtWeigth.afterTextChanged {
                Log.e("txtWeigth", "onBindViewHolder=: "+it )
                if (it.isNotEmpty()) {
                    list[position].manualWeigth = it
                }

            }




        }
    }

    private fun EditText.afterTextChanged(cb: ((String) -> Unit)?){
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                cb?.invoke(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

        })
    }

    override fun getItemCount()= list.size
}