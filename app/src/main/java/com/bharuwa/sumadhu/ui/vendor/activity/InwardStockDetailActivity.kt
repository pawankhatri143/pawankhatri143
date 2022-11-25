package com.bharuwa.sumadhu.ui.vendor.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Util.formatTo
import com.bharuwa.sumadhu.constants.Util.fromJson
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.constants.Util.toDate
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.ui.vendor.dialog.CancelReasionDialog
import com.bharuwa.sumadhu.ui.vendor.model.InwardStockResult
import com.bharuwa.sumadhu.ui.viewmodel.InwardStockViewModel
import com.bharuwa.sumadhu.ui.viewmodel.QualityCheckViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_inward_stock_detail.*
import kotlinx.android.synthetic.main.appbarlayout.*
import kotlinx.android.synthetic.main.custome_dialog.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class InwardStockDetailActivity : AppCompatActivity() {
    @Inject lateinit var inWardStockViewModel: InwardStockViewModel
    private val sdf = SimpleDateFormat("dd-MM-yyyy")
    private val viewModel by viewModels<QualityCheckViewModel>()
    private var dispatchID: String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inward_stock_detail)

        textTitle.text = getString(R.string.inward_detail)

        val inwardStockList = intent.getStringExtra("inwardStock")?.fromJson<InwardStockResult>()

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val formatted = current.format(formatter)
        Log.d("getCurrentTime", formatted)

        val getCurrentDate = sdf.format(Date())
        textViewInwardDate.text = "$getCurrentDate ($formatted)"
        btnAcceptInward.isEnabled = false
        btnCancelInwards.isEnabled = false
        btnAcceptInward.setOnClickListener {

            if (dispatchID != null) addVendorInward()
        else  addVendorInwardFarmer()
        }
        btnCancelInwards.setOnClickListener { giveRejectReason() }



        if (inwardStockList?.traderDispatchId != null) setText(inwardStockList)
        else setTextFarmer(inwardStockList)

        checkbox.setOnClickListener { 
            if (checkbox.isChecked){
                btnAcceptInward.alpha= 1f
                btnAcceptInward.isClickable= true
                btnAcceptInward.isEnabled = true

                btnCancelInwards.alpha= 1f
                btnCancelInwards.isClickable= true
                btnCancelInwards.isEnabled = true

            }else {
                btnAcceptInward.alpha= .5f
                btnAcceptInward.isClickable= false
                btnAcceptInward.isEnabled = false

                btnCancelInwards.alpha= .5f
                btnCancelInwards.isClickable= false
                btnCancelInwards.isEnabled = false
            }
        }
    }

    private fun giveRejectReason() {
        val dialog = CancelReasionDialog(this)
        dialog.setDialogDismissListener {
            if (dispatchID != null) callRejectionApi(it)
            else  callRejectionApiFarmer(it)
        }
        dialog.show()
    }

    private fun callRejectionApi(reason: String){
        val pd = getProgress()

      val result=  intent.getStringExtra("inwardStock")?.fromJson<InwardStockResult>()
        val body = HashMap<String, String>()
        body["dispatchId"] = result?.traderDispatchId.toString()
        body["dispatchStatus"] = "REJECTED"
        body["remark1"] = reason


        Log.e("rejectedParams", body.json())

        viewModel.inwardStatusUpdate(body).observe(this@InwardStockDetailActivity) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        Log.e("rejectedResponse","=="+ it.data?.json())
                        if (it.data?.status == "ok") {
                            setResult(RESULT_OK)
                            finish()
                        }
                        showToast(it.data?.message.toString())
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

    private fun callRejectionApiFarmer(reason: String){
        val pd = getProgress()

      val result=  intent.getStringExtra("inwardStock")?.fromJson<InwardStockResult>()
        val body = HashMap<String, String>()
        body["orderId"] = result?.orderId.toString()
        body["orderStatus"] = "REJECTED"
        body["extraRemark5"] = reason

        Log.e("rejectedParams", body.json())
        viewModel.inwardStatusUpdateFarmer(body).observe(this@InwardStockDetailActivity) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        Log.e("rejectedResponse","=="+ it.data?.json())
                        if (it.data?.status == "ok") {
                            setResult(RESULT_OK)
                            finish()
                        }
                        showToast(it.data?.message.toString())
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

   private fun setText(inwardStockList: InwardStockResult) {

        if (inwardStockList != null) {
            Log.d("inwardStockList>>>>", inwardStockList.json())
            textViewDispatchId.text = inwardStockList.disptchId
            textViewTraderId.text = inwardStockList.traderId
            textViewTraderName.text = inwardStockList.traderName
            textViewVendorId.text = inwardStockList.vendorId
            textViewVendorName.text = inwardStockList.vendorName
            textViewVendorWarehouse.text = inwardStockList.vendorWarehouse
            textViewFlora.text = inwardStockList.flora
            textViewPONumber.text = inwardStockList.poNumber
            textViewPOAmount.text = "${"\u20B9"} ${inwardStockList.poAmount}"
            textViewInvoiceNumber.text = inwardStockList.invoiceNumber
            textViewInvoiceAmount.text = "${"\u20B9"} ${inwardStockList.invoiceAmount}"
            textViewVehicleNumber.text = inwardStockList.vehicalNumber.toString()
            textViewDriverName.text = inwardStockList.driverName
            textViewDriverNumber.text = inwardStockList.driverNumber
            textViewTransport.text = inwardStockList.transport
            textViewDispatchType.text = inwardStockList.dispatchType
            textViewDispatchNetWeight.text = "${inwardStockList.dispatchNetWeight} Kg"
            textViewDispatchDate.text = inwardStockList.dispatchDate?.toDate("yyyy-MM-dd'T'HH:mm:ss")?.formatTo("dd-MM-yyyy")
            textViewDispatchStatus.text = inwardStockList.dispatchStatus

            if (textViewDispatchStatus.text == "INWARDED") {
                btnAcceptInward.visibility = View.GONE
                btnCancelInwards.visibility = View.GONE
            }
        }
    }

  private fun setTextFarmer(inwardStockList: InwardStockResult?) {

        if (inwardStockList != null) {
            Log.e("inwardStockList>>>>", inwardStockList.json())
            textViewDispatchId.text = inwardStockList.orderNo
            textViewTraderId.text = inwardStockList.traderId
            textViewTraderName.text = inwardStockList.farmerId
            textViewVendorId.text = inwardStockList.vendorId
            txtSellerName.text = getString(R.string.farmer_name_)
            textViewVendorName.text = MyApp.get().getProfile()?.name.toString()
            textViewVendorWarehouse.text = "N/A"
            textViewFlora.text = inwardStockList.flora
            textViewPONumber.text = "N/A"
            textViewPOAmount.text = "N/A"
            textViewInvoiceNumber.text = "N/A"
            textViewInvoiceAmount.text = "N/A"
            textViewVehicleNumber.text = "N/A"
            textViewDriverName.text = "N/A"
            textViewDriverNumber.text = "N/A"
            textViewTransport.text = "N/A"
            textViewDispatchType.text = "N/A"
            textViewDispatchNetWeight.text = "${inwardStockList.actualHoneyNetWeight} Kg"
            textViewDispatchDate.text = inwardStockList.dispatchDate?.toDate("yyyy-MM-dd'T'HH:mm:ss")?.formatTo("dd-MM-yyyy")
            textViewDispatchStatus.text = "DISPATCHED"//inwardStockList.dispatchStatus

            if (textViewDispatchStatus.text == "INWARDED") {
                btnAcceptInward.visibility = View.GONE
                btnCancelInwards.visibility = View.GONE
            }
        }
    }

    private fun addVendorInward() {
        val pd = getProgress()
        val body = HashMap<String, Any>()

        val inwardStockList = intent.getStringExtra("inwardStock")?.fromJson<InwardStockResult>()

        if (inwardStockList != null) {
            dispatchID= inwardStockList.disptchId.toString()
            body["traderDispatchId"] = inwardStockList.traderDispatchId.toString()
            body["disptchId"] = inwardStockList.disptchId.toString()
            body["traderId"] = inwardStockList.traderId.toString()
            body["traderName"] = inwardStockList.traderName.toString()
            body["vendorId"] = inwardStockList.vendorId.toString()
            body["vendorName"] = inwardStockList.vendorName.toString()
            body["vendorWarehouse"] = inwardStockList.vendorWarehouse.toString()
            body["traderOrders"] =
                if (inwardStockList.traderOrders == null)  emptyList<String>()
            else inwardStockList.traderOrders.toString()

            body["PONumber"] = inwardStockList.poNumber.toString()
            body["floraName"] = inwardStockList.flora.toString()
            body["vehicalDispatchId"] = inwardStockList.vehicalDispatchId.toString()
            body["vehicalNumber"] = inwardStockList.vehicalNumber.toString()
            body["driverName"] = inwardStockList.driverName.toString()
            body["driverMobile"] = inwardStockList.driverNumber.toString()
            //body["totalContainer"] = ""
            //body["remainingContainer"] = ""
            body["honeyTotalWeight"] = inwardStockList.dispatchHoneyWeight.toString()
            body["containerWeight"] = inwardStockList.dispatchHoneyWeight.toString()
            body["netWeight"] = inwardStockList.dispatchNetWeight.toString()
            body["totalAmount"] = inwardStockList.dispatchTotalHoneyPrice.toString()
            //body["recievedBy"] = inwardStockList.
            //body["inwardDetailId"] = inwardStockList.
           // body["remark"] = inwardStockList.
            //body["remarkOther"] = inwardStockList.
        }

        Log.e("addVendorInwardParams", body.json())

        inWardStockViewModel.addVendorInward(body).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        if (it.data?.status == "ok") {
                            //customDialog(it.data.message)
                            customDialog()
                        }else{
                            it.data?.let { it1 -> showToast(it1.message) }
                        }
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

    private fun addVendorInwardFarmer() {
        val pd = getProgress()
        val body = HashMap<String, Any>()

        val inwardStockList = intent.getStringExtra("inwardStock")?.fromJson<InwardStockResult>()

        if (inwardStockList != null) {
            dispatchID= null
            body["traderDispatchId"] = inwardStockList.orderId.toString()
            body["disptchId"] = inwardStockList.orderNo.toString()
        //    body["traderId"] = inwardStockList.farmerId.toString()
            body["traderName"] = inwardStockList.farmerId.toString()
            body["vendorId"] = MyApp.get().getProfile()?._id.toString()
            body["vendorName"] = MyApp.get().getProfile()?.name.toString()
        //    body["vendorWarehouse"] = inwardStockList.vendorWarehouse.toString()
            body["traderOrders"] =
                if (inwardStockList.traderOrders == null)  emptyList<String>()
                else inwardStockList.traderOrders.toString()

         //   body["PONumber"] = inwardStockList.poNumber.toString()
            body["floraName"] = inwardStockList.flora.toString()
        //    body["vehicalDispatchId"] = inwardStockList.vehicalDispatchId.toString()
        //    body["vehicalNumber"] = inwardStockList.vehicalNumber.toString()
            body["driverName"] = inwardStockList.farmerId.toString()
            body["driverMobile"] = inwardStockList.extraRemark1.toString()
            body["honeyTotalWeight"] = inwardStockList.actualHoneyNetWeight.toString()
            body["containerWeight"] = inwardStockList.actualHoneyNetWeight.toString()
            body["netWeight"] = inwardStockList.actualHoneyNetWeight.toString()
            body["totalAmount"] = "0"

            body["recievedBy"] = MyApp.get().getProfile()?.name.toString()
            body["inwardType"] = "FARMER"
            body["remark"] = "Purchase Direct from Farmer"
        }

        Log.e("addFarmerInwardParams", body.json())

        inWardStockViewModel.addFarmerOrderVendorInward(body).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        if (it.data?.status == "ok") {
                            //customDialog(it.data.message)
                            customDialog()
                        }else{
                            it.data?.let { it1 -> showToast(it1.message) }
                        }
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

    private fun customDialog() {
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.custome_dialog)
        customDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        customDialog.setCancelable(false)
        customDialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        customDialog.textViewMessage.text = "Inward Stock Updated"
        customDialog.buttonOk.setOnClickListener {
            startActivity(Intent(applicationContext, InwardStockActivity::class.java))
            finish()
            customDialog.dismiss()
        }
        customDialog.show()
    }

    /*private fun addVendorInward() {
        val pd = getProgress()
        val body = HashMap<String, String>()

        intent.getStringExtra("inwardStock")?.fromJson<InwardStockResult>()?.let {
            inWardStockViewModel.addVendorInward(it).observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            pd.dismiss()
                            if (it.data?.status == "ok") {
                                showToast(it.data.message)
                                //customDialog(it.data.message)
                                //startActivity(Intent(applicationContext, VendorDashboardActivity::class.java))
                                //finishAffinity()
                            }else{
                                it.data?.let { it1 -> showToast(it1.message) }
                            }
                        }
                        Status.ERROR -> {
                            pd.dismiss()
                            if (it.message == "internet") {
                                showAlert(getString(R.string.network_error),getString(R.string.no_internet))
                            } else {
                                showAlert(getString(R.string.error), it?.message.toString())
                            }
                        }
                    }
                }
            }
        }
    }*/

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun back(view: View) {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

}