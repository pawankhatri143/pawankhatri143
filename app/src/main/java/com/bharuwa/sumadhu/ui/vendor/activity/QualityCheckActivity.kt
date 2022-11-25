package com.bharuwa.sumadhu.ui.vendor.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.ui.vendor.adapter.SectionsPagerAdapter
import com.bharuwa.sumadhu.ui.viewmodel.QualityCheckViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_quality_check.*
import kotlinx.android.synthetic.main.appbarlayout.*
import javax.inject.Inject

@AndroidEntryPoint
class QualityCheckActivity : AppCompatActivity() {
    val qualityCheckViewModel by viewModels<QualityCheckViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quality_check)

        //qualityCheckViewModel.getVendorInward("629ef447626f78fd7bf7ae48")
        qualityCheckViewModel.getVendorInward(MyApp.get().getProfile()?._id.toString())

        textTitle.text = getString(R.string.quality_check)
        setTabs()

    }

    override fun onResume() {
        super.onResume()
      //  qualityCheckViewModel.getVendorInward(MyApp.get().getProfile()?._id.toString())
    }

    private fun setTabs() {
        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)
        tabs.setSelectedTabIndicatorColor(Color.parseColor("#000000"))
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun back(view: View) {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

}