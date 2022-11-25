package com.bharuwa.sumadhu.ui.trader.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.databinding.ActivityMyDrumBinding
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.ui.trader.adapter.SectionsPagerAdapter
import com.bharuwa.sumadhu.ui.trader.viewModel.AddDrumViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_my_drum.*


@AndroidEntryPoint
class MyDrumActivity : AppCompatActivity() {

    private val TAG = "MyDrum"
    private lateinit var _binding: ActivityMyDrumBinding
    private val addDrumViewModel: AddDrumViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMyDrumBinding.inflate(layoutInflater)
        setContentView(_binding.root)
//        setContentView(R.layout.activity_my_drum)

        _binding.include.textTitle.text = "My Drum"
        setTabs()

        /*MyApp.get().getProfile()?.let {
            getMyDrumList(it._id.toString())
        }*/
       
    }

    private fun setTabs() {
        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        _binding.viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)
    }

    fun back(view: View) {
        finish()
    }

    /*private fun getMyDrumList(userID: String) {


        val pd = getProgress()
       
        Log.e(TAG, "userID=$userID")
        addDrumViewModel.getTraderAllDrumList("62541b2c450348129223297a").observe(this) {
            it?.let { resource ->
                Log.e(TAG, "getMyDrumList: ==")
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        if (resource.data!=null){
                            Log.e(TAG, "getMyDrumList: "+resource.data.result?.json() )

                            updateAdapter(resource.data.result)
                        }

                    }
                    Status.ERROR -> {
                        pd.dismiss()
                        if (it.message == "internet") {
                            showAlert(getString(R.string.network_error),getString(R.string.no_internet))
                        } else {
                            showAlert(getString(R.string.error), it?.message.toString())
                        }
                    }
                    else -> { pd.dismiss() }
                }
            }

        }
    }*/


    }


