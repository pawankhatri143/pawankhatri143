package com.bharuwa.sumadhu.ui.dashboard.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Permissions.checkCameraPermission
import com.bharuwa.sumadhu.constants.Util
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.databinding.FragmentDashboardNewBinding
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.network.model.DashboardModel
import com.bharuwa.sumadhu.ui.adapter.HomePageAdapter
import com.bharuwa.sumadhu.ui.dashboard.view.DashboardActivity
import com.bharuwa.sumadhu.ui.dashboard.view.DashboardActivity.Companion.beeAndFlora
import com.bharuwa.sumadhu.ui.dialogs.ConfirmAlertDialog
import com.bharuwa.sumadhu.ui.farm.*
import com.bharuwa.sumadhu.ui.trader.activity.TraderDeshboardActivity
import com.bharuwa.sumadhu.ui.trader2.DashboardFragmentTrader2
import com.bharuwa.sumadhu.ui.viewmodel.BeeViewModel
import com.bharuwa.sumadhu.ui.viewmodel.BoxesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardNewBinding? = null
    private val binding get() = _binding!!
    private val viewModelBox by viewModels<BoxesViewModel>()
    private val viewModelBee by viewModels<BeeViewModel>()
    @Inject lateinit var homePageAdapter: HomePageAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardNewBinding.inflate(inflater, container, false)
        val root: View = binding.root
       // getBeeAndFlora()
        getFlora()
        getBee()

        if (MyApp.get().getProfile()?.userType?.contains(",") ==true){
            _binding!!.llSwitchLayout.visibility= View.VISIBLE
        }else  _binding!!.llSwitchLayout.visibility= View.GONE


            _binding?.switch1?.isChecked= true
            _binding?.txtTrader?.setTextColor(resources.getColor(R.color.textColor_dim))
            _binding?.txtBeeKeeper?.setTextColor(resources.getColor(R.color.black))
            _binding?.switch1?.setBackColorRes(R.color.login_text_color)

            _binding?.switch1?.setOnClickListener {
                if (_binding?.switch1?.isChecked == true){
                    //now beekeeper is active
                    _binding?.txtTrader?.setTextColor(resources.getColor(R.color.textColor_dim))
                    _binding?.txtBeeKeeper?.setTextColor(resources.getColor(R.color.black))
                    _binding?.switch1?.setBackColorRes(R.color.login_text_color)
                   /* startActivity(Intent(requireActivity(), DashboardActivity::class.java)
                        .putExtra("comeFrom",true))*/
                }else{
                    //now trader is active
                    _binding?.txtTrader?.setTextColor(resources.getColor(R.color.black))
                    _binding?.txtBeeKeeper?.setTextColor(resources.getColor(R.color.textColor_dim))
                    _binding?.switch1?.setBackColorRes(R.color.yellow)
                    Util.currentUserTrader = true
                    startActivity(Intent(requireActivity(), TraderDeshboardActivity::class.java))
                    requireActivity().finish()
                }

            }

        homePageAdapter.apply {
            setItemClick { data,action->
                when(action){
                    "view"->{ if (checkCameraPermission(requireActivity()))
                        startActivity(Intent(requireActivity(), FarmDetailsActivity::class.java).putExtra("data", data.json()))
                    }
                    "relocate"->{
                        ConfirmAlertDialog(
                            requireActivity(),getString(R.string.are_you_sure),getString(R.string.do_you_want_to_relocate),"Relocate"
                        ).apply {
                            setDialogDismissListener {
                                barcodeRelocateScanedResult.launch(Intent(requireActivity(),
                                    FarmRelocationActivity::class.java)
                                    .putExtra("formAction",getString(R.string.relocate)).putExtra("boxes",data.emptyBoxes?.json()).putExtra("locationData",data.farm?.json()))
                            }
                        }.show()
                    }
                    "empty"->{
                        ConfirmAlertDialog(
                            requireActivity(),getString(R.string.are_you_sure),getString(R.string.do_you_want_to_empty),"Empty"
                        ).apply {
                            setDialogDismissListener {
                                barcodeRelocateScanedResult.launch(Intent(requireActivity(),
                                    FarmRelocationActivity::class.java)
                                    .putExtra("formAction",getString(R.string.empty)).putExtra("locationData",data.farm?.json()).putExtra("boxes",data.boxes?.json()))
                            }
                        }.show()
                    }
                    "map"->startActivity(Intent(requireActivity(), FarmOnMapActivity::class.java).putExtra("location",data.farm!!.json()))
                    "addMore"-> addScanBoxResult.launch(Intent(requireContext(), FarmSetupActivity::class.java).putExtra("from","Add More").putExtra("locationObject",data.farm?.json()))
                }
            }
        }

        binding.layoutBoxes.recycleView.layoutManager =LinearLayoutManager(requireActivity())
        binding.layoutBoxes.recycleView.adapter = homePageAdapter

        binding.layoutBoxes.swipeRefresh.setOnRefreshListener {
            getBoxes()
            binding.layoutBoxes.swipeRefresh.isRefreshing = false
        }
         binding.layoutStart.layoutContinue.setOnClickListener {
                updatedLocationResult.launch(Intent(requireContext(), FarmListActivity::class.java))
        }
        binding.layoutBoxes.layoutAddNew.setOnClickListener {
            updatedLocationResult.launch(Intent(requireContext(), FarmListActivity::class.java))
        }
        return root
    }

    override fun onResume() {
        super.onResume()
        _binding?.switch1?.isChecked = true
        _binding?.txtTrader?.setTextColor(resources.getColor(R.color.textColor_dim))
        _binding?.txtBeeKeeper?.setTextColor(resources.getColor(R.color.black))
        _binding?.switch1?.setBackColorRes(R.color.login_text_color)
    }

    private fun getFlora() {
        val pd = requireActivity().getProgress()
        viewModelBee.getFloraList().observe(requireActivity()) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        beeAndFlora?.flora = it.data?.result
                        getBoxes()
                    }
                    Status.ERROR -> {
                        pd.dismiss()
                        if (it.message == "internet") {
                            requireActivity().showAlert(getString(R.string.network_error),getString(R.string.no_internet))
                        } else {
                            requireActivity().showAlert(getString(R.string.error), it?.message.toString())
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun getBee() {
        val pd = requireActivity().getProgress()
        viewModelBee.getBeeList().observe(requireActivity()) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        beeAndFlora?.bee = it.data?.result
                        getBoxes()
                    }
                    Status.ERROR -> {
                        pd.dismiss()
                        if (it.message == "internet") {
                            requireActivity().showAlert(getString(R.string.network_error),getString(R.string.no_internet))
                        } else {
                            requireActivity().showAlert(getString(R.string.error), it?.message.toString())
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    /*private fun getBeeAndFlora() {
        val pd = requireActivity().getProgress()
        viewModelBee.getBeeAndFlora().observe(requireActivity()) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        beeAndFlora = it.data
                        getBoxes()
                    }
                    Status.ERROR -> {
                        pd.dismiss()
                        if (it.message == "internet") {
                            requireActivity().showAlert(getString(R.string.network_error),getString(R.string.no_internet))
                        } else {
                            requireActivity().showAlert(getString(R.string.error), it?.message.toString())
                        }
                    }
                    else -> {}
                }
            }

        }
    }*/

    private val updatedLocationResult =registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            var selectLocation=it.data!!
            addScanBoxResult.launch(Intent(requireContext(), FarmSetupActivity::class.java).putExtra("locationObject",selectLocation.getStringExtra("locationObject")).putExtra("from","Add New"))
         }
    }
    private val addScanBoxResult =registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            getBoxes()
        }
    }

   private val barcodeRelocateScanedResult =registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            getBoxes()
        }
    }

    private fun getBoxes() {
        var pd = requireActivity().getProgress()
        viewModelBox.getActiveFarms(MyApp.get().getProfile()?._id!!).observe(requireActivity()){
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        (activity as DashboardActivity).farmList.clear()
                        (activity as DashboardActivity).farmList.addAll(it.data!!)
                        setData(it.data)
                    }
                    Status.ERROR -> {
                        pd.dismiss()
                        if (it.message == "internet") {
                            requireActivity().showAlert(getString(R.string.network_error),getString(R.string.no_internet))
                        } else {
                            requireActivity().showAlert(getString(R.string.error), it?.message.toString())
                        }
                    }
                    else -> {}
                }
            }
        }
    }

     private fun setData(list:List<DashboardModel>?){
        binding.layoutStart.layoutStartScreen.visibility=if(list!!.isEmpty()) VISIBLE else GONE
        binding.layoutBoxes.layoutBoxesScreen.visibility=if(list.isEmpty()) GONE else VISIBLE
        list?.let { homePageAdapter.setAdapterData(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}