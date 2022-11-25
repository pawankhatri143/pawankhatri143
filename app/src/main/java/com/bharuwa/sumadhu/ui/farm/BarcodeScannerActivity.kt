package com.bharuwa.sumadhu.ui.farm

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Surface
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.ContextUtils
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.databinding.ActivityScannerBinding
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.ui.trader.model.BarCodeResultModel
import com.bharuwa.sumadhu.ui.viewmodel.BoxesViewModel
import com.bharuwa.sumadhu.ui.viewmodel.CameraXViewModel
import com.bharuwa.sumadhu.ui.viewmodel.LocationViewModel
import com.google.android.datatransport.cct.internal.LogEvent
import com.google.gson.Gson
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_scanner.*
import kotlinx.android.synthetic.main.titlebarlayout.*
import java.util.*
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@AndroidEntryPoint
class BarcodeScannerActivity : AppCompatActivity() {
    private val boxesViewModel: BoxesViewModel by viewModels()
    private val locationViewModel:LocationViewModel by viewModels()
    private var _binding:ActivityScannerBinding?=null
    private val binding get() = _binding!!
    private var userId:String? = null
    private var locationId:String? = null
    private var scannedData = ""
    private var isEnableScanner = true
    private var cameraProvider: ProcessCameraProvider? = null
    private var cameraSelector: CameraSelector? = null
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var previewUseCase: Preview? = null
    private var analysisUseCase: ImageAnalysis? = null
    private val screenAspectRatio: Int
        get() {
            // Get screen metrics used to setup camera for full screen resolution
            val metrics = DisplayMetrics().also { binding.previewViews.display?.getRealMetrics(it) }
            return aspectRatio(metrics.widthPixels, metrics.heightPixels)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userId = MyApp.get().getProfile()?._id
        locationId = intent.getStringExtra("locationId")

        textTitle.text=getString(R.string.scan_barrcode)

        setupCamera()

        layoutAddBox.setOnClickListener {
            when{
                edtBarcode.text.trim().toString().isNullOrEmpty()->showToast(getString(R.string.please_enter_barcode))
                edtBarcode.text.trim().toString().length<6->showToast(getString(R.string.please_enter_6digit_barcode))
                else->addNewBox(edtBarcode.text.trim().toString())
            }

        }
        imageViewBack.setOnClickListener { finish()}
        binding.previewViews.setOnClickListener { isEnableScanner= true }
    }

    private fun setupCamera() {

        cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(CameraXViewModel::class.java)
            .processCameraProvider
            .observe(this) { provider: ProcessCameraProvider? ->
                cameraProvider = provider
                if (isCameraPermissionGranted()) {
                    bindCameraUseCases()
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.CAMERA),
                        PERMISSION_CAMERA_REQUEST
                    )
                }
            }
    }

    private fun bindCameraUseCases() {
        bindPreviewUseCase()
        bindAnalyseUseCase()
    }

    private fun bindPreviewUseCase() {
        if (cameraProvider == null) {
            return
        }
        if (previewUseCase != null) {
            cameraProvider!!.unbind(previewUseCase)
        }


        previewUseCase = Preview.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .build()
        previewUseCase!!.setSurfaceProvider(binding.previewViews.surfaceProvider)

        try {
            cameraProvider!!.bindToLifecycle(
                /* lifecycleOwner= */this,
                cameraSelector!!,
                previewUseCase
            )
        } catch (illegalStateException: IllegalStateException) {
            Log.e(TAG, illegalStateException.message ?: "IllegalStateException")
        } catch (illegalArgumentException: IllegalArgumentException) {
            Log.e(TAG, illegalArgumentException.message ?: "IllegalArgumentException")
        }
    }

    private fun bindAnalyseUseCase() {
        // Note that if you know which format of barcode your app is dealing with, detection will be
        // faster to specify the supported barcode formats one by one, e.g.
        val option = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_CODE_128)
            .build()
        val barcodeScanner: BarcodeScanner = BarcodeScanning.getClient(option)

        if (cameraProvider == null) {
            return
        }
        if (analysisUseCase != null) {
            cameraProvider!!.unbind(analysisUseCase)
        }

        analysisUseCase = ImageAnalysis.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .build()

        // Initialize our background executor
        val cameraExecutor = Executors.newSingleThreadExecutor()

        analysisUseCase?.setAnalyzer(
            cameraExecutor,
            ImageAnalysis.Analyzer { imageProxy ->
                processImageProxy(barcodeScanner, imageProxy)
            }
        )

        try {
            cameraProvider!!.bindToLifecycle(
                /* lifecycleOwner= */this,
                cameraSelector!!,
                analysisUseCase
            )
        } catch (illegalStateException: IllegalStateException) {
            Log.e(TAG, illegalStateException.message ?: "IllegalStateException")
        } catch (illegalArgumentException: IllegalArgumentException) {
            Log.e(TAG, illegalArgumentException.message ?: "IllegalArgumentException")
        }
    }

    @SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
    private fun processImageProxy(
        barcodeScanner: BarcodeScanner,
        imageProxy: ImageProxy
    ) {
        val inputImage = InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)
        barcodeScanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                runOnUiThread {
                    if (barcodes.isNotEmpty()){
                        val code = barcodes.last().rawValue
                        Log.e("barcode==", code)
                        if (code.length == 12 && isEnableScanner) {
                            if (code != scannedData) {
                                scannedData = code
                                addNewBox(scannedData)
                                showToast(scannedData)
                                isEnableScanner = false
                            }
                        }
                    }
                }
            }
            .addOnFailureListener {
                Log.e(TAG, it.message ?: it.toString())
            }.addOnCompleteListener {
                imageProxy.close()
            }
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_CAMERA_REQUEST) {
            if (isCameraPermissionGranted()) {
                bindCameraUseCases()
            } else {
                Log.e(TAG, "no camera permission")
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            baseContext,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val TAG = BarcodeScannerActivity::class.java.simpleName
        private const val PERMISSION_CAMERA_REQUEST = 1

        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }




    private fun addNewBox(barCode:String) {
        if (intent.getStringExtra("fromScreen")!=null && intent.getStringExtra("fromScreen") == "trader"){
            try {
                val code= Gson().fromJson(barCode,BarCodeResultModel::class.java)
//              Log.e("dfdfdsfs", "DecodeCallback2:= "+ code.farmId)
                val intent = Intent()
                intent.putExtra("barCode", code.farmId)
                setResult(RESULT_OK, intent)
                finish()
            }catch (e: Exception) {
                val intent = Intent()
                intent.putExtra("barCode", "6255024b450348129223299a")
                setResult(RESULT_OK, intent)
                finish()
            }
        }else if(locationId== null && intent.getStringExtra("fromScreen").isNullOrEmpty()){
            val intent = Intent()
            intent.putExtra("barCode",barCode)
            setResult(RESULT_OK, intent)
            finish()
        }else
            saveBoxes(barCode)
    }

    private fun saveBoxes(barCode:String) {
        val pd = getProgress()
        val body = HashMap<String, String>()
        var profileData = MyApp.get().getProfile()
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
                        isEnableScanner= true
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

    override fun attachBaseContext(newBase: Context?) {
        val locale = Locale(MyApp.get().getLocale())
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase!!, locale)
        super.attachBaseContext(localeUpdatedContext)
    }

}