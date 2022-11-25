package com.bharuwa.sumadhu.ui.farm

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.ContextUtils
import com.bharuwa.sumadhu.constants.Permissions.checkCameraPermission
import com.bharuwa.sumadhu.constants.Util.fromJson
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.databinding.ActivityBoxScannedBinding
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.network.model.BoxesModel
import com.bharuwa.sumadhu.network.model.LocationsModel
import com.bharuwa.sumadhu.ui.adapter.MyBoxesAdapter
import com.bharuwa.sumadhu.ui.viewmodel.BoxesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_box_scanned.*
import kotlinx.android.synthetic.main.titlebarlayout.*
import java.util.*


@AndroidEntryPoint
class FarmSetupActivity : AppCompatActivity(){
    private lateinit var myfarmAdapter: MyBoxesAdapter
    private var _binding: ActivityBoxScannedBinding?=null
    private val binding get() = _binding!!
    private val boxesViewModel: BoxesViewModel by viewModels()
    private var locationData:LocationsModel?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding=ActivityBoxScannedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.include4.textTitle.text=getString(R.string.farm_location)
//        boxesViewModel= ViewModelProvider(this).get(BoxesViewModel::class.java)
        myfarmAdapter = MyBoxesAdapter().apply {
            setItemClick {
            }
        }
        var actionType=intent.getStringExtra("from")
        txtChangeAddress.visibility= if(actionType=="Add More") View.GONE else VISIBLE
        locationData=intent.getStringExtra("locationObject")?.fromJson()
        val recyclerView: RecyclerView = binding.recycleView
        recyclerView.layoutManager = GridLayoutManager(this,2)
        recyclerView.adapter = myfarmAdapter


        textTitle.text=getString(R.string.farm_setup)
        imageViewBack.setOnClickListener {
            onBackPressed()
        }
        layoutSaveScanBox.setOnClickListener {
            val intent = Intent()
            setResult(RESULT_OK, intent)
            finish()
        }
        locationData?.let {
            setLocationData()
            getBoxes()
            binding.swipeRefresh.setOnRefreshListener {
                getBoxes()
                binding.swipeRefresh.isRefreshing=false
            }
        }
        binding.txtChangeAddress.setOnClickListener {
            updatedLocationResult.launch(Intent(this, FarmListActivity::class.java).putExtra("locationData",MyApp.get().getDefaultLocation()?.json()))
        }
        binding.tvScanBarcode.setOnClickListener {
            if (checkCameraPermission(this)) {
               if(MyApp.get().getDefaultLocation()!=null) {
                    barcodeScanedResult.launch(Intent(this, BarcodeScannerActivity::class.java).putExtra("locationId",locationData?._id))
               }
                else showToast(getString(R.string.please_set_location_first))
            }
        }
    }
    private fun getBoxes(){
        var pd=getProgress()
        boxesViewModel!!.getBoxes(MyApp.get().getProfile()?._id!!,locationData?._id!!).observe(this){
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        setAdapterData(it.data!!)
                    }
                    Status.ERROR -> {
                        pd.dismiss()
                        if (it.message == "internet") {
                            showAlert(getString(R.string.network_error),getString(
                                R.string.no_internet))
                        } else {
                            showAlert(getString(R.string.error), it?.message.toString())
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun setAdapterData(list: List<BoxesModel>) {
        txtChangeAddress.visibility= if(list.isNotEmpty()) View.GONE else VISIBLE
        binding.tvTotalboxCount.text="${getString(R.string.total_boxes)} : ${list.size}"
        myfarmAdapter.setAdapterData(list)
    }
    private val updatedLocationResult =registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result->
        if(result.resultCode==Activity.RESULT_OK)
        {
            val data: Intent? = result.data
            locationData=data?.getStringExtra("locationObject")?.fromJson()
            locationData?.let {
                setLocationData()
            }
        }

    }
    private fun setLocationData(){
        locationData?.let {
            txtAddressName.text=it.name
            tvLocation.text="${it.address}, ${it.city}, ${it.district}, ${it.state}-${it.pincode}"
        }
    }

    private val barcodeScanedResult =registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            getBoxes()
        }
    }
    override fun onDestroy() {
       super.onDestroy()
       _binding = null
   }

    override fun onBackPressed() {
        val intent = Intent()
        setResult(RESULT_OK, intent)
        super.onBackPressed()
    }
    override fun attachBaseContext(newBase: Context?) {
        val locale = Locale(MyApp.get().getLocale())
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase!!, locale)
        super.attachBaseContext(localeUpdatedContext)
    }
}


