package com.bharuwa.sumadhu.ui.farm

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.ContextUtils
import com.bharuwa.sumadhu.constants.Util.fromListJson
import com.bharuwa.sumadhu.constants.Util.hideKeyboard
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.nameCat
import com.bharuwa.sumadhu.custom.clearSearch
import com.bharuwa.sumadhu.custom.searchValue
import com.bharuwa.sumadhu.network.model.Flora
import com.bharuwa.sumadhu.ui.adapter.FloraSelectionAdapter
import com.bharuwa.sumadhu.ui.dashboard.view.DashboardActivity
import kotlinx.android.synthetic.main.activity_flora_selection.*
import kotlinx.android.synthetic.main.titlebarlayout.*
import java.util.*
import javax.inject.Inject

class FloraSelectionActivity : AppCompatActivity() {

    @Inject
     lateinit var floraSelectionAdapter: FloraSelectionAdapter
    private var floraDataList: List<Flora>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flora_selection)
        textTitle.text = getString(R.string.select_flora)
        hideKeyboard()
        hideKeyboard()
        floraDataList = intent.getStringExtra("floraData")?.fromListJson()
        selectedItems=listOf<Flora>().toMutableList()
        floraSelectionAdapter = FloraSelectionAdapter().apply {
            floraDataList?.let {
                selectedItems.addAll(it)
            }
            onVisibilityCheck {
                emptyFloraList.visibility=if(itemCount==0)View.VISIBLE else View.GONE
                recyclerViewFlora.visibility=if(itemCount==0)View.GONE else View.VISIBLE
            }
        }
        imageViewBack.setOnClickListener {
            finish()
        }
        recyclerViewFlora.layoutManager = LinearLayoutManager(this)
        recyclerViewFlora.adapter = floraSelectionAdapter

        editTextSearchFlora.searchValue {
            floraSelectionAdapter.filter.filter(it)
        }
        editTextSearchFlora.clearSearch()

        layoutAddSelectedFlora.setOnClickListener {
            Log.e("selectedFlora", "onCreate: "+selectedItems.json())
            val intent = Intent()
            intent.putExtra("floraData", selectedItems.json())
            setResult(RESULT_OK, intent)
            finish()
        }

        getBeeAndFlora()
    }

    private fun getBeeAndFlora() {
        DashboardActivity.beeAndFlora?.let { list->
            setFlora(list.flora!!)
        }
    }

    private fun setFlora(list: List<Flora>?) {
        list?.let {
            val sortedList = it.sortedWith(compareBy { it.nameCat()})
           // Log.e("fdfdfds", "setFlora:== "+sd.json() )
            val offSeason = selectedItems.find { it.code == "F999" }
            floraSelectionAdapter.setAdapterData(sortedList, offSeason != null)
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val locale = Locale(MyApp.get().getLocale())
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase!!, locale)
        super.attachBaseContext(localeUpdatedContext)
    }
    companion object{
        var selectedItems = listOf<Flora>().toMutableList()
    }
}