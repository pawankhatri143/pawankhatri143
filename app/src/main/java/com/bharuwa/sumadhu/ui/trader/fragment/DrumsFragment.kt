package com.bharuwa.sumadhu.ui.trader.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.databinding.FragmentDrumBinding
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.ui.dialogs.EnterPriceDialog
import com.bharuwa.sumadhu.ui.trader.activity.BuyHoneyFromFarmerActivity.Companion.farmerFarm
import com.bharuwa.sumadhu.ui.trader.activity.PurchaseCompleteActivity
import com.bharuwa.sumadhu.ui.trader.adapter.DemoAdapter3
import com.bharuwa.sumadhu.ui.trader.model.DrumDetailsModel
import com.bharuwa.sumadhu.ui.trader.viewModel.AddDrumViewModel
import com.bharuwa.sumadhu.ui.trader.viewModel.BuyHoneyViewModel
import com.bharuwa.sumadhu.ui.trader.viewModel.DrumItem
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


@AndroidEntryPoint
class DrumsFragment : Fragment() {

    @Inject
    lateinit var drumsAdapter: DemoAdapter3
    private val buyHoneyViewModel: BuyHoneyViewModel by viewModels()
    private lateinit var _binding: FragmentDrumBinding
    private val addDrumViewModel: AddDrumViewModel by viewModels()
    private val TAG = "DrumsFragmentBaba"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentDrumBinding.inflate(layoutInflater)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.recyclerViewDrums.apply {

            layoutManager = LinearLayoutManager(requireActivity())
            adapter = drumsAdapter
            drumsAdapter.setItemClick {
                Log.e(TAG, "setItemClick: $it")
                _binding.txtTotalDrumCapacity.setText("${it} Kg")
            }
        }

        _binding.btnConfirm.setOnClickListener {
            if (drumsAdapter.getTotalDrumCatacityCount() > 0) {
//                callPlaceApi(price)
                dialogForPrice()
//

            } else requireActivity().showToast("Please select any drum")
        }

        _binding.btnCancel.setOnClickListener {
            requireActivity().finish()
        }

        MyApp.get().getProfile()?.let {
            getMyDrumList(it._id.toString())
        }
    }

    companion object {
        val orderList: MutableList<DrumDetailsModel> = ArrayList()
        private val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())
    }
   /* private fun callPlaceApi() {

        val pd = requireActivity().getProgress()
     Log.e("fdfndfdf", "orderList=: " + orderList.json())
        buyHoneyViewModel.placeOrder(orderList).observe(requireActivity()) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        startActivity(Intent( requireActivity(), PurchaseCompleteActivity::class.java)
                            .putExtra("data",resource.data?.json()))
                        Log.e("fdfndfdf", "getFarmDetails: " + resource.data?.json())
                    }
                    Status.ERROR -> {
                        pd.dismiss()
                        if (it.message == "internet") {
                            requireActivity().showAlert(
                                getString(R.string.network_error),
                                getString(R.string.no_internet)
                            )
                        } else {
                            requireActivity().showAlert(
                                getString(R.string.error),
                                it?.message.toString()
                            )
                        }
                    }
                }
            }

        }
    }*/

    private fun dialogForPrice() {

        val dialog = EnterPriceDialog(requireActivity())
        dialog.setItemClick{ price ->
            if (!price.isNullOrBlank()){
                if (price.toFloat() > 0) calculationValidation(price.toFloat())
            }

        }
        dialog.show()

    }

    private fun calculationValidation(price: Float) {

        orderList.forEach {
            it.drumAmount = it.drumHoneyWeight?.times(price)
            it.honeyPerKgAmount = price
            it.drumFillFarmId = farmerFarm?._id.toString()
            it.drumFillFarmerId = farmerFarm?.userId.toString()
        }
      //  callPlaceApi()
    }


    private fun getMyDrumList(userID: String) {


        val pd = requireActivity().getProgress()

        Log.e(TAG, "userID=$userID")
        addDrumViewModel.getTraderAllDrumList(userID).observe(requireActivity()) {
            it?.let { resource ->
                Log.e(TAG, "getMyDrumList: ==")
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        if (resource.data!=null && !resource.data.result.isNullOrEmpty()){
                            Log.e(TAG, "getMyDrumList: "+resource.data.result?.json() )

                            drumsAdapter.setData(resource.data.result as List<DrumItem>)
                        }

                    }
                    Status.ERROR -> {
                        pd.dismiss()
                        if (it.message == "internet") {
                            requireActivity().showAlert(getString(R.string.network_error),getString(R.string.no_internet))
                        } else {
                            requireActivity().showAlert(getString(R.string.error), it?.message.toString())
                        }
                    }
                    else -> { pd.dismiss() }
                }
            }

        }
    }
}