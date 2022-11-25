package com.bharuwa.sumadhu.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.ui.dashboard.view.DashboardActivity.Companion.beeAndFlora
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Util.formatTo
import com.bharuwa.sumadhu.constants.Util.name
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.constants.Util.toDate
import com.bharuwa.sumadhu.databinding.DashboardListItemBinding
import com.bharuwa.sumadhu.network.model.DashboardModel
import javax.inject.Inject

class HomePageAdapter @Inject constructor() : RecyclerView.Adapter<HomePageAdapter.MyViewHolder>(), Filterable {
    private var dashboardList = listOf<DashboardModel>()
    private var searchedDashboardList = listOf<DashboardModel>()
    private var itemClick: ((DashboardModel,String) -> Unit)? = null
    private var listVisibility: ((List<DashboardModel>) -> Unit)? = null
    fun setAdapterData(list: List<DashboardModel>) {
        this.dashboardList = list
        this.searchedDashboardList = list
        notifyDataSetChanged()
    }
    fun setItemClick(action: (DashboardModel,String) -> Unit) {
        this.itemClick = action
    }
    fun onVisibilityCheck(action: (List<DashboardModel>) -> Unit) {
        this.listVisibility = action
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding=
            DashboardListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val listObject = searchedDashboardList[position]
        listObject.farm?.let {
            holder.binding.tvFarmName.text="${it.name}"
            holder.binding.tvDate.text=it.createdAt?.toDate()?.formatTo()
            holder.binding.tvLocation.text="${it.address} ${it.city} ${it.state} -${it.pincode}"

            val lang = MyApp.get().getLanguage()
            val bees = beeAndFlora?.bee
            //val beeName = bees?.find { b -> b.name == it.beeName || b.name_hi == it.beeName}
            val beeName = bees?.find { b -> b.beeNameEn == it.beeName || b.beeNameHi == it.beeName}
            val floraList = it.flora
            val flora = beeAndFlora?.flora?.filter { f -> f.nameEn in floraList!! || f.nameHi in floraList }?.map { f -> if (lang == "Hindi") f.nameHi else f.nameEn }


            //holder.binding.tvBee.text = "${if (lang == "Hindi") beeName?.name_hi else beeName?.name}"
            holder.binding.tvBee.text = "${if (lang == "Hindi") beeName?.beeNameHi else beeName?.beeNameEn}"
            holder.binding.tvFlora.text = "${flora?.joinToString()}"
        }
        holder.binding.tvBoxCount.text="${listObject.boxCount}"
        holder.binding.tvFillBoxCount.text="${listObject.totalBoxCount}"
        holder.binding.tvEmptyBoxCount.text="${listObject.emptyBoxCount}"

        holder.binding.layoutItem.setOnClickListener {
            itemClick?.invoke(listObject,"view")
        }
        holder.binding.layoutAddMore.setOnClickListener {
            itemClick?.invoke(listObject,"addMore")
        }
        holder.binding.layoutRelocateBox.setOnClickListener {
            listObject.emptyBoxCount?.let {
                if(it>0) itemClick?.invoke(listObject,"relocate") else holder.itemView.context.showToast("Boxes not available for migrate")
            }?: run {
                holder.itemView.context.showToast("Boxes not available for migrate")
            }

        }
        holder.binding.layoutEmptyBox.setOnClickListener {
            listObject.boxCount?.let {
               if(it>0) itemClick?.invoke(listObject,"empty") else holder.itemView.context.showToast("Boxes not available for empty")
            }?: run {
                holder.itemView.context.showToast("Boxes not available for empty")
            }

        }
        holder.binding.layoutMap.setOnClickListener {
            itemClick?.invoke(listObject,"map")
        }
    }

    override fun getItemCount(): Int {
        return searchedDashboardList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class MyViewHolder(val binding : DashboardListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                searchedDashboardList = if (charString.isEmpty()) {
                    dashboardList
                } else {
                    Log.e("searchVillage", charString)
                    val filteredList: MutableList<DashboardModel> = ArrayList()
                    dashboardList.forEach { villageList ->
                        /*if (villageList.bookingNumber!!.lowercase().startsWith(charString.lowercase())) {
                            filteredList.add(villageList)
                        }*/
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = searchedDashboardList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                searchedDashboardList = filterResults.values as List<DashboardModel>
                listVisibility?.invoke(searchedDashboardList)
                notifyDataSetChanged()
            }
        }
    }

}