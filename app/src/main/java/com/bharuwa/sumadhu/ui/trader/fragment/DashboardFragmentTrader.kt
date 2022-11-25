package com.bharuwa.sumadhu.ui.trader.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.constants.Permissions
import com.bharuwa.sumadhu.databinding.ActivityTraderDeshboardBinding
import com.bharuwa.sumadhu.databinding.FragmentDashboardBinding
import com.bharuwa.sumadhu.databinding.FragmentDashboardTraderBinding
import com.bharuwa.sumadhu.ui.trader.activity.BuyHoneyFromFarmerActivity
import com.bharuwa.sumadhu.ui.trader.activity.MyDrumActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragmentTrader: Fragment() {

    private lateinit var _binding: FragmentDashboardTraderBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

         _binding = FragmentDashboardTraderBinding.inflate(layoutInflater)
        return _binding.root
        //inflater.inflate(R.layout.fragment_dashboard_trader, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.layoutManageDrum.setOnClickListener {
            if (Permissions.checkCameraPermission(requireActivity()))
            startActivity(Intent(requireActivity(), MyDrumActivity::class.java))
        }

        _binding.layoutBuyHoney.setOnClickListener {
            if (Permissions.checkCameraPermission(requireActivity())) {
                startActivity(Intent(requireActivity(), BuyHoneyFromFarmerActivity::class.java)) }
        }

    }


}