package com.bharuwa.sumadhu.ui.vendor.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.ui.vendor.activity.QualityCheckActivity
import com.bharuwa.sumadhu.ui.vendor.activity.QualityCheckTabDetailActivity
import com.bharuwa.sumadhu.ui.vendor.adapter.QCApprovedAdapter
import com.bharuwa.sumadhu.ui.vendor.adapter.QualityCheckAllAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_qc_approved.*
import javax.inject.Inject

@AndroidEntryPoint
class QualityCheckApprovedFragment : Fragment() {
    @Inject lateinit var qualityCheckAllAdapter: QualityCheckAllAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?, ): View? {
        return inflater.inflate(R.layout.fragment_qc_approved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewQCApproved.layoutManager = LinearLayoutManager(requireActivity())
        recyclerViewQCApproved.adapter = qualityCheckAllAdapter

        (requireActivity() as QualityCheckActivity).qualityCheckViewModel.listApprovedData.observe(viewLifecycleOwner){
            qualityCheckAllAdapter.setData(it)
        }

        /*qualityCheckAllAdapter.apply {
            onItemClick {
                startActivity(Intent(requireActivity(), QualityCheckTabDetailActivity::class.java).putExtra("inwardId", it.inwardId))
                requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }*/

    }

}