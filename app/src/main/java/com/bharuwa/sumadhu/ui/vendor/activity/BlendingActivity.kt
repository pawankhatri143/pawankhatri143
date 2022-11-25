package com.bharuwa.sumadhu.ui.vendor.activity

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.hideKeyboard
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.network.model.Flora
import com.bharuwa.sumadhu.ui.trader2.AddDispatchActivity
import com.bharuwa.sumadhu.ui.trader2.LoadDispatchActivity
import com.bharuwa.sumadhu.ui.trader2.model.TraderOrderItem
import com.bharuwa.sumadhu.ui.vendor.adapter.BlendingInwardAdapter
import com.bharuwa.sumadhu.ui.vendor.dialog.CompanyInfoDialog
import com.bharuwa.sumadhu.ui.vendor.model.*
import com.bharuwa.sumadhu.ui.viewmodel.BeeViewModel
import com.bharuwa.sumadhu.ui.viewmodel.BlendingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_inward_stock.*
import kotlinx.android.synthetic.main.activity_pre_blending.*
import kotlinx.android.synthetic.main.titlebarlayout.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class BlendingActivity : AppCompatActivity() {
    private val sdf = SimpleDateFormat("yyyy-MM-dd")
    private val blendingViewModel by viewModels<BlendingViewModel>()
    @Inject lateinit var blendingInwardAdapter: BlendingInwardAdapter
    private var locationName = ""
    private val beeFloraModel: BeeViewModel by viewModels()
    //private var floraList = mutableListOf<Flora>()
    private var floraList = mutableListOf<Flora>()
    var orderList = listOf<QualityCheckAllResult>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_blending)

        textTitle.text = getString(R.string.blending)
        finalBlendedList.clear()
         totalNetWeigthV = 0f
         totalHoneyWeigthV = 0f

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())
        editTextBlendingDate.setText(currentDate)
        editTextBlendingDate.setOnClickListener { openCalender() }

        imageViewDate.setOnClickListener {openCalender()}
        editTextDesiredQuantity.doOnTextChanged { text, start, before, count ->

            if (text?.isNotEmpty() == true){

                desiredWeigth= text.toString().toFloat()
                txtDesiredWeigth.text = desiredWeigth.toString()
            }
            else {
                desiredWeigth= 0f
                txtDesiredWeigth.text = desiredWeigth.toString()
            }

        }
        recyclerViewBlending.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewBlending.adapter = blendingInwardAdapter


        blending_layout.setOnClickListener {
            hideKeyboard()
        }
        blendingInwardAdapter.apply {
          //  onQuantityUpdate = {
            setOnItemClick {

                if (desiredWeigth > 1F  ){
                    editTextDesiredQuantity.isEnabled= false
                    editTextDesiredQuantity.isFocusable= false
                val selectedWtg = (totalHoneyWeigthV*100)/desiredWeigth
                txtSelectedWeigth.setText(totalHoneyWeigthV.toString())
                progressindicator.progress = selectedWtg.toInt()
                    if (selectedWtg.toInt()> 100){
                        txtOverWeigth.text= "${totalHoneyWeigthV- desiredWeigth}"
                        progressindicator.setIndicatorColor(ContextCompat.getColor(this@BlendingActivity, R.color.red))
                    }
                    else {
                        txtOverWeigth.text= "0"
                        progressindicator.setIndicatorColor(ContextCompat.getColor(this@BlendingActivity, R.color.green))
                    }

                }else{
                    txtSelectedWeigth.setText(totalHoneyWeigthV.toString())
                  //  txtWeigthIndecator.text= "0/${desiredWeigth.toString()}"
                 //   txtWeigthIndecator.setTextColor(ContextCompat.getColor(this@BlendingActivity, R.color.textColor))
                    progressindicator.setIndicatorColor(ContextCompat.getColor(this@BlendingActivity, R.color.green))
                    progressindicator.progress = 0
                    txtOverWeigth.text= "0"
                    editTextDesiredQuantity.isEnabled= true
                    editTextDesiredQuantity.isFocusable= true
                }
            }


          /* onItemClick{
                "${AddDispatchActivity.totalHoneyWeigth} Kg"
            }*/
        }

        buttonProceed.setOnClickListener {

      when {
                editTextDesiredQuantity.text.trim().isEmpty() -> showToast("Enter batch weight")
                txtSelectedWeigth.text.trim().isEmpty() -> showToast("Select at least one inward ")
                desiredWeigth < txtSelectedWeigth.text.toString().trim().toFloat() -> showToast("Over weight")
                desiredWeigth > txtSelectedWeigth.text.toString().trim().toFloat() -> showToast("Add more honey")
                editTextBlendingDate.text.trim().isEmpty() -> showToast("Enter blending date")
                editTextFlora.text.trim().isEmpty() -> showToast("Enter flora name")
              //  editTextRemark.text.trim().isEmpty() -> showToast("Enter remark")
                finalBlendedList.isEmpty() -> showToast("Select any inward")
                else -> getButtonProceed()
            }
        }
        editTextFlora.setOnClickListener { if (floraList.isNotEmpty()) setFlora() }

        getLocation()
        getBlendingVendorInward()


        getFlora()

    }

    private fun openCalender() {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val mCalender = Calendar.getInstance()
                mCalender[Calendar.YEAR] = year
                mCalender[Calendar.MONTH] = monthOfYear
                mCalender[Calendar.DAY_OF_MONTH] = dayOfMonth
                val selectedDate = sdf.format(mCalender.time)
                editTextBlendingDate.setText(selectedDate)
            }, year, month, day
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }


    /*private fun getBeeFlora() {
        val pd = getProgress()
        beeFloraModel.getBeeAndFlora().observe(this) {
            it?.let { resource ->

                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()

                        if (resource.data?.flora?.isNotEmpty() == true) {
                            Log.d("floraList", resource.data.json())
                            floraList.addAll(resource.data.flora!!)
                        }
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
    }*/

    private fun getFlora() {
        val pd = getProgress()
        beeFloraModel.getFloraList().observe(this) {
            it?.let { resource ->

                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()

                        if (resource.data?.result?.isNotEmpty() == true) {
                            Log.d("floraList", resource.data.json())
                            floraList.addAll(resource.data.result!!)
                        }
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

    private fun setFlora() {
        val popup = PopupMenu(this,editTextFlora)
        floraList.removeIf { it.code == "F999" }
        floraList.forEachIndexed { index, list->
            popup.menu.add(0,0,0,list.nameEn)
        }
        popup.setOnMenuItemClickListener { item ->
            editTextFlora.setText(item.title.toString())
            val filteredList=  orderList.filter { it.floraName?.lowercase() == item.title.toString().lowercase() }
            blendingInwardAdapter.setData(filteredList,this)
            refreshData()
            true
        }
        popup.show()
    }

    private fun getAdapter(list: List<String>): ArrayAdapter<String> {
        val adapter = ArrayAdapter(this, R.layout.spinner_source_layout, list)
        adapter.setDropDownViewResource(R.layout.spinner_source_layout)
        return adapter
    }

    private fun bindLocation(location: List<BlendingLocationResult>) {
        val list: List<String> = location.map { it.warehouseLocation }
        spinnerLocation.adapter = getAdapter(list)

        spinnerLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                spinnerLocation.tag = location[position].warehouseLocation
                locationName = location[position].warehouseLocation
                Log.d("locationId", locationName)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun getLocation() {
        val pd = getProgress()

        val inwardId = MyApp.get().getProfile()?._id.toString()//"629ef447626f78fd7bf7ae48"
        blendingViewModel.getVendorWarehouse(inwardId).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        if (it.data!!.status == "ok") {
                            it.data.result?.let { it1 -> bindLocation(it1) }
                        }else{
                            showToast(it.data.message)
                        }
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

    private fun getBlendingVendorInward() {
        val pd = getProgress()

        val userId = MyApp.get().getProfile()?._id.toString()//"628b743f116c9f834a7c22b1"
        Log.e("getBlendingVendorInward", userId)
        blendingViewModel.getVendorInwardForBlending(userId).observe(this) {
            it?.let { resource ->
                when (resource.status) {

                    Status.SUCCESS -> {
                        pd.dismiss()
                        Log.e("getBlendingVendorInward", it.data!!.json())
                        val list = it.data?.result!!.filter { it.qualityTestStatus=="APPROVED"}
                        orderList= list
                        blendingInwardAdapter.setData(list,this)
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

    private fun getButtonProceed() {
        val pd = getProgress()


      /*  val proceedBlendingModelList = mutableListOf<BlendingProceedModel>()
        blendingInwardAdapter.listOfBlending.forEach {
            val date = sdf.format(Date())
            val blendingModelData = BlendingProceedModel(it.inwardId, it.tempHoneyWeigth?.toInt(), date,)
            proceedBlendingModelList.add(blendingModelData)
        }*/

        try {

            val blendingBody = HashMap<String, Any>()
            blendingBody["vendorId"] = MyApp.get().getProfile()?._id.toString()//"6sdf6sdf67sdf8sdf"
            blendingBody["location"] = locationName
            blendingBody["desiredQuantity"] = editTextDesiredQuantity.text.toString().toFloat().toInt()
            blendingBody["outputQuantity"] = txtSelectedWeigth.text.toString().toFloat()
            blendingBody["flora"] = editTextFlora.text.toString().trim()
            blendingBody["BlendingDate"] = editTextBlendingDate.text.toString().trim()
            blendingBody["status"] = "Active"
            blendingBody["blendingStatus"] = "Active"
            blendingBody["remark1"] = editTextRemark.text.toString().trim()
            blendingBody["honeyBlendingDetails"] = finalBlendedList


            Log.e("proceedParams", blendingBody.json())

            blendingViewModel.addHoneyBlending(blendingBody).observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            pd.dismiss()
                            Log.e("resonse","addHoneyBlending"+it.data?.json())
                            if (it.data?.status == "ok") {
                                showToast(it.data.message)
                                openCompanyInfoDialog(it.data)
                            }
                            else{
                                showToast(it.data!!.message)
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
        }catch (e: Exception){}


    }

    private fun openCompanyInfoDialog(data: AddProceedBlendingModel) {
        val dialog = CompanyInfoDialog(this,data.result.desiredQuantity.toFloat())
        dialog.setDialogDismissListener {
            sendCompanyDetails(it,data.result)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun sendCompanyDetails(
        companymodel: CompanyInfoModel,
        result: AddProceedBlendingResult
    ) {
        val pd = getProgress()

        val body = HashMap<String, Any>()
        body["vendorId"] = MyApp.get().getProfile()?._id.toString()
        body["blendingId"] = result.blendingId
        body["blendingBatchId"] = result.batchId
        body["vendorWarehouse"] = result.location.toString()
        body["manufacturerId"] = "123456"
        body["manufacturerName"] = companymodel.cName
        body["manufacturerContact"] = "9999999999"
        body["manufacturerWarehouse"] =  companymodel.cAddress
        body["manufacturerWarehouseAddress"] =  companymodel.cAddress
        body["manufacturerWarehouseCity"] = companymodel.cAddress
        body["manufacturerWarehouseState"] =  companymodel.cAddress
        body["manufacturerWarehousePincode"] =  companymodel.cAddress
        body["manufacturerWarehouseContact"] = "9999999999"
        body["flora"] = result.flora
        body["desiredQuantity"] = result.desiredQuantity
        body["totalWeight"] = result.desiredQuantity
        body["NetWeight"] = companymodel.netWeigth
        body["remark1"] = result.remark1.toString()
        body["remark2"] = editTextBlendingDate.text.toString().trim()
        body["remark3"] = "Active"

        blendingViewModel.addCompanyDetails(body).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        it.data?.result?.let {

                            startActivity(Intent(this, BlendingSummaryActivity::class.java)
                                .putExtra("data",it.json())
                            )
                        }

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

    fun buttonBack(view: View) {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
    private fun refreshData(){

         finalBlendedList.clear()
         totalNetWeigthV = 0f
         totalHoneyWeigthV = 0f

        txtSelectedWeigth.text= totalHoneyWeigthV.toString()
        progressindicator.setIndicatorColor(ContextCompat.getColor(this@BlendingActivity, R.color.green))
        progressindicator.progress = 0
        txtOverWeigth.text= "0"
        editTextDesiredQuantity.isEnabled= true
        editTextDesiredQuantity.isFocusable= true
    }
    companion object{
        var desiredWeigth =0F
        var finalBlendedList = mutableListOf<BlendingProceedModel>()
        var totalNetWeigthV = 0f
        var totalHoneyWeigthV = 0f
    }



}