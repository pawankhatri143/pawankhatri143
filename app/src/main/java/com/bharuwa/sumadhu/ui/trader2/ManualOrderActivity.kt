package com.bharuwa.sumadhu.ui.trader2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Util.fromJson
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.databinding.ActivityManualOrderBinding
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.ui.trader.activity.PurchaseCompleteActivity
import com.bharuwa.sumadhu.ui.trader.viewModel.BuyHoneyViewModel
import com.bharuwa.sumadhu.ui.trader2.adapter.ManualOrderAdapter
import com.bharuwa.sumadhu.ui.trader2.model.*
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ManualOrderActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityManualOrderBinding
    private var farmDetails: Result2Item? = null
    private var traderDetails: SearchTraderModel? = null
    private var buyFromTrader= false
    private val viewModel: BuyHoneyViewModel by viewModels()
    private var traderId: String?= null
    private var selectedOrderDetails: ResultItem12?= null
    @Inject
    lateinit var aAdapter: ManualOrderAdapter
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    var currentDate=  ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityManualOrderBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        currentDate = sdf.format(Date())

        _binding.include.textTitle.text = "Manual Order"
        _binding.include.imgBack.setOnClickListener { finish() }

        when{
            intent?.extras?.getBoolean("byFromTrader",false) == true ->  {
                intent?.extras?.getBoolean("byFromTrader",false)?.let {
                    buyFromTrader= it
                }
                intent?.extras?.getString("data")?.fromJson<SearchTraderModel>()?.let {
                    traderDetails= it
                }
                intent?.extras?.getString("traderId")?.let { traderId= it }
                intent?.extras?.getString("orderDetails")?.fromJson<ResultItem12>()?.let { selectedOrderDetails = it }
            }
            intent?.extras?.getBoolean("byFromTrader",false) == false ->{
                intent?.extras?.getString("data")?.fromJson<Result2Item>()?.let {
                    farmDetails= it
                }
            }
        }

        _binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ManualOrderActivity)
            adapter = aAdapter
        }
        aAdapter.setItemClick {
            try {
                val weigth=  aAdapter.list.map { it.manualWeigth.toFloat()}
                _binding.txtTotalWeigth.setText("${weigth.sum()}")
            }catch (e: Exception){
                Log.e("error", "error======: "+e.localizedMessage )
            }

        }


        _binding.btnProceed.setOnClickListener {

            if (_binding.txtTotalWeigth.text.toString().toFloat() > 1){

                var sum = 0F
                aAdapter.list.forEach {
                    sum += it.manualWeigth.toFloat()
                    if (sum > 1)
                        addBuckitOnList(it)
                }
                if (sum > 1){

                    if (buyFromTrader){

                        if ( _binding.txtTotalWeigth.text.toString().toFloat() > selectedOrderDetails?.remainHoneyWeight!!)
                            showToast("Total weight should be less than or equal to ${selectedOrderDetails?.remainHoneyWeight}")
                        else callPlaceApi(_binding.txtTotalWeigth.text.toString().toFloat())
                    }else callPlaceApi(_binding.txtTotalWeigth.text.toString().toFloat())
                } else showToast("Add bucket or drum count/ weight")
            }else showToast("Please add total weight")
        }

        getAllDispatchList()
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
                            aAdapter.setData(resource?.data.result as List<ContainerDetails>)

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

        var flora = "Till"
        if (!TraderAndFarmDetailsActivity.tempProfile?.flora.isNullOrEmpty())
            flora = TraderAndFarmDetailsActivity.tempProfile?.flora?.joinToString().toString()

        val bucket = BuckitDetailsModel(
            containerCode= details.containerId,
            containerFillStatus= "Fill",
            containerEmptyStatus= "Empty",
            containerType= "Bucket",
            containerFillFarmerId= if (farmDetails != null) farmDetails?.userId else traderId,
            containerFillFarmId= if (farmDetails != null) farmDetails?.farmId else traderId,
            containerHoneyWeight= details.manualWeigth.toDouble(),
            containerNetWeight= details.manualWeigth.toDouble().plus(details.emptyContainerWeight!!),
            emptyContainerWeight= details.emptyContainerWeight.toDouble(),
            containerAmount= details.containerCapacity!!.toDouble(),
            honeyPerKgAmount= "200".toDouble(),
            orderId= if (farmDetails == null) selectedOrderDetails?.orderId.toString() else "",
            traderId= if (traderId.isNullOrBlank()) "" else traderId,
            status= "1",
            containerFillDate= currentDate,
            containerEmptyDate= currentDate,
            purchaseBy= MyApp.get().getProfile()?._id,
            _id="",
            containerCount= details.numberOfBucket,
            actualHoneyTotalWeight= _binding.txtTotalWeigth.text.toString(),
            actualHoneyNetWeight= _binding.txtTotalWeigth.text.toString(),
            flora = flora
        )

        MyApp.get().buckitList.add(bucket)

     //   Log.e("dfsdsa", "addBuckitOnList: "+MyApp.get().buckitList.size)

    }
    private fun callPlaceApi(totalKg: Float) {

        val pd = getProgress()
        Log.e("fdfndfdf", "orderList=: " + MyApp.get().buckitList.json())
        viewModel.placeOrder(MyApp.get().buckitList).observe(this) {
            it?.let { resource ->
                pd.dismiss()
                when (resource.status) {
                    Status.SUCCESS -> {
                        if ( resource.data?.status== "ok"){
                            MyApp.get().buckitList.clear()
                            startActivity(
                                Intent( this, PurchaseCompleteActivity::class.java)
                                .putExtra("data",resource.data?.json())
                                .putExtra("honyWeigth",totalKg.toString())
                                .putExtra("flora",totalKg.toString())
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
}