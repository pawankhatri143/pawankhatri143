package com.bharuwa.sumadhu.ui.trader.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.ui.trader.adapter.DemoAdapter
import kotlinx.android.synthetic.main.fragment_network.*

class NetworkFragment : Fragment() {
    val demoAdapter = DemoAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?, ): View? {
        return inflater.inflate(R.layout.fragment_network, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewNetworkList.layoutManager = LinearLayoutManager(requireActivity())
        recyclerViewNetworkList.adapter = demoAdapter

        btnAddBeeKeeper.setOnClickListener {  requireActivity().showToast("work on progress") }

    }

}