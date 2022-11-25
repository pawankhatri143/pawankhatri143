package com.bharuwa.sumadhu.ui.trader2

import android.content.Intent
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
import com.bharuwa.sumadhu.databinding.ActivityAddDispatchBinding
import com.bharuwa.sumadhu.databinding.ActivityMyDispatchBinding
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.ui.trader.viewModel.BuyHoneyViewModel
import com.bharuwa.sumadhu.ui.trader2.adapter.AddDispatchOrderAdapter
import com.bharuwa.sumadhu.ui.trader2.adapter.MyDispatchOrderAdapter
import com.bharuwa.sumadhu.ui.trader2.model.DispatchedOrder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyDispatchActivity : AppCompatActivity() {

    @Inject
    lateinit var aAdapter: MyDispatchOrderAdapter
    private val viewModel: BuyHoneyViewModel by viewModels()
    private lateinit var _binding: ActivityMyDispatchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMyDispatchBinding.inflate(layoutInflater)
        setContentView(_binding.root)
       // setContentView(R.layout.activity_my_dispatch)

        _binding.include.textTitle.text = "My Dispatches"
        _binding.include.imgBack.setOnClickListener { finish() }

        getAllDispatchList()
        _binding.recyclerView.apply {

            layoutManager = LinearLayoutManager(this@MyDispatchActivity)
            adapter = aAdapter

        }
        _binding.fabAddDispatch.setOnClickListener {
            startActivity(Intent(this,AddDispatchActivity::class.java))
        }
        getAllDispatchList()
    }

    private fun getAllDispatchList() {

        val pd = getProgress()

        val traderID = MyApp.get().getProfile()?._id.toString()
        viewModel.getTraderDispatchOrderList(traderID).observe(this) {
            it?.let { resource ->
                Log.e("fdfndfdf", "vendorList: " + resource.data?.json())

                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()

                        if (resource.data?.result?.isNotEmpty() == true){
                            Log.e("fdfndfdf", "vendorList: setVender" )
                            aAdapter.setData(resource?.data.result as List<DispatchedOrder>)

                        }



                    }
                    Status.ERROR -> {
                        pd.dismiss()
                        if (it.message == "internet") {
                            showAlert(
                                getString(R.string.network_error),
                                getString(R.string.no_internet)
                            )
                        }/* else {
                            showAlert(
                                getString(R.string.error),
                                it?.message.toString()
                            )
                        }*/
                    }

                    else -> {}
                }
            }

        }
    }
}