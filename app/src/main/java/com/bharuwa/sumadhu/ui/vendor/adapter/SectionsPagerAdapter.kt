package com.bharuwa.sumadhu.ui.vendor.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bharuwa.sumadhu.ui.vendor.fragment.QualityCheckAllFragment
import com.bharuwa.sumadhu.ui.vendor.fragment.QualityCheckApprovedFragment
import com.bharuwa.sumadhu.ui.vendor.fragment.QualityCheckRejectedFragment

private val TAB_TITLES = arrayOf("Pending", "Approved", "Rejected")

class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when(position) {
            0-> QualityCheckAllFragment()
            1-> QualityCheckApprovedFragment()
            2-> QualityCheckRejectedFragment()
            else -> QualityCheckAllFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return TAB_TITLES[position]
    }

    override fun getCount(): Int {
        return TAB_TITLES.size
    }
}