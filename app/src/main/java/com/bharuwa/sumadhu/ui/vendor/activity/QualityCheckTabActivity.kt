package com.bharuwa.sumadhu.ui.vendor.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.ui.vendor.adapter.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_quality_check_tab.*
import kotlinx.android.synthetic.main.titlebarlayout.*

class QualityCheckTabActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quality_check_tab)

        textTitle.text = "Quality Check Detail"
        setTabs()

    }

    private fun setTabs() {
        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun buttonBack(view: View) {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

}