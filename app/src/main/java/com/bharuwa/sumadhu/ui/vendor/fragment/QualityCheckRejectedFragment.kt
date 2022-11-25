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
import com.bharuwa.sumadhu.ui.vendor.adapter.QualityCheckAdapter
import com.bharuwa.sumadhu.ui.vendor.adapter.QualityCheckAllAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_qc_rejected.*
import javax.inject.Inject

@AndroidEntryPoint
class QualityCheckRejectedFragment : Fragment() {
    @Inject lateinit var qualityCheckAllAdapter: QualityCheckAllAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?, ): View? {
        return inflater.inflate(R.layout.fragment_qc_rejected, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewQCRejected.layoutManager = LinearLayoutManager(requireActivity())
        recyclerViewQCRejected.adapter = qualityCheckAllAdapter

        (requireActivity() as QualityCheckActivity).qualityCheckViewModel.listRejectedData.observe(viewLifecycleOwner){
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