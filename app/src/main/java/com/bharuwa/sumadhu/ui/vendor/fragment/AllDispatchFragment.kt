package com.bharuwa.sumadhu.ui.vendor.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.databinding.FragmentAllDispatchBinding
import com.bharuwa.sumadhu.databinding.FragmentPendingDispatchBinding
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.ui.vendor.activity.DispatchActivity
import com.bharuwa.sumadhu.ui.vendor.adapter.VendorDispatchAdapter
import com.bharuwa.sumadhu.ui.viewmodel.BlendingViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AllDispatchFragment : Fragment() {

    @Inject
    lateinit var aAdapter: VendorDispatchAdapter
    private lateinit var _binding: FragmentAllDispatchBinding
    private val viewModel: BlendingViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAllDispatchBinding.inflate(layoutInflater)
        return _binding.root
      //  return inflater.inflate(R.layout.fragment_all_dispatch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            adapter = aAdapter
        }
        /*aAdapter.onItemClick {
            seletedBatch= it
        }*/


        getAllBatchesList()
    }

    private fun getAllBatchesList() {

        val pd = requireActivity().getProgress()

        viewModel.getAllBlendedHoneyList(MyApp.get().getProfile()?._id.toString()).observe(requireActivity()) {

            it?.let { resource ->
                Log.e("loadDispatch", "response: " + it.json())
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        aAdapter.setData(it.data?.result)
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

}