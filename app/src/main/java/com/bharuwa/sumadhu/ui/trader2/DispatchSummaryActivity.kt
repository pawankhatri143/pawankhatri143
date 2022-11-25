package com.bharuwa.sumadhu.ui.trader2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.constants.Util.fromJson
import com.bharuwa.sumadhu.databinding.ActivityAddDispatchBinding
import com.bharuwa.sumadhu.databinding.ActivityDispatchSummaryBinding
import com.bharuwa.sumadhu.ui.trader.activity.TraderDeshboardActivity
import com.bharuwa.sumadhu.ui.trader2.model.AddDispatchDetails

class DispatchSummaryActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityDispatchSummaryBinding
    private var data: AddDispatchDetails?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDispatchSummaryBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.include.textTitle.text = "Dispatch Summary"
        _binding.include.imgBack.visibility= View.INVISIBLE
      //  _binding.include.imgBack.setOnClickListener { goHome() }

        data= intent?.getStringExtra("data")?.fromJson<AddDispatchDetails>()
        data?.let{

            _binding.txtDispatchID.text= it.disptchId
            _binding.txtVenderName.text= it.vendorName
            _binding.txtLocation.text= it.vendorWarehouse
            _binding.txtTotalWeigth.text= "${it.dispatchHoneyWeight.toString()} Kg"
            _binding.txtFlora.text= it.flora
         //   _binding.txtPoNumber.text= it.poNumber
        //    _binding.txtInvoiceNumber.text= it.invoiceNumber
           // _binding.txtVNumber.text= it.vehicalNumber
          //  _binding.txtDriverNo.text= it.driverNumber
         //   _binding.txtAgncyNumber.text= it.transport
            _binding.txtRemark.text= it.remark1
        }

        _binding?.btnHome?.setOnClickListener {
           goHome()
        }
//        setContentView(R.layout.activity_dispatch_summary)
    }

    private fun goHome() {
        val intent = Intent(applicationContext, TraderDeshboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finishAffinity()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        goHome()
    }
}