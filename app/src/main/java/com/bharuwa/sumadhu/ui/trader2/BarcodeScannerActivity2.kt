package com.bharuwa.sumadhu.ui.trader2


import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.ContextUtils
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.databinding.ActivityBarcodeScanner2Binding
import com.bharuwa.sumadhu.databinding.ActivityScannerBinding
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.ui.trader.model.BarCodeResultModel
import com.bharuwa.sumadhu.ui.viewmodel.BoxesViewModel
import com.bharuwa.sumadhu.ui.viewmodel.LocationViewModel
import com.budiyev.android.codescanner.*
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_barcode_scanner2.*
import kotlinx.android.synthetic.main.titlebarlayout.*
import java.util.*

@AndroidEntryPoint
class BarcodeScannerActivity2 : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private val boxesViewModel: BoxesViewModel by viewModels()
    private val locationViewModel:LocationViewModel by viewModels()
    private var _binding:ActivityBarcodeScanner2Binding?= null
    private val binding get() = _binding!!
    private var userId:String?=null
    private var locationId:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_scanner2)
        _binding= ActivityBarcodeScanner2Binding.inflate(layoutInflater)
        setContentView(binding.root)
//        locationViewModel=ViewModelProvider(this).get(LocationViewModel::class.java)
//        boxesViewModel=ViewModelProvider(this).get(BoxesViewModel::class.java)

        userId=MyApp.get().getProfile()?._id
        locationId=intent.getStringExtra("locationId")
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

        codeScanner = CodeScanner(this, scannerView)
        textTitle.text=getString(R.string.scan_barrcode)
        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id

        if (intent.getStringExtra("fromScreen")!=null && intent.getStringExtra("fromScreen") == "trader"){
            codeScanner.formats = CodeScanner.ALL_FORMATS
        }else{
            codeScanner.formats = listOf(
                BarcodeFormat.AZTEC,
                BarcodeFormat.CODABAR,
                BarcodeFormat.CODE_39,
                BarcodeFormat.CODE_93,
                BarcodeFormat.CODE_128,
                BarcodeFormat.DATA_MATRIX,
                BarcodeFormat.EAN_8,
                BarcodeFormat.EAN_13,
                BarcodeFormat.ITF,
                BarcodeFormat.MAXICODE,
                BarcodeFormat.PDF_417,
                BarcodeFormat.RSS_14,
                BarcodeFormat.RSS_EXPANDED,
                BarcodeFormat.UPC_A,
                BarcodeFormat.UPC_E,
                BarcodeFormat.UPC_EAN_EXTENSION
//            BarcodeFormat.QR_CODE
            )
        }


        /*codeScanner.formats = listOf(
            BarcodeFormat.AZTEC,
            BarcodeFormat.CODABAR,
            BarcodeFormat.CODE_39,
            BarcodeFormat.CODE_93,
            BarcodeFormat.CODE_128,
            BarcodeFormat.DATA_MATRIX,
            BarcodeFormat.EAN_8,
            BarcodeFormat.EAN_13,
            BarcodeFormat.ITF,
            BarcodeFormat.MAXICODE,
            BarcodeFormat.PDF_417,
            BarcodeFormat.RSS_14,
            BarcodeFormat.RSS_EXPANDED,
            BarcodeFormat.UPC_A,
            BarcodeFormat.UPC_E,
            BarcodeFormat.UPC_EAN_EXTENSION
//            BarcodeFormat.QR_CODE
        )*/




        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
//                showToast("Scan barcode: ${it.text}")
                Log.e("dfdfdsfs", "DecodeCallback:= "+ it)
                Log.e("dfdfdsfs", "DecodeCallback==:= "+ it.text)
                val barCode=it.text
                addNewBox(barCode)
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                showToast("${getString(R.string.camera_initialization_error)}: ${it.message}")
            }
        }


        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
        layoutAddBox.setOnClickListener {
            when{
                edtBarcode.text.trim().toString().isNullOrEmpty()->showToast(getString(R.string.please_enter_barcode))
                edtBarcode.text.trim().toString().length<6->showToast(getString(R.string.please_enter_6digit_barcode))
                else->addNewBox(edtBarcode.text.trim().toString())
            }

        }
        imageViewBack.setOnClickListener {
            finish()
        }
    }
    private fun addNewBox(barCode:String){

        if (intent.getStringExtra("fromScreen")!=null && intent.getStringExtra("fromScreen") == "trader"){

            try {
                val code = Gson().fromJson(barCode,BarCodeResultModel::class.java)
                val intent = Intent()
                intent.putExtra("barCode", code.farmId)
                setResult(RESULT_OK, intent)
                finish()
            }catch (e: Exception){
                val intent = Intent()
                intent.putExtra("barCode", "6255024b450348129223299a")
                setResult(RESULT_OK, intent)
                finish()
            }


        }else
            saveBoxes(barCode)
    }



    private fun saveBoxes(barCode:String){
        val pd = getProgress()
        val body = HashMap<String, String>()
        var profileData= MyApp.get().getProfile()
        profileData?.let {
            body["userId"] = it._id.toString()
        }
        body["barcode"] = barCode
        body["locationId"] =locationId.toString()

        Log.e("boxesParams", body.json())
        boxesViewModel!!.saveBox(body).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        showToast(getString(R.string.boxes_successfully_saved))
                        val intent = Intent()
                        setResult(RESULT_OK, intent)
                        finish()
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

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
    override fun attachBaseContext(newBase: Context?) {
        val locale = Locale(MyApp.get().getLocale())
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase!!, locale)
        super.attachBaseContext(localeUpdatedContext)
    }
}