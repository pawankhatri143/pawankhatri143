package com.bharuwa.sumadhu.ui.trader.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.databinding.DashboardListItemBinding
import com.bharuwa.sumadhu.databinding.ItemAllDrumBinding
import com.bharuwa.sumadhu.network.model.DashboardModel
import com.bharuwa.sumadhu.ui.adapter.HomePageAdapter
import com.bharuwa.sumadhu.ui.trader.model.ResultItem
import com.bharuwa.sumadhu.ui.trader.viewModel.DrumItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


class AllDrumsAdapter @Inject constructor() : RecyclerView.Adapter<AllDrumsAdapter.ViewHolder>() {
    private var list = mutableListOf<DrumItem>()
    private var itemClick: ((String) -> Unit)? = null

    fun setItemClick(action: (String) -> Unit) {
        this.itemClick = action
    }

    fun setData(result: MutableList<DrumItem>) {
        this.list.clear()
        this.list = result
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding=
            ItemAllDrumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.txtDrumNumber.text = list[position].drumID
        holder.binding.txtDrumStatus.text = list[position].drumStatus
    }

    override fun getItemCount() = list.size

    class ViewHolder(val binding : ItemAllDrumBinding) : RecyclerView.ViewHolder(binding.root)

}