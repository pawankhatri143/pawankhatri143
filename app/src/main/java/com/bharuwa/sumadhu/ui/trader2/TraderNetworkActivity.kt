package com.bharuwa.sumadhu.ui.trader2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.databinding.ActivityTraderDeshboardBinding
import com.bharuwa.sumadhu.databinding.ActivityTraderNetworkBinding
import com.bharuwa.sumadhu.ui.trader.adapter.DemoAdapter
import kotlinx.android.synthetic.main.fragment_network.*

class TraderNetworkActivity : AppCompatActivity() {

    private val demoAdapter = DemoAdapter()
    private lateinit var _binding: ActivityTraderNetworkBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTraderNetworkBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.include.textTitle.text = "My Network"
        _binding.include.imgBack.setOnClickListener { finish() }

        _binding.recyclerViewNetworkList.layoutManager = LinearLayoutManager(this)
        _binding.recyclerViewNetworkList.adapter = demoAdapter

        _binding.btnAddNewShopKeeper.setOnClickListener {
            showToast("work on progress")
        }
    }
}