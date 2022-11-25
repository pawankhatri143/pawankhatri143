package com.bharuwa.sumadhu.ui.trader2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.databinding.ActivityAddDispatchBinding
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.network.model.Flora
import com.bharuwa.sumadhu.ui.trader.viewModel.BuyHoneyViewModel
import com.bharuwa.sumadhu.ui.trader2.adapter.AddDispatchOrderAdapter
import com.bharuwa.sumadhu.ui.trader2.model.*
import com.bharuwa.sumadhu.ui.viewmodel.BeeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AddDispatchActivity : AppCompatActivity(),View.OnClickListener {
    @Inject
    lateinit var aAdapter: AddDispatchOrderAdapter
    private val viewModel: BuyHoneyViewModel by viewModels()
    private val beeFloraModel: BeeViewModel by viewModels()
    private lateinit var _binding: ActivityAddDispatchBinding
    private var vendorList: List<VendorDetail?>?= null
    private var vendorAddress: List<WarehouseDetails?>?= null
    //private var floraList= mutableListOf<Flora>()
    private var floraList = mutableListOf<Flora>()
    private var vendorID : String? = null
    private var vendorName : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddDispatchBinding.inflate(layoutInflater)
        setContentView(_binding.root)
      //  setContentView(R.layout.activity_add_dispatch)
        traderOrderIdList?.clear()
        _binding.include.textTitle.text = "Add Dispatch"
        _binding.include.imgBack.setOnClickListener { finish() }

        _binding.recyclerView.apply {

            layoutManager = LinearLayoutManager(this@AddDispatchActivity)
            adapter = aAdapter

        }
        aAdapter.setItemClick {
            Log.e("fdfndfdf", "setItemClick: "+totalHoneyWeigth)
            _binding.txtTotalDrumCapacity.text = "${totalHoneyWeigth} Kg"
           // _binding.edtWeigth.setText("${totalHoneyWeigth} Kg")
        }
        onClickListners()
        getVenderList()
        getFlora()
        MyApp.get().getProfile()?._id?.let {
            getMyPendingOrder(it)
        }

    }

    /*private fun getBeeFlora() {
        val pd = getProgress()
        beeFloraModel.getBeeAndFlora().observe(this) {
            it?.let { resource ->

                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()

                        if (resource.data?.flora?.isNotEmpty() == true) {
                            floraList.addAll(resource.data.flora!!)
                            if (floraList.size > 1) floraList.removeAt(0)
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
                           // if (floraList.size > 1) floraList.removeAt(0)
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

    private fun onClickListners() {
        _binding.edtVenderName.setOnClickListener(this)
        _binding.edtVndrAddress.setOnClickListener(this)
        _binding.edtFloraName.setOnClickListener(this)
        _binding.btnProceed.setOnClickListener(this)
    }

    private fun getMyPendingOrder(userId: String) {
        val pd = getProgress()

        var flora ="ALL"
        if (_binding.edtFloraName.text.toString().isNotBlank()) flora = _binding.edtFloraName.text.toString()

        viewModel.getMyPendingOrder(userId,flora).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        if (resource.data?.result?.isNotEmpty() == true)
                        aAdapter.setData(resource.data?.result as List<ResultItem12>,this)
                        else   aAdapter.setData(listOf<ResultItem12>(),this)

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
    private fun getVenderList() {

        val pd = getProgress()

        viewModel.vendorList().observe(this) {
            it?.let { resource ->
                Log.e("fdfndfdf", "vendorList: " + resource.data?.json())

                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()

                        if (resource.data?.result?.isNotEmpty() == true){
                            Log.e("fdfndfdf", "vendorList: setVender" )
                            vendorList= resource.data.result
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

    private fun getVendorWarehouseList(vendorID: String?) {

        val pd = getProgress()

        viewModel.getVendorWarehouseList(vendorID).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()

                        if (resource.data?.result?.isNotEmpty() == true){
                            vendorAddress= resource?.data?.result
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

    private fun setVender() {
        val popup = PopupMenu(this,_binding.edtVenderName)


        vendorList?.forEachIndexed { index, list ->
            popup.menu.add(index,0,0,list?.name)
            // popup.menu.add(0,0,0,list?.vendorName)
        }
        popup.setOnMenuItemClickListener { item ->
            _binding.edtVenderName.setText(item.title.toString())
            Log.e("Dfdfdfd", "setVender: "+item.groupId.toString())
            vendorID= vendorList?.get(item.groupId)?.userId.toString()
            vendorName= vendorList?.get(item.groupId)?.name.toString()
                getVendorWarehouseList(vendorList?.get(item.groupId)?.userId.toString())
            true
        }
        popup.show()


    }
    private fun setVenderAddress() {
        val popup = PopupMenu(this,_binding.edtVndrAddress)


        vendorAddress?.forEach {list->
          popup.menu.add(0,0,0,list?.warehouseName)
           // popup.menu.add(0,0,0,list?.vendorName)
        }
        popup.setOnMenuItemClickListener { item ->
            _binding.edtVndrAddress.setText(item.title.toString())
            true
        }
        popup.show()

    }

    private fun setFlora() {
        val popup = PopupMenu(this,_binding.edtFloraName)
        if (floraList.size > 1) floraList.removeAt(0)
        floraList.forEach {list->
          popup.menu.add(0,0,0,list.nameEn)
          // popup.menu.add(0,0,0,list?.vendorName)
        }
        popup.setOnMenuItemClickListener { item ->
            _binding.edtFloraName.setText(item.title.toString())
            MyApp.get().getProfile()?._id?.let {
                getMyPendingOrder(it)
            }
            true
        }
        popup.show()
    }

    private fun addDispatch(){


         val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())

        val pd = getProgress()

        val body = HashMap<String, Any>()
        val profileData= MyApp.get().getProfile()
        profileData?.let {
            body["traderId"] = it._id.toString()
            body["traderName"] = it.name.toString()
        }

        body["vendorId"] = vendorID.toString()
        body["vendorName"] = vendorName.toString()
        body["manufacturId"] = ""
        body["manufacturName"] = ""
        body["vendorWarehouse"] = _binding.edtVndrAddress.text.toString()
        body["traderOrders"] = traderOrderIdList?.map { it.dispatchOrderId }!!
        body["traderDispatchDetails"] = traderOrderIdList!!
        body["flora"] = _binding.edtFloraName.text.toString()
        body["poNumber"] = ""//_binding.edtPONumber.text.toString()
        body["poAmount"] = "200"
        body["invoiceNumber"] = ""//_binding.edtInvoiceNumber.text.toString()
        body["invoiceAmount"] = "200"
        body["vehicalNumber"] = ""//_binding.edtVehicleNumber.text.toString()
        body["driverName"] =  ""//_binding.edtDriverName.text.toString()
        body["driverNumber"] = ""// _binding.edtDriverNumber.text.toString()
        body["transport"] =  "Bharat"
        body["dispatchNetWeight"] = totalNetWeigth.toString()
        body["dispatchHoneyWeight"] = totalHoneyWeigth.toString()
        body["dispatchHoneyPricePerKG"] = "300"
        body["dispatchTotalHoneyPrice"] = "300"
        body["dispatchType"] = "Trader"
        body["dispatchDate"] = currentDate
        body["dispatchStatus"] = "Active"
        body["dispatchStatusDate"] = currentDate
        body["vehicalLodadedBy"] = "Haridwar"
        body["remark1"] = _binding.edtOther.text.toString()
        body["remark2"] = ""
        body["remark3"] = ""
        body["remark4"] = ""
        body["status"] = "Active"



        Log.e("addTraderDispatch", "addDispatchBody: " + body?.json())

        viewModel.addTraderDispatch(body).observe(this) {
            it?.let { resource ->
                Log.e("fdfndfdf", "vendorList: " + resource.data?.json())
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()

                        if (resource.data?.result!=null){
                             totalNetWeigth = 0f
                             totalHoneyWeigth = 0f
                         startActivity(
                             Intent(this,DispatchSummaryActivity::class.java)
                             .putExtra("data",resource.data?.result.json())
                         )
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
    override fun onClick(v: View?) {
        when(v){
            _binding.edtVenderName -> if (vendorList.isNullOrEmpty()) getVenderList() else setVender()
            _binding.edtVndrAddress -> {
                when{
                    vendorID.isNullOrEmpty()-> showToast("Please select vendor")
                    vendorAddress.isNullOrEmpty()-> getVendorWarehouseList(vendorID)
                    else -> setVenderAddress()
                }
               /* if (vendorAddress.isNullOrEmpty()) getVendorWarehouseList(vendorID) else setVenderAddress()*/
            }
            _binding.edtFloraName -> if (vendorList.isNullOrEmpty()) getFlora() else setFlora()
            _binding.btnProceed -> {
                when{
                    vendorID.isNullOrBlank() -> showToast("Select Vendor")
                    _binding.edtVndrAddress.text.isNullOrBlank() -> showToast("Select Vendor location")
                    _binding.edtFloraName.text.isNullOrBlank() -> showToast("Select Flore")
                  //  _binding.edtPONumber.text.isNullOrBlank() -> showToast("Enter Po Number")
                 //   _binding.edtInvoiceNumber.text.isNullOrBlank() -> showToast("Enter Invoice Number")
                //    _binding.edtVehicleNumber.text.isNullOrBlank() -> showToast("Enter Vehicle Number")

              //     _binding.edtDriverName.text.isNullOrBlank() -> showToast("Enter Driver Name")
                //    _binding.edtDriverNumber.text.isNullOrBlank() -> showToast("Enter Driver Number")
                  //  _binding.edtDriverNumber.text.toString().length != 10 -> showToast("Enter Valid Number")
                 //   _binding.edtTransportAgency.text.isNullOrBlank() -> showToast("Enter Transport Agency")

                    totalNetWeigth == null -> showToast("Select any order")
                    totalHoneyWeigth < 1 -> showToast("Select any order")
                    traderOrderIdList!!.isEmpty() -> showToast("Select any order")

                    else ->addDispatch()

                    //
                }
            }
        }
    }


    companion object{
         var traderOrderIdList: MutableList<TraderOrderItem>?= ArrayList()
         var totalNetWeigth = 0f
         var totalHoneyWeigth = 0f
    }

}