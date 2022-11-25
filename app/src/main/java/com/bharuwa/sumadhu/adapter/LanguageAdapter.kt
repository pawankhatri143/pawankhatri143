package com.bharuwa.sumadhu.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Util.inflate
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import kotlinx.android.synthetic.main.language_item_layout.view.*

class LanguageAdapter: RecyclerView.Adapter<LanguageAdapter.ViewHolder>()  {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    private var itemClick: ((Item) -> Unit)? = null
    private var planList = listOf<Item>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageAdapter.ViewHolder {
        return ViewHolder(parent.inflate(R.layout.language_item_layout))
    }
    init {
        planList = listOf(
            Item(MyApp.get().getString(R.string.lan_hindi), R.drawable.a_hindi,R.color.red,"Hindi", "hi"),
            Item(MyApp.get().getString(R.string.lang_english), R.drawable.a_english,R.color.black,"English", "en"),
        )
    }

    fun setItemClick(action: (Item) -> Unit) {
        this.itemClick = action
    }

    override fun getItemCount(): Int {
        return planList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.langName.text = planList[position].name
        holder.itemView.cardView.setCardBackgroundColor(planList[position].color)
        Glide.with(holder.itemView.context).load(planList[position].image).transform(CenterCrop()).into(holder.itemView.imageView)

        holder.itemView.setOnClickListener {
            itemClick?.invoke(planList[position])
        }
    }
}

data class Item(
    val name: String,
    val image: Int,
    val color: Int,
    val code: String,
    val locale: String = ""
)
