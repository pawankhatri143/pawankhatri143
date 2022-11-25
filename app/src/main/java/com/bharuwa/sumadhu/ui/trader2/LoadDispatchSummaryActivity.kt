package com.bharuwa.sumadhu.ui.trader2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.constants.Util.fromJson
import com.bharuwa.sumadhu.databinding.ActivityLoadDispatchSummaryBinding
import com.bharuwa.sumadhu.ui.trader.activity.TraderDeshboardActivity
import com.bharuwa.sumadhu.ui.trader2.model.AddDispatchDetailsNew

class LoadDispatchSummaryActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityLoadDispatchSummaryBinding
    private var data: AddDispatchDetailsNew?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoadDispatchSummaryBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        //setContentView(R.layout.activity_load_dispatch_summary)
        data= intent?.getStringExtra("data")?.fromJson<AddDispatchDetailsNew>()
        _binding.include.textTitle.text = "Load Dispatch Summary"
        _binding.include.imgBack.visibility= View.INVISIBLE

        data?.let{
            _binding.txtDispatchID.text= it.vehicalId
            _binding.txtVenderName.text= it.vendorName
            _binding.txtLocation.text= it.vendorWarehouseName
            _binding.txtTransport.text= it.transport
            _binding.txtTransportType.text= it.vehicalType
            _binding.txtVehicleNumber.text= it.vehicalNumber
            _binding.txtDriverName.text= it.driverName
            _binding.txtDriverNumber.text= it.driverNumber
            _binding.txtPoNumber.text= it.poNumber
            _binding.txtInvoiceNumber.text= it.invoiceNumber
            _binding.txtRemark.text= it.remark1

        }
      //  _binding.txtDispatchID.text= it.disptchId
       /* _binding.txtVenderName.text= LoadDispatchActivity.bodyLDispatch["vendorName"].toString()
        _binding.txtLocation.text= LoadDispatchActivity.bodyLDispatch["endPoint"].toString()
        _binding.txtTransport.text= LoadDispatchActivity.bodyLDispatch["transport"].toString()
        _binding.txtTransportType.text= LoadDispatchActivity.bodyLDispatch["vehicalType"].toString()
        _binding.txtVehicleNumber.text= LoadDispatchActivity.bodyLDispatch["VehicalNumber"].toString()
        _binding.txtDriverName.text= LoadDispatchActivity.bodyLDispatch["driverName"].toString()
        _binding.txtDriverNumber.text= LoadDispatchActivity.bodyLDispatch["driverNumber"].toString()
        _binding.txtPoNumber.text= LoadDispatchActivity.bodyLDispatch["poNumber"].toString()
        _binding.txtInvoiceNumber.text= LoadDispatchActivity.bodyLDispatch["invoiceNumber"].toString()
        _binding.txtRemark.text= LoadDispatchActivity.bodyLDispatch["Remark1"].toString()
*/
        _binding.btnHome.setOnClickListener {

            goHome()
        }
    }

    private fun goHome() {
        LoadDispatchActivity.bodyLDispatch.clear()
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