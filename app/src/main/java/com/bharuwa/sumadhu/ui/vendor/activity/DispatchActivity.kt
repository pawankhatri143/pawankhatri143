package com.bharuwa.sumadhu.ui.vendor.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Util.fromJson
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.databinding.ActivityDispatchBinding
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.ui.trader2.LoadDispatchActivity
import com.bharuwa.sumadhu.ui.vendor.adapter.VendorDispatchAdapter
import com.bharuwa.sumadhu.ui.vendor.model.BlendedItem
import com.bharuwa.sumadhu.ui.viewmodel.BlendingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.company_info_dialog.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class DispatchActivity : AppCompatActivity(),View.OnClickListener {

    @Inject lateinit var aAdapter: VendorDispatchAdapter
    private lateinit var _binding: ActivityDispatchBinding
    private val viewModel: BlendingViewModel by viewModels()
    private var seletedBatch: BlendedItem? = null
    private  var pendingDispatchList : List<BlendedItem>?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDispatchBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.include.textTitle.text = getString(R.string.dispatch)
        _binding.include.imgBack.setOnClickListener { finish() }

     /*   _binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@DispatchActivity, LinearLayoutManager.VERTICAL, false)
            adapter = aAdapter
        }
        aAdapter.onItemClick {
            seletedBatch= it
        }
*/

        intent.getStringExtra("data")?.fromJson<BlendedItem>()?.let {
            seletedBatch=it
        }
        _binding.edtVType.setOnClickListener(this)
     //   _binding.edtCompanyName.setOnClickListener(this)
        _binding.btnDispatchNow.setOnClickListener(this)

     //   getPendingBatchesList()

    }

    override fun onClick(v: View?) {
        when(v){
            _binding.edtVType -> bindVehical()
            _binding.edtCompanyName ->setCompanyName()
            _binding.btnDispatchNow ->  checkValidation()
        }
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


    private fun checkValidation() {

        when{
            _binding.edtVType.text.isBlank() -> showToast("Please Select Vehical Type")
            _binding.edtVehicleNumber.text.isBlank() -> showToast("Please Enter Vehical Number")
            //   _binding.edtDriverName.text.isBlank() -> showToast("Please Enter Driver Name")
            _binding.edtDriverNumber.text.isBlank() -> showToast("Please Enter Driver Number")
            _binding.edtDriverNumber.text.length != 10 -> showToast("Please Enter Valid Mobile Number")
            _binding.edtInvoiceNumber.text.isBlank() -> showToast("Please Enter Invoice Number")
          //  _binding.edtCompanyName.text.isBlank() -> showToast("Please Enter Company Name")
           // seletedBatch!=null -> showToast("Please Select Pending Dispatch List")
        //    LoadDispatchActivity.selectedDispatchedOrderIdList.isEmpty()  -> showToast("Please Select Pending Dispatch List")


            else -> loadDispatch()
        }

    }

    private fun getPendingBatchesList(){
        val pd = getProgress()

        viewModel.getBlendedHoneyList(MyApp.get().getProfile()?._id.toString()).observe(this) {

            it?.let { resource ->
                Log.e("loadDispatch", "response: " + it.json())
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        pendingDispatchList= it.data?.result
                        aAdapter.setData(it.data?.result)
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
    private fun setCompanyName() {

        val popupMenu = PopupMenu(this,edtCompanyName)

            popupMenu.menu.add(1,1,1,"All")
            popupMenu.menu.add(1,2,1,"Pragati Organics Private Limited")
            popupMenu.menu.add(1,3,1,"Patanjali Ayurved Private Limited")


        popupMenu.setOnMenuItemClickListener { item ->

            edtCompanyName.setText(item.title.toString())

            if (item.itemId == 1)  aAdapter.setData(pendingDispatchList)
            else{
                aAdapter.setData(pendingDispatchList?.filter { it.manufacturerName == item.title.toString() }
                    ?: pendingDispatchList)
            }


            true
        }
        popupMenu.show()
    }
    private fun loadDispatch(){
        val pd = getProgress()

       /* val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())*/

        val body = HashMap<String, Any>()

        val profileData= MyApp.get().getProfile()
        profileData?.let {
            body["vendorId"] = it._id.toString()
            body["vendorName"] = it.name.toString()
        }

        body["blendingId"] = seletedBatch?.blendingId.toString()
        body["manufacturId"] = seletedBatch?.manufacturerId.toString()
        body["manufacturName"] = seletedBatch?.manufacturerName.toString()

        body["vendorWarehouse"] = seletedBatch?.vendorWarehouse.toString()
        body["flora"] = seletedBatch?.flora.toString()
        body["vehicalNumber"] = _binding.edtVehicleNumber.text.toString()
        body["driverName"] = _binding.edtDriverName.text.toString()
        body["driverNumber"] = _binding.edtDriverNumber.text.toString()

        body["poNumber"] = _binding.edtPONumber.text.toString()
        body["poAmount"] = 2000.00
        body["invoiceNumber"] = _binding.edtInvoiceNumber.text.toString()
        body["invoiceAmount"] = 2000.00
        body["transport"] = "Haridwar"
        body["dispatchNetWeight"] = seletedBatch?.netWeight.toString()
        body["dispatchHoneyWeight"] = seletedBatch?.desiredQuantity.toString()
        body["remark1"] = _binding.edtOther.text.toString()
        body["remark2"] = ""
        body["vehicalLodadedBy"] = "Ram ji"
        Log.e("loadDispatch", "bodyLDispatch: " + body.json())

        viewModel.loadVenderDispatch(body).observe(this) {

            it?.let { resource ->
                Log.e("loadDispatch", "response: " + it.json())
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        if (it.data?.status == "ok") {
                            setResult(RESULT_OK)
                            finish()
                        }
                        showToast(it.data?.message.toString())

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
}