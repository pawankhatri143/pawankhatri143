package com.bharuwa.sumadhu.ui.trader2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Util.currentUserTrader
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.constants.Util.userTypeBoth
import com.bharuwa.sumadhu.databinding.FragmentDashboardTrader2Binding
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.ui.dashboard.view.DashboardActivity
import com.bharuwa.sumadhu.ui.viewmodel.BlendingViewModel
import com.bharuwa.sumadhu.ui.viewmodel.TraderDashboardViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DashboardFragmentTrader2 : Fragment() {
    private lateinit var _binding: FragmentDashboardTrader2Binding
    private val viewModel by viewModels<TraderDashboardViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDashboardTrader2Binding.inflate(layoutInflater)
        return _binding.root
//              return inflater.inflate(R.layout.fragment_dashboard_trader2, container, false)
    }

    override fun onResume() {
        super.onResume()
        getTraderDashboardCount()
        if (currentUserTrader){
            _binding.switch1.isChecked = false
            _binding.txtTrader.setTextColor(resources.getColor(R.color.black))
            _binding.txtBeeKeeper.setTextColor(resources.getColor(R.color.textColor_dim))
            _binding.switch1.setBackColorRes(R.color.skin_color)
            currentUserTrader= false
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (MyApp.get().getProfile()?.userType?.contains(",") ==true){
            _binding.llSwitchLayout.visibility= View.VISIBLE
        }else  _binding.llSwitchLayout.visibility= View.GONE
        _binding.switch1.setOnClickListener {
            if (_binding.switch1.isChecked){
                //now beekeeper is active
                _binding.txtTrader.setTextColor(resources.getColor(R.color.textColor_dim))
                _binding.txtBeeKeeper.setTextColor(resources.getColor(R.color.black))
                _binding.switch1.setBackColorRes(R.color.login_text_color)
                userTypeBoth = true
                startActivity(Intent(requireActivity(), DashboardActivity::class.java)
                    .putExtra("comeFrom",true))
            }else{
                //now trader is active
                _binding.txtTrader.setTextColor(resources.getColor(R.color.black))
                _binding.txtBeeKeeper.setTextColor(resources.getColor(R.color.textColor_dim))
                _binding.switch1.setBackColorRes(R.color.skin_color)

            }

        }


        _binding.layoutBuyHoney.setOnClickListener {
         //   if (Permissions.checkCameraPermission(requireActivity()))
                startActivity(Intent(requireActivity(), FindUserDetailsActivity::class.java).
                putExtra("userType", _binding.switch1.isChecked))
              //  startActivity(Intent(requireActivity(), MyDrumActivity::class.java))

        }
        _binding.layoutMyHoney.setOnClickListener {
            startActivity(Intent(requireActivity(), MyOrdersActivity::class.java))
            //   startActivity(Intent(requireActivity(), BuyHoneyFromFarmerActivity::class.java)) }
        }
        _binding.layoutMyNetwork.setOnClickListener { startActivity(Intent(requireActivity(), TraderNetworkActivity::class.java)) }
        _binding.layoutDispatch.setOnClickListener { startActivity(Intent(requireActivity(), MyDispatchActivity::class.java)) }
        _binding.layoutLoadDispatch.setOnClickListener { startActivity(Intent(requireActivity(), LoadDispatchActivity::class.java)) }
    }


    private fun getTraderDashboardCount() {
        val pd = requireActivity().getProgress()

        val inwardId = MyApp.get().getProfile()?._id.toString()
        viewModel.getTraderDashboardCount(inwardId).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        Log.e("TraderDashboard", "getTraderDashboardCount: "+it.data?.json() )
                        if (it.data!!.status == "ok") {
                            it.data.result?.let {
                               _binding.txtTotalBeekeepers.text= "${it.farmerCount}\nBeekeepers"
                               _binding.txtTotalOrder.text= "${it.orderCount}\nOrders"
                               _binding.txtOrderSupplied.text= "${it.dispatchOrders} Order\nSupplied"
                            }
                        }
                    }
                    Status.ERROR -> {
                        pd.dismiss()

                    }
                    else -> {}
                }
            }
        }
    }

}