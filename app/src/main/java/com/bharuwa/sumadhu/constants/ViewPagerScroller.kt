package com.bharuwa.sumadhu.constants

import android.content.Context
import android.widget.Scroller

class ViewPagerScroller(context: Context): Scroller(context) {
    private val mScrollDuration = 2500
    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration)
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration)
    }
}