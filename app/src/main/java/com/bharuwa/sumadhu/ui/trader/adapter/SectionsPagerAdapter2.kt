package com.bharuwa.sumadhu.ui.trader.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bharuwa.sumadhu.ui.trader.fragment.AllDrumsFragment
import com.bharuwa.sumadhu.ui.trader.fragment.DrumsFragment
import com.bharuwa.sumadhu.ui.trader.fragment.EmptyDrumsFragment
import com.bharuwa.sumadhu.ui.trader.fragment.FullDrumsFragment

private val TAB_TITLES = arrayOf("Drums", "Boxes", "Hives")

class SectionsPagerAdapter2(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when(position) {
            0-> DrumsFragment()
            1-> EmptyDrumsFragment()
            2-> FullDrumsFragment()
            else -> AllDrumsFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return TAB_TITLES[position]
    }

    override fun getCount(): Int {
        return TAB_TITLES.size
    }
}