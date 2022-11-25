package com.bharuwa.sumadhu.ui.farm

import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.ContextUtils
import com.bharuwa.sumadhu.constants.Util.fromJson
import com.bharuwa.sumadhu.constants.Util.fromListJson
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.getProgress2
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.databinding.ActivityStoreHoneyBinding
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.network.model.BoxesModel
import com.bharuwa.sumadhu.network.model.LocationsModel
import com.bharuwa.sumadhu.network.model.StoreHoneyModel
import com.bharuwa.sumadhu.ui.adapter.MyBoxesAdapter
import com.bharuwa.sumadhu.ui.dashboard.view.InputFilterMinMax
import com.bharuwa.sumadhu.ui.dialogs.EnterPriceDialog
import com.bharuwa.sumadhu.ui.dialogs.HoneyStoredDialog
import com.bharuwa.sumadhu.ui.trader.viewModel.BuyHoneyViewModel
import com.bharuwa.sumadhu.ui.trader2.adapter.ManualOrderAdapter
import com.bharuwa.sumadhu.ui.trader2.model.ContainerDetails
import com.bharuwa.sumadhu.ui.viewmodel.BoxesViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class StoreHoneyActivity : AppCompatActivity(){
    private lateinit var myfarmAdapter: MyBoxesAdapter
    private var _binding: ActivityStoreHoneyBinding?=null
    private val binding get() = _binding!!
//    private val viewModel: BuyHoneyViewModel by viewModels()
//    private val viewModel: BuyHoneyViewModel by viewModels()
    private val viewModel: BuyHoneyViewModel by viewModels()
    @Inject
    lateinit var aAdapter: ManualOrderAdapter
    var buckitList= mutableListOf<StoreHoneyModel>()
    private var farmLocation: LocationsModel?=null
    private val boxesViewModel: BoxesViewModel by viewModels()
    var selectedItems = listOf<BoxesModel>().toMutableList()
    var pd: Dialog?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding=ActivityStoreHoneyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.include4.textTitle.text=getString(R.string.store_honey)
        myfarmAdapter = MyBoxesAdapter().apply {
            setItemClick {
            }
        }
        pd=getProgress2()
        farmLocation=intent.getStringExtra("locationData")?.fromJson<LocationsModel>()

        var numberOfBoxes=intent.getIntExtra("boxes",0)
        farmLocation?.let {
            binding.tvFarmName.text=it.name
            val completeLocation="${it.address}, ${it.city}, ${it.district}, ${it.state}-${it.pincode}"
            binding.tvLocation.text = completeLocation
        }

        numberOfBoxes?.let {
            binding.txtBoxCount.text="$numberOfBoxes"
            binding.txtMaximumYields.text="${numberOfBoxes*6 }" //  6 is container weight
            binding.edtActualWeightOfHoney.filters = arrayOf<InputFilter>(InputFilterMinMax(1, numberOfBoxes*6))
            if(MyApp.get().getLanguage()=="English")
            binding.txtNote.text="(${getString(R.string.note)} ${numberOfBoxes * 6} ${getString(R.string.kg)})"
            else  binding.txtNote.text="(${getString(R.string.you)} ${numberOfBoxes * 6} ${getString(R.string.kg)} ${getString(R.string.note)})"
        }
        binding.edtActualWeightOfHoney.doAfterTextChanged {
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@StoreHoneyActivity)
            adapter = aAdapter
        }
        aAdapter.setItemClick {
            try {
                val weigth=  aAdapter.list.map { it.numberOfBucket.toInt()}
                binding.txtTotalBoxCount.text = "${weigth.sum()}"
            }catch (e: Exception){
                Log.e("error", "error======: "+e.localizedMessage )
            }

        }

        binding.include4.imageViewBack.setOnClickListener {
            onBackPressed()
        }
        getAllDispatchList()
        binding.layoutProceed.setOnClickListener {
            buckitList.clear()
            when{
                binding.edtActualWeightOfHoney.text.toString().isNullOrEmpty()->showToast("Please enter honey weight")
                binding.txtTotalBoxCount.text.toString().toInt() < 1-> showToast("Please add total weight")
                else->{
                    var sum = 0F
                    aAdapter.list.forEach {
                        sum += it.manualWeigth.toFloat()
                        if (it.manualWeigth.toFloat() > 1)
                            addBuckitOnList(it)
                    }
                    if (sum > 1)
                        storeHoney()
                    else showToast("Add bucket or drum count/ weight")
                }
            }

           /* if (binding.txtTotalWeigth.text.toString().toFloat() > 1){
                var sum = 0F
                aAdapter.list.forEach {
                    sum += it.manualWeigth.toFloat()
                    if (it.manualWeigth.toFloat() > 1)
                        addBuckitOnList(it)
                }
                if (sum > 1)
                    callPlaceApi(binding.txtTotalWeigth.text.toString().toFloat())
                else showToast("Add bucket or drum count/ weight")
            }else showToast("Please add total weight")*/

            // Log.e("TotalWeight","Total weight is: $sum")

        }
    }
    private fun getAllDispatchList() {

        val pd = getProgress()

        viewModel.getContainerList().observe(this) {
            it?.let { resource ->
                Log.e("fdfndfdf", "vendorList: " + resource.data?.json())

                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()

                        if (resource.data?.result?.isNotEmpty() == true){
                            Log.e("fdfndfdf", "vendorList: setVender" )
                            aAdapter.setData(resource?.data.result as List<ContainerDetails>,true)

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
    private fun addBuckitOnList(details: ContainerDetails) {

        val bucket = StoreHoneyModel(
            actualHoneyTotalWeight= binding.edtActualWeightOfHoney.text.toString(),
            actualHoneyNetWeight= binding.edtActualWeightOfHoney.text.toString(),
            containerCode= details.containerId,
            containerCount= details.numberOfBucket,
            containerFillFarmId=farmLocation?._id,
            containerFillFarmerId= farmLocation?.userId,
            containerHoneyWeight= details.manualWeigth.toDouble(),
            containerNetWeight= details.manualWeigth.toDouble().plus(details.emptyContainerWeight!!),
            containerType= details.containerType,
            flora = farmLocation?.flora?.joinToString { it }
        )

        buckitList.add(bucket)

        Log.e("dfsdsa", "addBuckitOnList: "+buckitList.size)

    }
    private fun storeHoney() {

//        val pd = getProgress()
        pd?.show()
        Log.e("orderList=: ", buckitList.json())
        viewModel.storeHoney(buckitList).observe(this) {
            it?.let { resource ->
//                pd.dismiss()
                when (resource.status) {
                    Status.SUCCESS -> {
                        if ( resource.data?.status== "ok"){
                            buckitList.clear()
                            emptyBoxes()
//                            showToast(it.data?.message.toString())
                        }
                    }
                    Status.ERROR -> {
                        pd?.dismiss()
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
    private fun emptyBoxes(){
//        var pd=getProgress()
        selectedItems=intent.getStringExtra("ids")!!.fromListJson()
        var ids=selectedItems.map { it._id!! }
        Log.e("boxesIds", ids!!.json())
        boxesViewModel!!.emptyBoxBulk(ids).observe(this){
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd?.dismiss()
                       /* showToast(it.data?.message!!)
                        val intent = Intent()
                        setResult(RESULT_OK, intent)
                        finish()*/
                        storeHoneyDialog()
                    }
                    Status.ERROR -> {
                        pd?.dismiss()
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
    private fun storeHoneyDialog() {

        val dialog = HoneyStoredDialog(this)
        dialog.setCancelable(false)
        dialog.setDialogDismissListener {
            val intent = Intent()
            setResult(RESULT_OK, intent)
            finish()

        }
        dialog.show()

    }

    override fun attachBaseContext(newBase: Context?) {
        val locale = Locale(MyApp.get().getLocale())
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase!!, locale)
        super.attachBaseContext(localeUpdatedContext)
    }
}


