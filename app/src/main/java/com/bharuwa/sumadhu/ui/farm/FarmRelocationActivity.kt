package com.bharuwa.sumadhu.ui.farm

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.LinearLayoutManager
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.ContextUtils
import com.bharuwa.sumadhu.constants.Util.fromJson
import com.bharuwa.sumadhu.constants.Util.fromListJson
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.databinding.ActivityFarmSetupBinding
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.network.model.BoxesModel
import com.bharuwa.sumadhu.network.model.LocationsModel
import com.bharuwa.sumadhu.ui.adapter.MultipleBoxSelectionAdapter
import com.bharuwa.sumadhu.ui.viewmodel.BoxesViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class FarmRelocationActivity : AppCompatActivity() {
    private var _binding: ActivityFarmSetupBinding? = null
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var multipleSelectionAdapter: MultipleBoxSelectionAdapter
    private var boxesList = mutableListOf<BoxesModel>()
    private var location:LocationsModel?=null
    private val boxesViewModel: BoxesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityFarmSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        boxesViewModel= ViewModelProvider(this).get(BoxesViewModel::class.java)
        multipleSelectionAdapter = MultipleBoxSelectionAdapter().apply {
            setItemClick {
            }
        }
        val action=intent.getStringExtra("formAction")
        val boxesList=intent.getStringExtra("boxes")?.fromListJson<List<BoxesModel>>()!!

        binding.tvActionButton.text=action
        binding.layoutLocation.visibility=if(action==getString(R.string.relocate)) VISIBLE else GONE
        if(action==getString(R.string.empty)){
            binding.include4.textTitle.text=getString(R.string.empty_boxes)
            val location=intent.getStringExtra("locationData")?.fromJson<LocationsModel>()
            location?.let { locationData->
                val completeLocation="${locationData.address}, ${locationData.city}, ${locationData.district}, ${locationData.state}-${locationData.pincode}"
                binding.tvLocation.text = completeLocation
                binding.tvLocation.visibility= View.VISIBLE
            }
        } else if(action==getString(R.string.relocate)){
            binding.include4.textTitle.text=getString(R.string.relocate_boxes)
        }

        checkboxAllSelected = binding.checkboxAllSelect
        textViewTotalCount =binding.tvTotalCount
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = multipleSelectionAdapter

        multipleSelectionAdapter.setAdapterData(boxesList)
        binding.include4.imageViewBack.setOnClickListener {
            finish()
        }
        checkboxAllSelected?.let { checkbox->
            checkbox.setOnClickListener {
                allSelection(checkbox)
            }
        }

        binding.edtLocation.setOnClickListener {
            updatedLocationResult.launch(Intent(this, FarmListActivity::class.java).putExtra("locationData",intent.getStringExtra("locationData")))
        }
        binding.layoutRelocate.setOnClickListener {
            when{
                binding.edtLocation.text.toString().isNullOrEmpty() && binding.layoutLocation.visibility==VISIBLE->showToast(getString(R.string.please_select_location))
                multipleSelectionAdapter.selectedItems.isEmpty()->showToast(getString(R.string.please_select_barcode))
                else ->if(action==getString(R.string.relocate)) relocateBoxes() else {
//                    emptyBoxes()
                    barcodeRelocateScanedResult.launch(Intent(this,StoreHoneyActivity::class.java).putExtra("locationData",intent.getStringExtra("locationData")).putExtra("boxes",multipleSelectionAdapter.selectedItems.size).putExtra("ids",multipleSelectionAdapter.selectedItems.json()))

                }
            }
        }

        binding.imgScanQrCode.setOnClickListener {
            barcodeResult.launch(Intent(this, BarcodeScannerActivity::class.java))
        }

        binding.etSearchItem.addTextChangedListener(object : TextWatcher
        {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                multipleSelectionAdapter.setFilter(s.toString()); }
            override fun afterTextChanged(s: Editable?) {}
        })
    }


    private fun allSelection(checkbox: AppCompatCheckBox) {
        if (checkbox.isChecked) {
            boxesList?.let {
                multipleSelectionAdapter.selectAll()
                multipleSelectionAdapter.notifyDataSetChanged()
                textViewTotalCount?.text="${getString(R.string.total_scaned)} ${multipleSelectionAdapter.selectedItems.size}"
            }
        } else {
            boxesList?.let {
                multipleSelectionAdapter.clearAll()
                multipleSelectionAdapter.notifyDataSetChanged()
                textViewTotalCount?.text = "${getString(R.string.total_scaned)} 0"
            }
        }
    }
    private val updatedLocationResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode==Activity.RESULT_OK){
            val data: Intent? = result.data
             location=data?.getStringExtra("locationObject")?.fromJson()
            location?.let { locationData->
                var completeLocation="${locationData.address}, ${locationData.city}, ${locationData.district}, ${locationData.state}-${locationData.pincode}"
                binding.edtLocation.text = completeLocation
            }
        }
    }
    private fun relocateBoxes(){
        val pd=getProgress()
        val ids=multipleSelectionAdapter.selectedItems.map { it._id!! }
        Log.e("boxesIds", ids.json())
        boxesViewModel!!.relocateBoxBulk(ids,location?._id!!).observe(this){
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        showToast(it.data!!.message.toString())
                        val intent = Intent()
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
    private fun emptyBoxes(){
        var pd=getProgress()
        var ids=multipleSelectionAdapter.selectedItems.map { it._id!! }
        Log.e("boxesIds", ids.json())
        boxesViewModel!!.emptyBoxBulk(ids).observe(this){
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        showToast(it.data?.message!!)
                        val intent = Intent()
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
    private val barcodeRelocateScanedResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val intent = Intent()
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private val barcodeResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.etSearchItem.setText(it.data?.getStringExtra("barCode").toString())
        }
    }

    companion object{
        var textViewTotalCount: TextView?=null
        var checkboxAllSelected: AppCompatCheckBox?=null
    }
    override fun attachBaseContext(newBase: Context?) {
        val locale = Locale(MyApp.get().getLocale())
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase!!, locale)
        super.attachBaseContext(localeUpdatedContext)
    }
}