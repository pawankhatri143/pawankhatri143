package com.bharuwa.sumadhu.ui.vendor.adapter

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.constants.Util.inflate
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.home_pager_item_layout.view.*

class BannerPagerAdapter(val data: List<Int>) : PagerAdapter() {

    override fun getCount(): Int {
        return data.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val v = container.inflate(R.layout.home_pager_item_layout)
        val item = data[position]
        v.image.scaleType = ImageView.ScaleType.FIT_XY
        Glide.with(container.context)
            .load(item)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(v.image)
      /*  v.setOnClickListener {
            v.context.startActivity(
                Intent(v.context, MarketAdsActivity::class.java)
                    .putExtra("primaryCategoryCode", item.primaryCategoryCode)
                    .putExtra("subCategoryCode", item.subCategoryCode))
        }*/
        container.addView(v)
        return v
    }

}