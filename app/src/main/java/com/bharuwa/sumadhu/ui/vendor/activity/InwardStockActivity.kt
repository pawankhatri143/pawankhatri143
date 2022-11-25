package com.bharuwa.sumadhu.ui.vendor.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.ui.vendor.adapter.SectionPageAdapter
import com.bharuwa.sumadhu.ui.vendor.fragment.InwordStockFarmerFrag
import com.bharuwa.sumadhu.ui.vendor.fragment.InwordStockTraderFrag
import com.bharuwa.sumadhu.ui.viewmodel.InwardStockViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_inward_stock.*
import javax.inject.Inject

@AndroidEntryPoint
class InwardStockActivity : AppCompatActivity() {

    @Inject lateinit var inWardStockViewModel: InwardStockViewModel
    private val UPDATE_INWARD_CALLBACK =738
    private lateinit var fragmentFarmer: InwordStockFarmerFrag
    private lateinit var fragmentTrader: InwordStockTraderFrag

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inward_stock)

        textTitles.text = getString(R.string.inward_stock2)
        fragmentFarmer = InwordStockFarmerFrag()
        fragmentTrader = InwordStockTraderFrag()


        btnBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }


        tabs.setupWithViewPager(viewPager)
        val adapter = SectionPageAdapter(supportFragmentManager)
        adapter.addFragment(fragmentTrader, "Trader")
        adapter.addFragment(fragmentFarmer, "Farmer")
        viewPager.adapter = adapter


    }



    override fun onResume() {
        super.onResume()
        getInwardStockTrader()
        getInwardStockFarmer()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode== UPDATE_INWARD_CALLBACK && resultCode == RESULT_OK) {
            getInwardStockTrader()
            getInwardStockFarmer()
        }
    }

    private fun getInwardStockFarmer() {


        val pd = getProgress()
        val userID = MyApp.get().getProfile()?._id.toString()//"629ef447626f78fd7bf7ae48"
        inWardStockViewModel.inWardStockFromFarmer(userID).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        it.data!!.result?.let {
                                it1 ->  fragmentFarmer.setData(it1) }
                    }
                    Status.ERROR -> {
                        pd.dismiss()
                        if (it.message == "internet") {
                            showAlert(getString(R.string.network_error),getString(R.string.no_internet))
                        } else {
                            showAlert(getString(R.string.error), it.message.toString())
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun getInwardStockTrader() {
        val pd = getProgress()
        val userID = MyApp.get().getProfile()?._id.toString()//"629ef447626f78fd7bf7ae48"
        inWardStockViewModel.inWardStockFromTrader(userID).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        it.data!!.result?.let {
                                it1 ->  fragmentTrader.setData(it1) }
                    }
                    Status.ERROR -> {
                        pd.dismiss()
                        if (it.message == "internet") {
                            showAlert(getString(R.string.network_error),getString(R.string.no_internet))
                        } else {
                            showAlert(getString(R.string.error), it.message.toString())
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

   /* fun back(view: View) {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }*/

}