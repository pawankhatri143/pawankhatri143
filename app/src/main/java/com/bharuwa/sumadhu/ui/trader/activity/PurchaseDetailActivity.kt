package com.bharuwa.sumadhu.ui.trader.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.constants.Constants
import com.bharuwa.sumadhu.databinding.ActivityBuyHoneyFromFarmerBinding
import com.bharuwa.sumadhu.databinding.ActivityPurchaseDetailBinding
import com.bharuwa.sumadhu.ui.trader.adapter.SectionsPagerAdapter2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_my_drum.*
import kotlinx.android.synthetic.main.appbarlayout.*

@AndroidEntryPoint
class PurchaseDetailActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityPurchaseDetailBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_purchase_detail)
        _binding = ActivityPurchaseDetailBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        textTitle.text = "Purchase Detail"
        setTabs()

       /* intent?.extras?.getString(Constants.FARMER_ID)?.let { Farmer_id = it }
        intent?.extras?.getString(Constants.FARM_ID)?.let { Farm_id = it }*/
    }

    private fun setTabs() {
        val sectionsPagerAdapter2 = SectionsPagerAdapter2(supportFragmentManager)
        viewPager.adapter = sectionsPagerAdapter2
        tabs.setupWithViewPager(viewPager)
    }

    fun back(view: View) {
        finish()
    }

   /* companion object{
        var Farmer_id= ""
        var Farm_id= ""
    }*/
}