package com.bharuwa.sumadhu.ui.trader.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Constants
import com.bharuwa.sumadhu.constants.Util.fromListJson
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.databinding.ActivityPurchaseCompleteBinding
import com.bharuwa.sumadhu.ui.trader.model.ConfirmOrderModel
import com.bharuwa.sumadhu.ui.trader2.TraderAndFarmDetailsActivity
import com.bharuwa.sumadhu.ui.trader2.model.ConfirmTraderOrderResponse
import com.bharuwa.sumadhu.ui.trader2.model.TempProfile
import com.bharuwa.sumadhu.ui.vendor.activity.VendorDashboardActivity
import kotlinx.android.synthetic.main.activity_purchase_complete.*
import java.lang.StringBuilder

class PurchaseCompleteActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityPurchaseCompleteBinding
    private val TAG = "PurchaseCompleteBaba"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPurchaseCompleteBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.include.textTitle.text = "Purchase Complete"
        _binding.include.imgBack.visibility= View.GONE

        intent?.extras?.getString("data")?.fromListJson<ConfirmTraderOrderResponse>().let {

            Log.e(TAG, "onCreate: "+it?.json() )

          //  _binding.txtOrderId.text= it.orderNo
            _binding.txtWeigth.text= "${ intent?.extras?.getString("honyWeigth").toString()} Kg"
            _binding.txtStatus.text= "Confirm"
            TraderAndFarmDetailsActivity.tempProfile?.farmName?.let {
                _binding.txtFarmName.text= it
            }


            _binding.txtOrderID.text= it?.result2?.orderNo
            _binding.txtName.text= TraderAndFarmDetailsActivity.tempProfile?.name.toString()
            _binding.txtAddress.text= TraderAndFarmDetailsActivity.tempProfile?.address.toString()
            if (!TraderAndFarmDetailsActivity.tempProfile?.beeType.isNullOrBlank())
            _binding.txtSpecies.text= TraderAndFarmDetailsActivity.tempProfile?.beeType.toString()
            if (TraderAndFarmDetailsActivity.tempProfile?.flora != null)
            _binding.txtFlora.text= TraderAndFarmDetailsActivity.tempProfile?.flora?.joinToString()
        }
        _binding.btnBackToHome.setOnClickListener {

            goToHome()
        }
    }

    private fun goToHome() {
        TempProfile(null,null,null,null,null)

        val intent = if (MyApp.get().getProfile()?.userType.equals(Constants.VENDOR))  Intent(applicationContext, VendorDashboardActivity::class.java)
        else Intent(applicationContext, TraderDeshboardActivity::class.java)

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finishAffinity()
    }

    override fun onBackPressed() {
        goToHome()
    }


}