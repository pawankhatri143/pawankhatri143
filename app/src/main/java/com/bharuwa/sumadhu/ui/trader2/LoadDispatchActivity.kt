package com.bharuwa.sumadhu.ui.trader2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.databinding.ActivityAddDispatchBinding
import com.bharuwa.sumadhu.databinding.ActivityLoadDispatchBinding
import com.bharuwa.sumadhu.databinding.SelectToDispatchItemBinding
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.ui.trader.viewModel.BuyHoneyViewModel
import com.bharuwa.sumadhu.ui.trader2.adapter.AddDispatchOrderAdapter
import com.bharuwa.sumadhu.ui.trader2.adapter.SelectedToDispatchOrderAdapter
import com.bharuwa.sumadhu.ui.trader2.model.DispatchedOrder
import com.bharuwa.sumadhu.ui.trader2.model.ResultItem12
import com.bharuwa.sumadhu.ui.trader2.model.VendorDetail
import com.bharuwa.sumadhu.ui.trader2.model.WarehouseDetails
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class LoadDispatchActivity: AppCompatActivity(), View.OnClickListener {

    @Inject
    lateinit var aAdapter: SelectedToDispatchOrderAdapter
    private val viewModel: BuyHoneyViewModel by viewModels()
    private var vendorList: List<VendorDetail?>?= null
    private var vendorAddress: List<WarehouseDetails?>?= null
    private lateinit var _binding: ActivityLoadDispatchBinding

    private var dispatchList = mutableListOf<DispatchedOrder>()

    var vendorWarehouseId = ""
    var vendorWarehouseName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoadDispatchBinding.inflate(layoutInflater)
        setContentView(_binding.root)
      //  setContentView(R.layout.activity_load_dispatch)

        _binding.constraintLayout.visibility=GONE
        _binding.include.textTitle.text = "Load Pending  Dispatch"
        _binding.include.imgBack.setOnClickListener { finish() }

        _binding.recyclerView.apply {

            layoutManager = LinearLayoutManager(this@LoadDispatchActivity)
            adapter = aAdapter

        }
     /*   aAdapter.setItemClick {
            Log.e("fdfndfdf", "setItemClick: "+ AddDispatchActivity.totalHoneyWeigth)
            _binding.txtTotalDrumCapacity.text = "${AddDispatchActivity.totalHoneyWeigth} Kg"
            // _binding.edtWeigth.setText("${totalHoneyWeigth} Kg")
        }*/
        onClickListners()
        getVenderList()
        MyApp.get().getProfile()?._id?.let {
            getPendingDispatchList()
        }
        _binding.layoutEmpty.layoutContinue.setOnClickListener {
            startActivity(Intent(this, MyDispatchActivity::class.java))
            finish()
        }
    }

    private fun getPendingDispatchList() {

        val pd = getProgress()

        val traderID = MyApp.get().getProfile()?._id.toString()
        viewModel.getTraderDispatchOrderPennding(traderID).observe(this) {
            it?.let { resource ->
                Log.e("fdfndfdf", "getPendingDispatchList: " + resource.data?.json())

                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        if (resource.data?.result?.isNotEmpty() == true){
                            Log.e("fdfndfdf", "vendorList: setVender" )
                            aAdapter.setData(resource?.data.result as List<DispatchedOrder>)
                            dispatchList.addAll(resource?.data.result)
                        }else _binding.txtListTitle.visibility = View.GONE

                        _binding.constraintLayout.visibility=if (resource.data?.result?.isNotEmpty() == true) VISIBLE else GONE
                        _binding.layoutEmpty.layoutEmptyScreen.visibility=if (resource.data?.result?.isNotEmpty() == true) GONE else VISIBLE

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
            vendorID= vendorList?.get(item.groupId)?.userId.toString()
            vendorName= vendorList?.get(item.groupId)?.name.toString()
            getVendorWarehouseList(vendorList?.get(item.groupId)?.userId.toString())
            filterDispatchListRegardVender()
            true
        }
        popup.show()


    }

    private fun filterDispatchListRegardVender() {
        aAdapter.setData(dispatchList.filter { it.vendorId == vendorID })
    }

    private fun loadDispatch(){
        val pd = getProgress()

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())


        val profileData= MyApp.get().getProfile()
        profileData?.let {
            bodyLDispatch["traderId"] = it._id.toString()
            bodyLDispatch["traderName"] = it.name.toString()
        }

        bodyLDispatch["vehicalId"] = ""
        bodyLDispatch["vendorId"] = vendorID
        bodyLDispatch["vendorName"] = vendorName
        bodyLDispatch["vendorWarehouseId"] = vendorWarehouseId
        bodyLDispatch["vendorWarehouseName"] = vendorWarehouseName

        bodyLDispatch["vehicalType"] = _binding.edtVType.text.toString()
        bodyLDispatch["transport"] = _binding.edtTrasport.text.toString()
        bodyLDispatch["VehicalNumber"] = _binding.edtVehicleNumber.text.toString()
        bodyLDispatch["driverName"] = _binding.edtDriverName.text.toString()
        bodyLDispatch["driverNumber"] = _binding.edtDriverNumber.text.toString()
//        bodyLDispatch["startPoint"] = _binding.edtStartPoint.text.toString()
//        bodyLDispatch["endPoint"] = _binding.edtEndPoint.text.toString()
        bodyLDispatch["dispatchOrders"] = selectedDispatchedOrderIdList
        bodyLDispatch["poNumber"] = _binding.edtPONumber.text.toString()
        bodyLDispatch["invoiceNumber"] = _binding.edtInvoiceNumber.text.toString()
        bodyLDispatch["createDate"] = currentDate
        bodyLDispatch["vehicalRunningStatus"] = "On the Way"
        bodyLDispatch["status"] = "Active"
        bodyLDispatch["Remark1"] = _binding.edtOther.text.toString()
        bodyLDispatch["Remark2"] = ""
        bodyLDispatch["Remark3"] = ""
        bodyLDispatch["Remark4"] = ""
        bodyLDispatch["Remark5"] = ""
        Log.e("loadDispatch", "bodyLDispatch: " + bodyLDispatch.json())

        viewModel.loadDispatch(bodyLDispatch).observe(this) {

            it?.let { resource ->
                Log.e("loadDispatch", "response: " + it.json())
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        selectedDispatchedOrderIdList.clear()
                        vendorID= ""
                        vendorName= ""

                        it.message?.let { msg-> showToast(msg) }
                        startActivity(Intent(this, LoadDispatchSummaryActivity::class.java).putExtra("data",resource.data?.result?.json()))

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
        _binding.edtVType.setOnClickListener(this)
     //   _binding.edtTrasport.setOnClickListener(this)
        _binding.btnProceed.setOnClickListener(this)
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
            _binding.edtVType -> bindVehical()
         //   _binding.edtTrasport -> bindTrasportCompany()
            _binding.btnProceed -> checkValidation()

        }
    }

    private fun checkValidation() {

        when{
            _binding.edtVType.text.isBlank() -> showToast("Please Select Vehical Type")
            _binding.edtVehicleNumber.text.isBlank() -> showToast("Please Enter Vehical Number")
         //   _binding.edtDriverName.text.isBlank() -> showToast("Please Enter Driver Name")
            _binding.edtDriverNumber.text.isBlank() -> showToast("Please Enter Driver Number")
            _binding.edtDriverNumber.text.length != 10 -> showToast("Please Enter Valid Mobile Number")
//            _binding.edtStartPoint.text.isBlank() -> showToast("Please Enter Start Point")
//            _binding.edtEndPoint.text.isBlank() -> showToast("Please Enter End Point")
            _binding.edtTrasport.text.isBlank() -> showToast("Please Enter Transport")
            _binding.edtInvoiceNumber.text.isBlank() -> showToast("Please Enter Invoice Number")
            selectedDispatchedOrderIdList.isEmpty()  -> showToast("Please Select Pending Dispatch List")


            else -> loadDispatch()
        }

    }
    private fun setVenderAddress() {
        val popup = PopupMenu(this,_binding.edtVndrAddress)


        vendorAddress?.forEach {list->
            popup.menu.add(0,0,0,list?.warehouseName)
            // popup.menu.add(0,0,0,list?.vendorName)
        }
        popup.setOnMenuItemClickListener { item ->
            _binding.edtVndrAddress.setText(item.title.toString())
            vendorWarehouseId= vendorAddress?.get(item.groupId)?.warehouseId.toString()
            vendorWarehouseName= vendorAddress?.get(item.groupId)?.warehouseName.toString()
            true
        }
        popup.show()



    }

    private fun bindTrasportCompany() {

        val popup = PopupMenu(this,  _binding.edtTrasport)
        popup.menu.add(0,0,0,"Rama Transport")
        popup.menu.add(0,0,0,"Patanjali Parivahan")

        popup.setOnMenuItemClickListener { item ->
            _binding.edtTrasport.setText(item.title.toString())
            true
        }
        popup.show()
    }

    private fun bindVehical() {
        val popup = PopupMenu(this,  _binding.edtVType)
        popup.menu.add(0,0,0,"Truck")
        popup.menu.add(0,0,0,"Tempo")

        popup.setOnMenuItemClickListener { item ->
            _binding.edtVType.setText(item.title.toString())
            true
        }
        popup.show()
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

    companion object{
        var selectedDispatchedOrderIdList: MutableList<String> = ArrayList()
        val bodyLDispatch = HashMap<String, Any>()
        var totalNetWeigth = 0f
        var totalHoneyWeigth = 0f

         var vendorID = ""
         var vendorName = ""
    }
}