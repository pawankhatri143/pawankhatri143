package com.bharuwa.sumadhu.ui.main

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.ContextUtils
import com.bharuwa.sumadhu.constants.Util.hideKeyboard
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.custom.clearSearch
import com.bharuwa.sumadhu.custom.searchValue
import com.bharuwa.sumadhu.network.config.ApiRequest
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.network.model.CityStateVillageModel
import com.bharuwa.sumadhu.ui.adapter.CityStateAdapter
import com.bharuwa.sumadhu.ui.viewmodel.CityStateViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_city_state.*
import kotlinx.android.synthetic.main.titlebarlayout.*
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CityStateActivity : AppCompatActivity() {
    private val cityStateViewModel by viewModels<CityStateViewModel>()
    @Inject lateinit var cityStateAdapter: CityStateAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_state)
        textTitle.text = getString(R.string.app_name)
        hideKeyboard()

        cityStateAdapter.setItemClick {
            val intent = Intent()
            intent.putExtra("data", it.json())
            setResult(RESULT_OK, intent)
            finish()
        }

        cityStateAdapter.onVisibilityCheck {
            emptyVillageList.visibility=if(cityStateAdapter.itemCount==0)View.VISIBLE else View.GONE
            recyclerViewVillage.visibility=if(cityStateAdapter.itemCount==0)View.GONE else View.VISIBLE
        }

        recyclerViewVillage.layoutManager = LinearLayoutManager(this)
        recyclerViewVillage.adapter = cityStateAdapter

        imageViewBack.setOnClickListener {
            finish()
        }

        editTextSearchVillage.searchValue {
            cityStateAdapter.filter.filter(it)
        }

        editTextSearchVillage.clearSearch()

        initApi()

        swipeRefresh.setOnRefreshListener {
            initApi()
        }

    }

    private fun initApi(){
        when(intent.getStringExtra("type")){
            "state" -> {
                textTitle.text = "Select State"
                getCityState (cityStateViewModel.getState())
            }
            "district" -> {
                textTitle.text = "Select District"
                getCityState(cityStateViewModel.getDistrict(intent.getStringExtra("id").toString()))
            }
            "city" -> {
                textTitle.text = "Select City"
                getCityState(cityStateViewModel.getCity(intent.getStringExtra("id").toString()))
            }
        }
    }

    private fun getCityState(data:LiveData<ApiRequest<List<CityStateVillageModel>>>) {
        swipeRefresh.isRefreshing = true
        data.observe(this){ resource->
                resource?.let {
                    when (resource.status) {
                        Status.SUCCESS -> {
                            swipeRefresh.isRefreshing = false
                            if (!it.data.isNullOrEmpty())   cityStateAdapter.setVillageData(it.data.sortedBy { it.name })
                           else cityStateAdapter.setVillageData(it.data!!)
                        }
                        Status.ERROR -> {
                            swipeRefresh.isRefreshing = false
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


    override fun attachBaseContext(newBase: Context?) {
        val locale = Locale(MyApp.get().getLocale())
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase!!, locale)
        super.attachBaseContext(localeUpdatedContext)
    }
}