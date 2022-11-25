package com.bharuwa.sumadhu.ui.trader2

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Constants
import com.bharuwa.sumadhu.constants.Constants.FARMER
import com.bharuwa.sumadhu.constants.Constants.TRADER
import com.bharuwa.sumadhu.constants.Permissions
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.databinding.ActivityFindUserDetailsBinding
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.ui.trader.viewModel.BuyHoneyViewModel
import com.bharuwa.sumadhu.ui.trader.viewModel.FarmerDetailsViewModel
import com.bharuwa.sumadhu.ui.trader2.model.SearchTraderModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_find_user_details.*

@AndroidEntryPoint
class FindUserDetailsActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityFindUserDetailsBinding
    private val buyHoneyViewModel: BuyHoneyViewModel by viewModels()
    private val viewModel : FarmerDetailsViewModel by viewModels()

    val items = arrayOf("Farm", "Trader")
    var checkedItem = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFindUserDetailsBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        checkedItem = 0
        buyFromUserType= FARMER


        _binding.include.textTitle.text = "Buy Honey"
        _binding.include.imgBack.setOnClickListener { finish() }

        if (intent?.getStringExtra("userType").equals("vendor")){
            txtTittle.text= getString(R.string.enter_farmer_details)
        }



       /* if (intent.getBooleanExtra("userType",false)) _binding.rbBeeMkr.isChecked= true
        else _binding.rbTrader.isChecked= true*/
        _binding.btnSearch.setOnClickListener {
            when{
                _binding.edtMobileNumber.text.isBlank() -> showToast("Please enter phone number")
                _binding.edtMobileNumber.text.toString().length < 10 -> showToast("Please enter valid phone number")
                else -> findTraderDetails(_binding.edtMobileNumber.text.trim().toString())
            }

        }

        _binding.imgTabToScan.setOnClickListener { openScanner() }
        _binding.txtTabToScan.setOnClickListener { openScanner()}
    }

    private fun openScanner() {
        if (Permissions.checkCameraPermission(this))
            startActivityForResult(Intent(this, BarcodeScannerActivity2::class.java)
                .putExtra("fromScreen", "trader"), 883)

    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //if (requestCode == 883 && resultCode == Activity.RESULT_OK) {
        Log.e("dfdfdsfs", "barcodeID:= "+ "barcode" )
        if (resultCode == Activity.RESULT_OK) {

            if (!data?.getStringExtra("barCode").isNullOrEmpty()){

                try {
                    val barcode = data?.getStringExtra("barCode")
                    Log.e("dfdfdsfs", "barcodeID:= "+ barcode )
                    barcode?.let { farmID-> getFarmDetails(farmID) }

                }catch (e: Exception){
                    Log.e("dfdfdsfs", "barcodeID:= "+ e.localizedMessage )
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

                        if(!resource.data?.profile?.mobileNumber.isNullOrBlank()){
                            findTraderDetails(resource.data?.profile?.mobileNumber.toString())
                        }

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
    private fun findTraderDetails(mobileNumber: String) {

        val pd = getProgress()

        buyHoneyViewModel.findTraderDetails(mobileNumber).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()

                        if (resource.data?.result !=null) {
                            if (resource.data?.result?.userType?.contains(",") == true) {

                              /*  if (MyApp.get().getProfile()?.mobileNumber == mobileNumber){
                                    var buyFromUserType= FARMER
                                    openTraderFarmerDetail(resource.data)
                                }else */
                                if (MyApp.get().getProfile()?.userType.equals(Constants.VENDOR)){
                                    if (resource.data?.result2.isNullOrEmpty()){
                                       showAlert("Alert!","No Farm is attached")
                                    }else openTraderFarmerDetail(resource.data)
                                }else dialogForBuying(resource.data)
                            } else {
                                if (resource.data?.result?.userType?.lowercase() == "farmer"){
                                    checkedItem = 0
                                    buyFromUserType= FARMER

                                    if (MyApp.get().getProfile()?.userType.equals(Constants.VENDOR)){
                                        if (resource.data?.result2.isNullOrEmpty()){
                                            showAlert("Alert!","No Farm is attached")
                                        }else openTraderFarmerDetail(resource.data)
                                    }else dialogForBuying(resource.data)


                                }else {
                                    checkedItem = 1
                                    buyFromUserType= TRADER
                                    openTraderFarmerDetail(resource.data)
                                }

                            }
                        }else showAlert(
                            getString(R.string.record_not_found),
                            resource.data?.message.toString()
                        )

                        Log.e("fdfndfdf", "getFarmDetails: " + resource.data?.json())
                    }
                    Status.ERROR -> {
                        pd.dismiss()
                        if (it.message == "internet") {
                            showAlert(
                                getString(R.string.network_error),
                                getString(R.string.no_internet)
                            )
                        } else {
                            showAlert(
                                getString(R.string.error),
                                it?.message.toString()
                            )
                        }
                    }
                    else -> {}
                }
            }

        }
    }

    private fun openTraderFarmerDetail(data: SearchTraderModel) {

        startActivity(
            Intent(this, TraderAndFarmDetailsActivity::class.java)
                .putExtra("data", data.json())
                .putExtra("buyFrom","$checkedItem") // 0 for fields and 1 for direct trader
                .putExtra("mobile", _binding.edtMobileNumber.text.trim().toString())
                .putExtra("userType", intent.getBooleanExtra("userType", false))
        )
        finish()
    }

    private fun dialogForBuying(data: SearchTraderModel) {

        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.buy_from))
            .setCancelable(false)
            .setSingleChoiceItems(items,checkedItem) { dialog, position ->
                Log.e("selectedPost", "dialogForBuying: "+position )
                when(position){
                    0 -> {
                        checkedItem = 0
                        buyFromUserType= FARMER
                    }
                    1 -> {
                    checkedItem = 1
                    buyFromUserType= TRADER
                    }

                }
            }
            .setNeutralButton(resources.getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->

                openTraderFarmerDetail(data)
            }
            .show()
    }

    companion object{
        var buyFromUserType= FARMER
    }

}