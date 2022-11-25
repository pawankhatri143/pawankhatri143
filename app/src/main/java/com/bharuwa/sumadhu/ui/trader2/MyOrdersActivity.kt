package com.bharuwa.sumadhu.ui.trader2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.databinding.ActivityMyOrdersBinding
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.ui.trader.viewModel.BuyHoneyViewModel
import com.bharuwa.sumadhu.ui.trader2.adapter.MyOrderAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_buy_honey_from_farmer.*
import javax.inject.Inject


@AndroidEntryPoint
class MyOrdersActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMyOrdersBinding
    @Inject
    lateinit var aAdapter: MyOrderAdapter
    private val viewModel : BuyHoneyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_my_orders)
        _binding = ActivityMyOrdersBinding.inflate(layoutInflater)
        setContentView(_binding.root)

 
        _binding.include.textTitle.text = "My Orders"
        _binding.include.imgBack.setOnClickListener { finish() }

        _binding.recyclerView.apply {
            layoutManager= LinearLayoutManager(this@MyOrdersActivity, LinearLayoutManager.VERTICAL, false)
            recyclerView.adapter = aAdapter

//            aAdapter.setData(MyApp.get().buckitList)
            aAdapter.setItemClick {
//                _binding.txtTotalDrumCapacity.setText("${it} Kg")
            }
        }

        MyApp.get().getProfile()?._id?.let {
            getMyOrder(it)
        }

    }


    private fun getMyOrder(userId: String) {
        val pd = getProgress()

        viewModel.getMyAllOrder(userId).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        aAdapter.setData(resource.data?.result?.sortedByDescending { it?.orderDate })
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
}