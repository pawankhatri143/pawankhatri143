package com.bharuwa.sumadhu.ui.trader2

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.constants.Constants
import com.bharuwa.sumadhu.constants.Constants.FARMER
import com.bharuwa.sumadhu.constants.Constants.TRADER
import com.bharuwa.sumadhu.constants.Util.fromJson
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.databinding.ActivityTraderandFarmDetailsBinding
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.ui.trader.activity.AddDrumActivity
import com.bharuwa.sumadhu.ui.trader.viewModel.BuyHoneyViewModel
import com.bharuwa.sumadhu.ui.trader2.FindUserDetailsActivity.Companion.buyFromUserType
import com.bharuwa.sumadhu.ui.trader2.adapter.FarmDetailAdapter
import com.bharuwa.sumadhu.ui.trader2.adapter.MyOrderAdapter
import com.bharuwa.sumadhu.ui.trader2.adapter.TraderOrderAdapter
import com.bharuwa.sumadhu.ui.trader2.model.Result2Item
import com.bharuwa.sumadhu.ui.trader2.model.ResultItem12
import com.bharuwa.sumadhu.ui.trader2.model.SearchTraderModel
import com.bharuwa.sumadhu.ui.trader2.model.TempProfile
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLEncoder
import javax.inject.Inject

@AndroidEntryPoint
class TraderAndFarmDetailsActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityTraderandFarmDetailsBinding
    private var data: SearchTraderModel? = null
    private var userName = ""
    private var userAddress = ""
    private val viewModel : BuyHoneyViewModel by viewModels()

    val items = arrayOf("Scan barcode", "Manual order")
    var checkedItem = 0

    private var selectedFarmDetail: Result2Item?= null
    private var selectedTraderOrder: ResultItem12?= null

    @Inject lateinit var aAdapter: FarmDetailAdapter
    @Inject lateinit var orderAdapter: TraderOrderAdapter
    companion object{
        var tempProfile: TempProfile? = null

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTraderandFarmDetailsBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.include.textTitle.text = "Buy Honey"
        _binding.include.imgBack.setOnClickListener { finish() }

        data = intent?.extras?.getString("data")?.fromJson<SearchTraderModel>()

        Log.e("buyFromUserType", "onCreate: "+buyFromUserType )
        _binding.recyclerView.apply {

            layoutManager = LinearLayoutManager(this@TraderAndFarmDetailsActivity)
            if (buyFromUserType == FARMER) adapter = aAdapter
            else  adapter = orderAdapter

        }


        data?.let {
            userName= it.result?.name.toString()
            userAddress= it.result?.address.toString()

            _binding.userInfo.txtName.text = userName
//                "${it.result?.name.toString()} (${it.result?.userType.toString()})"
            _binding.userInfo.txtFieldId.text = it.result?.mobileNumber
            _binding.userInfo.txtAddress.text =
                "${it.result?.address.toString()}, ${it.result?.city.toString()}, ${it.result?.district.toString()} "


            when(buyFromUserType){
                FARMER ->{
//                    _binding.txtFarmListTittle.visibility = View.VISIBLE
                    if (!it.result2.isNullOrEmpty()) aAdapter.setData(it.result2 as List<Result2Item>)
                }
                TRADER -> {
                    _binding.txtFarmListTittle.text = "Order List"
                    it.result?.traderId?.let { it1 -> getMyOrder(it1) }
                    if (!it.result2.isNullOrEmpty()) aAdapter.setData(it.result2 as List<Result2Item>)
                }
            }
            /*if (buyFromUserType == FARMER){
                _binding.txtFarmListTittle.visibility = View.VISIBLE
                if (!it.result2.isNullOrEmpty()) aAdapter.setData(it.result2 as List<Result2Item>)
            }else _binding.txtFarmListTittle.text = "Order List"*/


            aAdapter.setItemClick {
                if (it.clickedOnLocation == false){
                    selectedFarmDetail = it
                }else{
                    try {
                        val strUri = "http://maps.google.com/maps?q=loc:${it.latitude},${it.longitude}"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
                        intent.setClassName(
                            "com.google.android.apps.maps",
                            "com.google.android.maps.MapsActivity"
                        )
                        startActivity(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

            }

            orderAdapter.setItemClick {
                selectedTraderOrder= it
            }

          /*  if (buyFromUserType.lowercase() != Constants.FARMER && it.result2.isNullOrEmpty()) {
                _binding.btnContinue.visibility = View.VISIBLE
                _binding.txtFarmListTittle.visibility = View.INVISIBLE

            }*/


            _binding.btnContinue.setOnClickListener {_->

//                if (selectedFarmDetail != null)
//                if (data?.result?.userType?.contains(",") == true)
                    choseOption()

            }

        }
    }
    private fun choseOption(){

        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.buy_from))
            .setCancelable(false)
            .setSingleChoiceItems(items,checkedItem) { dialog, position ->
                when(position){
                    0 -> checkedItem = 0
                    1 -> checkedItem = 1

                }
            }
            .setNeutralButton(resources.getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.ok)) { dialog, position ->
                when(checkedItem){
                    0 -> openScanner()
                    1 -> openManualOrder()
                }
            }
            .show()
    }

    private fun openManualOrder() {

        when(buyFromUserType.lowercase()){

        Constants.FARMER.lowercase() -> {
            if (selectedFarmDetail != null){
                val farmAddress = "${selectedFarmDetail?.address.toString()}, ${selectedFarmDetail?.city.toString()}, (${selectedFarmDetail?.district.toString()}) "
                tempProfile= TempProfile(userName,selectedFarmDetail?.name,farmAddress,selectedFarmDetail?.beeName, selectedFarmDetail?.flora)
              //  tempProfile= TempProfile(userName,userAddress,selectedFarmDetail?.beeName, selectedFarmDetail?.flora)
                startActivity(
                    Intent(this, ManualOrderActivity::class.java)
                        .putExtra("data", selectedFarmDetail?.json())
                )
            }else showToast("Please select farm")

        }
        Constants.TRADER.lowercase() -> {
            if (selectedTraderOrder!=null){
                tempProfile= TempProfile(userName,selectedTraderOrder?.farmId,userAddress,selectedTraderOrder?.extraRemark4, listOf(selectedTraderOrder?.flora.toString()))
                startActivity(
                    Intent(this, ManualOrderActivity::class.java)
                        .putExtra("data", data?.json())
                        .putExtra("orderDetails", selectedTraderOrder?.json())
                        .putExtra("byFromTrader", true)
                        .putExtra("traderId", data?.result?.traderId)
                )
            }else showToast("Please select any order")

        }
    }
    }

    private fun openScanner() {
        when(buyFromUserType.lowercase()){

            Constants.FARMER.lowercase() -> {
                if (selectedFarmDetail != null){
                    val farmAddress = "${selectedFarmDetail?.address.toString()}, ${selectedFarmDetail?.city.toString()}, (${selectedFarmDetail?.district.toString()}) "
                    tempProfile= TempProfile(userName,selectedFarmDetail?.name,farmAddress,selectedFarmDetail?.beeName, selectedFarmDetail?.flora)

                    // tempProfile= TempProfile(userName,userAddress,selectedFarmDetail?.beeName, selectedFarmDetail?.flora)
                    startActivity(
                        Intent(this, AddDrumActivity::class.java)
                            .putExtra("data", selectedFarmDetail?.json())
                    )
                }else showToast("Please select farm")

            }
            Constants.TRADER.lowercase() -> {
                tempProfile= TempProfile(userName,null,userAddress,null, null)
                startActivity(
                    Intent(this, AddDrumActivity::class.java)
                        .putExtra("data", data?.json())
                        .putExtra("byFromTrader", true)
                        .putExtra("traderId", data?.result?.traderId)
                )
            }
            //   Constants.BOTH.lowercase() ->
        }



    }

    private fun getMyOrder(userId: String) {
        val pd = getProgress()

        viewModel.getMyOrder(userId).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        orderAdapter.setData(resource.data?.result?.sortedByDescending { it?.orderDate })
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



}
