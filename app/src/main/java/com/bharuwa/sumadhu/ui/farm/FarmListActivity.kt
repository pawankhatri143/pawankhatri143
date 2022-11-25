package com.bharuwa.sumadhu.ui.farm

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.ui.adapter.LocationListAdapter
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.ContextUtils
import com.bharuwa.sumadhu.constants.Util.fromJson
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.databinding.ActivityLocationListBinding
import com.bharuwa.sumadhu.network.model.LocationsModel
import com.bharuwa.sumadhu.network.tool.ErrorUtils
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.ui.viewmodel.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.titlebarlayout.*
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class FarmListActivity : AppCompatActivity() {
    private val locationViewModel: LocationViewModel by viewModels()
    private var _binding: ActivityLocationListBinding?=null
    private val binding get() = _binding!!

    private var locationData: LocationsModel? = null
    private var locationList= mutableListOf<LocationsModel>()

    // private var addressID : String? = null
    val EDIT_ADDRESS_CALLBACK = 837
    @Inject
    lateinit var addressAdapter: LocationListAdapter
    private var selectedLocationData:LocationsModel?=null
    companion object {
        var locationId: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding=ActivityLocationListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        textTitle.text=getString(R.string.your_farms)
//        locationViewModel=ViewModelProvider(this).get(LocationViewModel::class.java)
        if (intent?.extras?.getString("locationData") != null)
            locationData = intent?.extras?.getString("locationData")?.fromJson()

        binding.recyclerViewAddress.apply {
            layoutManager = LinearLayoutManager(this@FarmListActivity)
            adapter = addressAdapter
        }
        addressAdapter.apply {
            setItemClick{
                selectedLocationData = it

                if(selectedLocationData!=null) {
                    saveLocation()
                }
                else if(MyApp.get().getDefaultLocation()!=null){
                    val intent = Intent()
                    intent.putExtra("locationObject",MyApp.get().getDefaultLocation()!!.json())
                    setResult(RESULT_OK, intent)
                    finish()
                }
                else
                    showToast("please set farm location")
            }
        }
        binding.cardViewAddNewLocation.setOnClickListener {
            locationUpdate.launch(Intent(this, AddNewFarmActivity::class.java))
        }

      /*  binding.layoutSetLocation.setOnClickListener {

        }*/

        imageViewBack.setOnClickListener {
            finish()
        }
        binding.swipeRefresh.setOnRefreshListener {
            getLocationList()
            binding.swipeRefresh.isRefreshing=false
        }
        getLocationList()
    }
    private val locationUpdate =registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            getLocationList()
        }
    }
    private fun getLocationList(){
        val pd = getProgress()
        locationViewModel.getLocation(MyApp.get().getProfile()?._id!!).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        locationList = it.data!!.toMutableList()
                        addressAdapter.setData(locationList)
                        binding.layoutSetLocation.visibility=if(locationList.isNotEmpty()) View.VISIBLE else View.GONE
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
    private fun saveLocation(){
        var pd = getProgress()
        locationViewModel!!.saveDefaultLocation(selectedLocationData?._id!!,selectedLocationData?.userId!!).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        MyApp.get().setDefaultLocation(it.data)
                        val intent = Intent()
                        intent.putExtra("locationObject",it.data!!.json())
                        setResult(RESULT_OK, intent)
                        finish()
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

    fun addnewAddress(view: View) {
        startActivityForResult(
            Intent(this, FarmSetupActivity::class.java),
            EDIT_ADDRESS_CALLBACK
        )
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    override fun attachBaseContext(newBase: Context?) {
        val locale = Locale(MyApp.get().getLocale())
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase!!, locale)
        super.attachBaseContext(localeUpdatedContext)
    }
}