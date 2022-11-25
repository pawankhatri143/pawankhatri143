package com.bharuwa.sumadhu.ui.vendor.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.ui.vendor.activity.InwardStockDetailActivity
import com.bharuwa.sumadhu.ui.vendor.adapter.InwardStockAdapter
import com.bharuwa.sumadhu.ui.vendor.model.InwardStockResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_inword_stock_farmer.*
import javax.inject.Inject

@AndroidEntryPoint
class InwordStockFarmerFrag : Fragment() {
    @Inject
    lateinit var inWardStockAdapter: InwardStockAdapter
    private val UPDATE_INWARD_CALLBACK =738

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inword_stock_farmer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewInward.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        recyclerViewInward.adapter = inWardStockAdapter

        inWardStockAdapter.apply {
            onItemClick {
                startActivityForResult(Intent(requireActivity(), InwardStockDetailActivity::class.java)
                    .putExtra("inwardStock", it.json()),UPDATE_INWARD_CALLBACK)

            }
        }

    }

    fun setData(it1: List<InwardStockResult>) {

        inWardStockAdapter.setInwardData(it1)
       /* if (inWardStockAdapter.itemCount == 0) {
            requireActivity().showToast("No data found!!!")
        }*/
    }

}