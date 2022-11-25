package com.bharuwa.sumadhu.ui.vendor.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bharuwa.sumadhu.databinding.ActivityDispatchStatusBinding
import com.bharuwa.sumadhu.ui.vendor.adapter.VendorDispatchAdapter
import com.bharuwa.sumadhu.ui.vendor.adapter.ViewPagerAdapter
import com.bharuwa.sumadhu.ui.vendor.model.BlendedItem
import com.bharuwa.sumadhu.ui.viewmodel.BlendingViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class DispatchStatusActivity : AppCompatActivity() {

    @Inject
    lateinit var aAdapter: VendorDispatchAdapter
    private lateinit var _binding: ActivityDispatchStatusBinding

    val fragArray = arrayOf(
        "PENDING",
        "ALL"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityDispatchStatusBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.imgBack.setOnClickListener { finish() }
        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        _binding.viewPager.adapter = adapter

        TabLayoutMediator(_binding.tabLayout, _binding.viewPager) { tab, position ->
            tab.text = fragArray[position]
        }.attach()

    }
}