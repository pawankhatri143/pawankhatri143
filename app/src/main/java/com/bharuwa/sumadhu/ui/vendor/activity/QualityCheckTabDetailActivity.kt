package com.bharuwa.sumadhu.ui.vendor.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.ui.vendor.adapter.QualityCheckTabDetailAdapter
import com.bharuwa.sumadhu.ui.viewmodel.QualityCheckViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_quality_check_tab_detail.*
import kotlinx.android.synthetic.main.titlebarlayout.*
import javax.inject.Inject


@AndroidEntryPoint
class QualityCheckTabDetailActivity : AppCompatActivity() {
    @Inject lateinit var qualityCheckTabDetailAdapter: QualityCheckTabDetailAdapter
    private val qualityCheckViewModel by viewModels<QualityCheckViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quality_check_tab_detail)

        textTitle.text = getString(R.string.detail)

        val qualityTestStatus = intent.getStringExtra("qualityTestStatus").toString()

        textViewStatus.text = qualityTestStatus

        if (qualityTestStatus == "PENDING") {
            linearLayout9.visibility = View.VISIBLE
            linearLayoutRemark.visibility = View.VISIBLE
        }else{
            linearLayout9.visibility = View.GONE
            linearLayoutRemark.visibility = View.GONE
        }

        recyclerViewTabDetail.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewTabDetail.adapter = qualityCheckTabDetailAdapter

        getVendorInwardDetails()

    }

    private fun getVendorInwardDetails() {
        val pd = getProgress()

        val inwardId = intent.getStringExtra("inwardId").toString()
        qualityCheckViewModel.getVendorInwardDetails(inwardId).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        if (it.data!!.status == "ok") {
                            it.data.result?.let { it1 -> qualityCheckTabDetailAdapter.setData(it1) }
                        }else{
                            showToast(it.data.message)
                        }
                    }
                    Status.ERROR -> {
                        pd.dismiss()
                        if (it.message == "internet") {
                            showAlert(getString(R.string.network_error),getString(R.string.no_internet))
                        } else {
                            showAlert(getString(R.string.error), it.message.toString())
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun buttonApproved(view: View) {
        val pd = getProgress()

        val body = HashMap<String, String>()
        val inwardId = intent.getStringExtra("inwardId").toString()
        body["inwardId"] = inwardId
        body["qualityTestStatus"] = "APPROVED"
        body["remark"] = editTextRemark.text.toString()
        body["remarkOther"] = ""

        Log.e("approvedParams", body.json())

        qualityCheckViewModel.inwardHoneyTestingApproved(body).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        if (it.data?.status == "ok") {
                            showToast(it.data.message)
                            setResult(RESULT_OK)
                            finish()
                        }else{
                            showToast(it.data!!.message)
                        }
                    }
                    Status.ERROR -> {
                        pd.dismiss()
                        if (it.message == "internet") {
                            showAlert(getString(R.string.network_error),getString(R.string.no_internet))
                        } else {
                            showAlert(getString(R.string.error), it?.message.toString())
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun buttonRejected(view: View) {
        val pd = getProgress()

        val body = HashMap<String, String>()
        val inwardId = intent.getStringExtra("inwardId").toString()
        body["inwardId"] = inwardId
        body["qualityTestStatus"] = "REJECTED"
        body["remark"] = ""
        body["remarkOther"] = ""

        Log.e("rejectedParams", body.json())

        qualityCheckViewModel.inwardHoneyTestingRejected(body).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        if (it.data?.status == "ok") {
                            showToast(it.data.message)
                            setResult(RESULT_OK)
                            finish()
                        }else{
                            showToast(it.data!!.message)
                        }
                    }
                    Status.ERROR -> {
                        pd.dismiss()
                        if (it.message == "internet") {
                            showAlert(getString(R.string.network_error),getString(R.string.no_internet))
                        } else {
                            showAlert(getString(R.string.error), it?.message.toString())
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun buttonBack(view: View) {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

}