package com.bharuwa.sumadhu.ui.trader.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.constants.Constants.FARMER_ID
import com.bharuwa.sumadhu.constants.Constants.FARM_ID
import com.bharuwa.sumadhu.constants.Permissions
import com.bharuwa.sumadhu.constants.Util.fromJson
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.databinding.ActivityBuyHoneyFromFarmerBinding
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.network.model.LocationsModel
import com.bharuwa.sumadhu.ui.farm.BarcodeScannerActivity
import com.bharuwa.sumadhu.ui.trader.adapter.BeeNameAdapter
import com.bharuwa.sumadhu.ui.trader.adapter.DemoAdapter2
import com.bharuwa.sumadhu.ui.trader.adapter.FloraAdapter
import com.bharuwa.sumadhu.ui.trader.model.DrumDetailsModel
import com.bharuwa.sumadhu.ui.trader.model.Farm
import com.bharuwa.sumadhu.ui.trader.model.FarmDetailsModel
import com.bharuwa.sumadhu.ui.trader.model.Profile
import com.bharuwa.sumadhu.ui.trader.viewModel.BuyHoneyViewModel
import com.bharuwa.sumadhu.ui.trader.viewModel.FarmerDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_buy_honey_from_farmer.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@AndroidEntryPoint
class BuyHoneyFromFarmerActivity : AppCompatActivity(), View.OnClickListener {

    @Inject
    lateinit var beeNameAdapter: BeeNameAdapter
    @Inject
    lateinit var floraAdapter: FloraAdapter
    private val viewModel : FarmerDetailsViewModel by viewModels()

    private lateinit var _binding: ActivityBuyHoneyFromFarmerBinding
    private var farmerFatched = false
    private var Farmer_id= ""
    private var Farm_id= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityBuyHoneyFromFarmerBinding.inflate(layoutInflater)
        setContentView(_binding.root)


        _binding.include.textTitle.text = getString(R.string.buy_honey_from_farmer)

        _binding.recyclerView.apply {
           layoutManager= LinearLayoutManager(this@BuyHoneyFromFarmerActivity, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = beeNameAdapter
        }
        _binding.recyclerView1.apply {
            layoutManager = LinearLayoutManager(this@BuyHoneyFromFarmerActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = floraAdapter
        }

        _binding.layoutScanCode.setOnClickListener(this)
        _binding.layoutBuyHoney.setOnClickListener(this)

//        getFarmDetails("6255024b450348129223299a")
    }

  /*  private val barCodeScanedResult =registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {

//            {"farmName":"Pawan Testing","boxes":"1","setupDate":"12/04/2022","flora":"Blackberry, Orange, Apple","farmLocation":"Haridwar, Haridwar, Uttarakhand","farmId":"6255024b450348129223299a","farmerMob":"8295160904","farmerName":"Pawan","beeName":"Apis cerana indica","mapLink":"http://maps.google.com/maps?q\u003dloc:29.9068665, 78.0009288"}
         //   Log.e("dfdfdsfs", "=: "+it.data?.getStringExtra("barCode"))
            if (!it.data?.getStringExtra("barCode").isNullOrEmpty()){

                try {
                    val barcode = it.data?.getStringExtra("barCode")
//                    Log.e("dfdfdsfs", "barcodeID:= "+ barcode )
                    barcode?.let { farmID-> getFarmDetails(farmID) }

                }catch (e: Exception){
                    Log.e("dfdfdsfs", "barcodeID:= "+ e.localizedMessage )
                }

            }

        }
    }*/

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

//            {"farmName":"Pawan Testing","boxes":"1","setupDate":"12/04/2022","flora":"Blackberry, Orange, Apple","farmLocation":"Haridwar, Haridwar, Uttarakhand","farmId":"6255024b450348129223299a","farmerMob":"8295160904","farmerName":"Pawan","beeName":"Apis cerana indica","mapLink":"http://maps.google.com/maps?q\u003dloc:29.9068665, 78.0009288"}
               Log.e("dfdfdsfs", "=: onActivityResult")
            if (!data?.getStringExtra("barCode").isNullOrEmpty()){

                try {
                    val barcode = data?.getStringExtra("barCode")
//                    Log.e("dfdfdsfs", "barcodeID:= "+ barcode )
                    barcode?.let { farmID-> getFarmDetails(farmID) }

                }catch (e: Exception){
                    Log.e("dfdfdsfs", "barcodeID:= "+ e.localizedMessage )
                }

            }

        }
    }



    fun back(view: View) {
        finish()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.layoutScanCode ->
                if (Permissions.checkCameraPermission(this))
                    startActivityForResult(
                        Intent(this, BarcodeScannerActivity::class.java)
                            .putExtra("fromScreen", "trader"), 883)
//                barCodeScanedResult.launch(Intent(this, BarcodeScannerActivity::class.java).putExtra("fromScreen","trader"))
            R.id.layoutBuyHoney -> {
                if (Permissions.checkCameraPermission(this)) {
                    if (farmerFatched) startActivity(
                        Intent(
                            this,
                            PurchaseDetailActivity::class.java
                        ).putExtra(FARMER_ID, Farmer_id).putExtra(FARM_ID, Farm_id)
                    )
                    else startActivityForResult( Intent(this, BarcodeScannerActivity::class.java).putExtra("fromScreen", "trader"),883)

                /*barCodeScanedResult.launch(
                        Intent(
                            this,
                            BarcodeScannerActivity::class.java
                        ).putExtra("fromScreen", "trader")
                    )*/

                }
            }
        }
    }

    private fun getFarmDetails(farmID: String) {
        val pd = getProgress()
        Log.e("fdfndfdf", "farmID: "+farmID )
        viewModel.fatchFarmDetails(farmID).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        updateUI(resource.data)
                        Log.e("fdfndfdf", "getFarmDetails: "+resource.data?.json() )
                    }
                    Status.ERROR -> {
                        pd.dismiss()
                        if (it.message == "internet") {
                            showAlert(getString(R.string.network_error),getString(R.string.no_internet))
                        } else {
                            showAlert(getString(R.string.error), it?.message.toString())
                        }
                    }
                    else -> {}
                }
            }

        }
    }


    private fun updateUI(data: FarmDetailsModel?) {
        _binding.txtName.text = data?.profile?.name
        _binding.txtReadyBox.text = data?.profile?.boxCount.toString()

        data?.farm?.beeName?.let {beeNameAdapter.setData(listOf(it))}
        data?.farm?.flora?.let {floraAdapter.setData(it as List<String>)  }

        Farmer_id= data?.farm?.userId.toString()
        Farm_id= data?.farm?._id.toString()
        farmerFatched = true
        farmerProfile = data?.profile
        farmerFarm = data?.farm
    }

    companion object{
        var farmerProfile: Profile? = null
        var farmerFarm: Farm? = null


    }
}