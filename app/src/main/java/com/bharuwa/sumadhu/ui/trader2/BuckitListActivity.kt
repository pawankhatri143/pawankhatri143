package com.bharuwa.sumadhu.ui.trader2

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.databinding.ActivityBuckitListBinding
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.ui.trader.activity.AddDrumActivity
import com.bharuwa.sumadhu.ui.trader.activity.PurchaseCompleteActivity
import com.bharuwa.sumadhu.ui.trader.fragment.DrumsFragment
import com.bharuwa.sumadhu.ui.trader.viewModel.BuyHoneyViewModel
import com.bharuwa.sumadhu.ui.trader2.adapter.BuckitItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BuckitListActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var _binding: ActivityBuckitListBinding
    private val buyHoneyViewModel: BuyHoneyViewModel by viewModels()
    @Inject
    lateinit var buckitItemAdapter: BuckitItemAdapter

    private var totalKg= 0.0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_buckit_list)
        _binding = ActivityBuckitListBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.include.textTitle.text = "Bucket List"
        _binding.include.imgBack.setOnClickListener { finish() }
       // MyApp.get().buckitList.size
        _binding.recyclerViewDrums.apply {

            layoutManager = LinearLayoutManager(this@BuckitListActivity)
            adapter = buckitItemAdapter
            buckitItemAdapter.setData(MyApp.get().buckitList)
            buckitItemAdapter.setItemClick {
//                _binding.txtTotalDrumCapacity.setText("${it} Kg")
            }
        }

        if(MyApp.get().buckitList.isNotEmpty()){

           /* MyApp.get().buckitList.forEach {
               totalKg+= it.containerHoneyWeight!!
            }*/
            val mapWeigth=  MyApp.get().buckitList.map { it.containerHoneyWeight!!}
            totalKg = mapWeigth.sum()
            MyApp.get().buckitList.forEach {
                it.actualHoneyTotalWeight= totalKg.toString()
                it.actualHoneyNetWeight= totalKg.toString()
            }

            _binding.txtTotalDrumCapacity.text= "$totalKg Kg"
        }

        _binding.btnConfirm.setOnClickListener(this)
        _binding.btnScan.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            _binding.btnConfirm -> callPlaceApi()
            _binding.btnScan -> {
                MyApp.get().buckitList.clear()
                finish()
            }//startActivity(Intent(this, AddDrumActivity::class.java))
        }
    }


    private fun callPlaceApi() {

        val pd = getProgress()
        Log.e("fdfndfdf", "orderList=: " + MyApp.get().buckitList.json())
        buyHoneyViewModel.placeOrder(MyApp.get().buckitList).observe(this) {
            it?.let { resource ->
                pd.dismiss()
                when (resource.status) {
                    Status.SUCCESS -> {
                       if ( resource.data?.status== "ok"){
                           MyApp.get().buckitList.clear()
                           startActivity(Intent( this, PurchaseCompleteActivity::class.java)
                               .putExtra("data",resource.data?.json())
                               .putExtra("honyWeigth",totalKg.toString()))
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