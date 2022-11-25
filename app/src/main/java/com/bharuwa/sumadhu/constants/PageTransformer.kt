package com.bharuwa.sumadhu.constants

import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.home_pager_item_layout.view.*

class PageTransformer: ViewPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width
        page.image.translationX = -position * (pageWidth / 2)
    }
}