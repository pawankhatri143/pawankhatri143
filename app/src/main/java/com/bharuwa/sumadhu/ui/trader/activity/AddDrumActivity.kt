package com.bharuwa.sumadhu.ui.trader.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Util.fromJson
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showAlertWithListner
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.databinding.ActivityAddDrumBinding
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.ui.dialogs.ConfirmAlertDialog
import com.bharuwa.sumadhu.ui.farm.BarcodeScannerActivity
import com.bharuwa.sumadhu.ui.farm.FarmRelocationActivity
import com.bharuwa.sumadhu.ui.trader.viewModel.AddDrumViewModel
import com.bharuwa.sumadhu.ui.trader.viewModel.BuyHoneyViewModel
import com.bharuwa.sumadhu.ui.trader2.BuckitListActivity
import com.bharuwa.sumadhu.ui.trader2.TraderAndFarmDetailsActivity
import com.bharuwa.sumadhu.ui.trader2.dialog.ScanMoreAlertDialog
import com.bharuwa.sumadhu.ui.trader2.model.BuckitDetailsModel
import com.bharuwa.sumadhu.ui.trader2.model.Result2Item
import com.bharuwa.sumadhu.ui.trader2.model.SearchTraderModel
import com.bharuwa.sumadhu.ui.viewmodel.CameraXViewModel
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.google.zxing.BarcodeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.appbarlayout.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@AndroidEntryPoint
class AddDrumActivity : AppCompatActivity(), View.OnClickListener {

    private val buyHoneyViewModel: BuyHoneyViewModel by viewModels()
    private var farmDetails: Result2Item? =null
    private lateinit var _binding: ActivityAddDrumBinding
    private var buyFromTrader= false
    private var traderId: String?= null

    private val sdf = SimpleDateFormat("yyyy-MM-dd")
    private lateinit var currentDate: String


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
            val metrics = DisplayMetrics().also { _binding.previewView?.display?.getRealMetrics(it) }
            return aspectRatio(metrics.widthPixels, metrics.heightPixels)
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddDrumBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        currentDate= sdf.format(Date())
          intent?.extras?.getString("data")?.fromJson<Result2Item>()?.let {
              farmDetails= it
          }
        intent?.extras?.getBoolean("byFromTrader",false)?.let {
            buyFromTrader= it
            if (it){

                _binding.edtDrumCapacity.visibility= View.GONE
                _binding.txtDrumCapacity.visibility= View.GONE
            }
        }
        intent?.extras?.getString("traderId")?.let {
            traderId= it
        }
        _binding.include.textTitle.text = getString(R.string.add_bucket)



        setupCamera()
//       _binding.txtScaner.setOnClickListener(this)
        _binding.btnAddDrum.setOnClickListener(this)
        _binding.include.imageViewBack.setOnClickListener(this)
        _binding.previewView.setOnClickListener(this)


    }

    fun back(view: View) {
        finish()
    }

    override fun onClick(v: View?) {
        when(v){
//            _binding.ll_open_scaner -> {}
//           _binding.txtScaner ->  codeScanner.startPreview()
            _binding.btnAddDrum -> checkValidation()
            _binding.previewView -> isEnableScanner= true
            _binding.include.imageViewBack -> finish()
        }
    }

    private fun checkValidation() {

        Log.e(TAG, "checkValidation: "+buyFromTrader )
        if (buyFromTrader){
            when{
                _binding.edtBarcode.text.toString().isNullOrBlank() -> showToast("Please Enter BarCode")
//                _binding.edtDrumCapacity.text.toString().isNullOrBlank() -> showToast("Please Enter Drum Capacity")
                else -> callAddNewTraderDrum()// callAddNewTraderDrum()
            }
        }else{
            when{
                _binding.edtBarcode.text.toString().isNullOrBlank() -> showToast("Please Enter BarCode")
                _binding.edtDrumCapacity.text.toString().isNullOrBlank() -> showToast("Please Enter Drum Capacity")
                else -> addBuckitOnList()// callAddNewTraderDrum()
            }
        }


    }

    private fun addBuckitOnList() {
        val bucket = BuckitDetailsModel(
            containerCode= _binding.edtBarcode.text.toString(),
            containerFillStatus= "Fill",
            containerEmptyStatus= "Empty",
            containerType= "Bucket",
            containerFillFarmerId= farmDetails?.userId,
            containerFillFarmId= farmDetails?.farmId,
            containerHoneyWeight= _binding.edtDrumCapacity.text.trim().toString().toDouble(),
            containerNetWeight= _binding.edtDrumCapacity.text.trim().toString().toDouble().plus(10),
            emptyContainerWeight= 10.00,
            containerAmount= _binding.edtDrumCapacity.text.trim().toString().toDouble(),
            honeyPerKgAmount= _binding.edtDrumCapacity.text.trim().toString().toDouble(),
            orderId= "",
            traderId= "",
            status= "1",
            containerFillDate= currentDate,
            containerEmptyDate= currentDate,
            purchaseBy= MyApp.get().getProfile()?._id,
            _id="",
            containerId="",
            flora = TraderAndFarmDetailsActivity.tempProfile?.flora?.joinToString(),
        )

        MyApp.get().buckitList.add(bucket)

        Log.e("dfsdsa", "addBuckitOnList: "+MyApp.get().buckitList.size)

        refreshPage()
    }

    private fun refreshPage() {
       val dialog= ScanMoreAlertDialog(
            this,"","",""
        ).apply {
            setDialogDismissListener {
                if (it == "more"){
                    _binding.edtBarcode.setText("")
                    _binding.edtDrumCapacity.setText("")
                    isEnableScanner= true
                }else{
                    startActivity(Intent(this@AddDrumActivity, BuckitListActivity::class.java))
                    finish()
                }

            }
        }
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun callAddNewTraderDrum() {
       val pd = getProgress()

        Log.e("gfdgjfd", "callAddNewTraderDrum:= "+ _binding.edtBarcode.text.toString() )
        Log.e("gfdgjfd", "callAddNewTraderDrum:traderId: "+ traderId )
         buyHoneyViewModel.getTraderOrdersByItem(_binding.edtBarcode.text.toString(),traderId).observe(this) {
           it?.let { resource ->
               when (resource.status) {
                   Status.SUCCESS -> {
                       pd.dismiss()

                       Log.e("gfdgjfd", "callAddNewTraderDrum: "+ resource.data?.result )
                       if (resource.data?.status == "ok" && resource.data?.result?.isNotEmpty() == true){

                           val list= resource.data?.result[0]
                           val bucket = BuckitDetailsModel(
                               containerCode= _binding.edtBarcode.text.toString(),
                               containerFillStatus= "Fill",
                               containerEmptyStatus= "Empty",
                               containerType= "Bucket",
                               containerFillFarmerId= list?.containerFillFarmerId,
                               containerFillFarmId= list?.containerFillFarmId,
                               containerHoneyWeight= list?.containerHoneyWeight,
                               containerNetWeight= list?.containerNetWeight,
                               emptyContainerWeight=list?.emptyContainerWeight,
                               containerAmount= list?.containerAmount,
                               honeyPerKgAmount= list?.honeyPerKgAmount,
                               orderId= list?.orderId,
                               traderId= traderId,
                               status=  list?.status,
                               containerFillDate= currentDate,
                               containerEmptyDate= currentDate,
                               purchaseBy= MyApp.get().getProfile()?._id,
                               containerCount= "1",
                               actualHoneyTotalWeight= list?.containerNetWeight.toString(),
                               actualHoneyNetWeight= list?.containerHoneyWeight.toString(),
                               flora = list?.flora,
                           )

                           MyApp.get().buckitList.add(bucket)
                           refreshPage()
                       }else  if (resource.data?.status == "ok" && resource.data?.result?.isEmpty() == true){
                       showToast("No data found")
                           refreshPage()
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
                   else -> { pd.dismiss() }
               }
           }

       }
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
           // .setTargetRotation(_binding.previewView?.display.rotation)
            .build()
        previewUseCase!!.setSurfaceProvider(_binding.previewView?.surfaceProvider)

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
                        if (code.length == 12 && isEnableScanner){
                            if (code != scannedData){
                                scannedData = code
                                showToast("Scan barcode: ${scannedData}")
                                _binding.edtBarcode.setText(scannedData)
                                isEnableScanner = false
                            }
                        }

                    }
                }
            }
            .addOnFailureListener {
                Log.e(TAG, it.message ?: it.toString())
            }.addOnCompleteListener {
                // When the image is from CameraX analysis use case, must call image.close() on received
                // images when finished using them. Otherwise, new images may not be received or the camera
                // may stall.
                imageProxy.close()
            }
    }


    /**
     *  [androidx.camera.core.ImageAnalysis], [androidx.camera.core.Preview] requires enum value of
     *  [androidx.camera.core.AspectRatio]. Currently it has values of 4:3 & 16:9.
     *
     *  Detecting the most suitable ratio for dimensions provided in @params by counting absolute
     *  of preview ratio to one of the provided values.
     *
     *  @param width - preview width
     *  @param height - preview height
     *  @return suitable aspect ratio
     */
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
}