package com.bharuwa.sumadhu.ui.vendor.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bharuwa.sumadhu.ui.vendor.fragment.AllDispatchFragment
import com.bharuwa.sumadhu.ui.vendor.fragment.PendingDispatchFragment

private const val NUM_TABS = 2
class ViewPagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            1 -> return AllDispatchFragment()
            else -> return PendingDispatchFragment()
        }

    }
}