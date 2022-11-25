package com.bharuwa.sumadhu.ui.vendor.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.databinding.ActivityDispatchBinding
import com.bharuwa.sumadhu.databinding.FragmentPendingDispatchBinding
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.ui.vendor.activity.DispatchActivity
import com.bharuwa.sumadhu.ui.vendor.adapter.VendorDispatchAdapter
import com.bharuwa.sumadhu.ui.vendor.model.BlendedItem
import com.bharuwa.sumadhu.ui.viewmodel.BlendingViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PendingDispatchFragment : Fragment() {

    @Inject
    lateinit var aAdapter: VendorDispatchAdapter
    private lateinit var _binding: FragmentPendingDispatchBinding
    private val viewModel: BlendingViewModel by viewModels()
    private var seletedBatch: BlendedItem? = null
    private  var pendingDispatchList : List<BlendedItem>?= null
    private val DISPATCH_REQUEST_CODE= 454

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPendingDispatchBinding.inflate(layoutInflater)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            adapter = aAdapter
        }
        aAdapter.onItemClick {
            seletedBatch= it
        }

        _binding.btnProceed.setOnClickListener {
            if (seletedBatch!=null){
                startActivityForResult(
                    Intent(requireActivity(), DispatchActivity::class.java).putExtra("data",seletedBatch?.json()),DISPATCH_REQUEST_CODE)
            }else requireActivity().showToast("Please Select One Batch")
        }
        getPendingBatchesList()
    }

    private fun getPendingBatchesList(){
        val pd = requireActivity().getProgress()

        viewModel.getBlendedHoneyList(MyApp.get().getProfile()?._id.toString()).observe(requireActivity()) {

            it?.let { resource ->
                Log.e("loadDispatch", "response: " + it.json())
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        pendingDispatchList= it.data?.result
                        aAdapter.setData(it.data?.result,true)
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

                    else -> {}
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == DISPATCH_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            getPendingBatchesList()
        }
    }
}