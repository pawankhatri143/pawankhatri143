package com.bharuwa.sumadhu.ui.vendor.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bharuwa.sumadhu.BuildConfig
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.CameraUtils.createImageFiles
import com.bharuwa.sumadhu.constants.CameraUtils.pickImageOptions
import com.bharuwa.sumadhu.constants.FileUtils.getPath
import com.bharuwa.sumadhu.constants.Permissions
import com.bharuwa.sumadhu.constants.Permissions.MY_PERMISSIONS_REQUEST
import com.bharuwa.sumadhu.constants.Permissions.checkCameraPermission
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.ui.vendor.activity.QualityCheckActivity
import com.bharuwa.sumadhu.ui.vendor.adapter.QualityCheckPenndingAdapter
import com.bharuwa.sumadhu.ui.vendor.dialog.AddMoreReportDialog
import com.bharuwa.sumadhu.ui.vendor.dialog.CancelReasionDialog
import com.bharuwa.sumadhu.ui.viewmodel.QualityCheckViewModel
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.android.synthetic.main.fragment_quality_check_all.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import javax.inject.Inject


@AndroidEntryPoint
class QualityCheckAllFragment : Fragment() {

    private val PICK_IMAGE_GALLERY = 453
    private val REQUEST_TAKE_PHOTO = 454
    private var isCameraClicked = false
    private var mPhotoFile: File? = null
    private val FILE_SELECT_CODE = 432
    private var selectedDocumentPath: String? = null
    private var adtPosition = 0
    private var inwordID = "0"

    @Inject lateinit var qualityCheckAllAdapter: QualityCheckPenndingAdapter
    private val qualityCheckViewModel by viewModels<QualityCheckViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_quality_check_all, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = qualityCheckAllAdapter.apply {
            onActionInvoked = { item, type,position ->
                adtPosition = position
                Log.e("djfkdsfkd", "type: "+type)
                inwordID = item.inwardId
                when(type){
                    //if (inWardList.imagePath!=null || inWardList.reportUpload){
                    "Approve" -> clickButtonApprove(item.inwardId)
                    "Reject" -> clickButtonRejected(item.inwardId)
                    "uploadReport" -> if (Permissions.hasStorageReadWritePermission(requireActivity()))openImageChoseOption()
                //openImageChoseOption()
                }

            }
        }

        setQualityCheckData()
    }

    private fun showFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        try {
            startActivityForResult(
                Intent.createChooser(intent, "Select a File to Upload"),
                FILE_SELECT_CODE
            )
        } catch (ex: ActivityNotFoundException) {
            // Potentially direct the user to the Market with a Dialog
           requireActivity().showToast("Please install a File Manager.")
        }
    }
    private fun setQualityCheckData() {
        (requireActivity() as QualityCheckActivity).qualityCheckViewModel.listAllData.observe(viewLifecycleOwner) {
            qualityCheckAllAdapter.setData(it)
            Log.e("listAllData", it.json())
        }
    }

    private fun callRejectionApi(reason: String, inwardId:String){
        val pd = requireActivity().getProgress()

        val body = HashMap<String, String>()
        body["inwardId"] = inwardId
        body["qualityTestStatus"] = "REJECTED"
        body["remark"] = reason
        body["remarkOther"] = ""

        Log.e("rejectedParams", body.json())

        qualityCheckViewModel.inwardHoneyTestingRejected(body).observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        if (it.data?.status == "ok") {
                            requireActivity().showToast(it.data.message)
                            (requireActivity() as QualityCheckActivity).qualityCheckViewModel.getVendorInward(
                                MyApp.get().getProfile()?._id.toString())
                        }else{
                            requireActivity().showToast(it.data!!.message)
                        }
                    }
                    Status.ERROR -> {
                        pd.dismiss()
                        if (it.message == "internet") {
                            requireActivity().showAlert(getString(R.string.network_error),getString(R.string.no_internet))
                        } else {
                            requireActivity().showAlert(getString(R.string.error), it?.message.toString())
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun clickButtonRejected( inwardId:String) {
        val dialog = CancelReasionDialog(requireActivity())
        dialog.setDialogDismissListener {
            callRejectionApi(it,inwardId)
        }
        dialog.show()
    }

    private fun clickButtonApprove(inwardId:String) {
        val pd = requireActivity().getProgress()

        val body = HashMap<String, String>()
        body["inwardId"] = inwardId
        body["qualityTestStatus"] = "APPROVED"
        body["remark"] = ""
        body["remarkOther"] = ""

        Log.e("approvedParams", body.json())

        qualityCheckViewModel.inwardHoneyTestingRejected(body).observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        if (it.data?.status == "ok") {
                            requireActivity().showToast(it.data.message)
                            (requireActivity() as QualityCheckActivity).qualityCheckViewModel.getVendorInward(MyApp.get().getProfile()?._id.toString())
                        }else{
                            requireActivity().showToast(it.data!!.message)
                        }
                    }
                    Status.ERROR -> {
                        pd.dismiss()
                        if (it.message == "internet") {
                            requireActivity().showAlert(getString(R.string.network_error),getString(R.string.no_internet))
                        } else {
                            requireActivity().showAlert(getString(R.string.error), it?.message.toString())
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun openImageChoseOption() {
        if (checkCameraPermission(requireActivity())) {
            requireActivity().pickImageOptions { isCameraClicked ->
                if (isCameraClicked) dispatchTakePictureIntent()
                else showFileChooser()
            }
        }
    }

    private fun openGallery() {
        if (checkCameraPermission(requireActivity())) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_GALLERY)
        }

    }

   private fun dispatchTakePictureIntent()
        {
            if (checkCameraPermission(requireActivity()))
            {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                    try {
                        val photoFile = requireActivity().createImageFiles()
                        val photoURI = FileProvider.getUriForFile(
                            requireActivity(),
                            BuildConfig.APPLICATION_ID + ".provider",
                            photoFile!!
                        )
                        mPhotoFile = photoFile;
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    } catch (ex: IOException) {
                        ex.printStackTrace();
                    }
                }
            }
        }


   override
   fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
        {
            when (requestCode) {
                MY_PERMISSIONS_REQUEST -> if (grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                    if (isCameraClicked){
                        dispatchTakePictureIntent()
                    }else{
                        openGallery()
                    }
                } else {
                    requireActivity().showToast("GET_ACCOUNTS Denied")
                }
                else -> super.onRequestPermissionsResult(
                    requestCode, permissions,
                    grantResults
                )
            }
        }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        when (requestCode) {
            FILE_SELECT_CODE -> {
                if (resultCode === AppCompatActivity.RESULT_OK) {

                    qualityCheckAllAdapter.updateData(adtPosition)
                    val uri: Uri? = intent?.data
                    // Get the path
                    if (uri != null) {
                        val path: String? = getPath(requireActivity(), uri)
                        path?.let {
                            selectedDocumentPath = it
//                            uploadFile(File(it)) }
                            callImageUploadApi(File(it))
                            openAddMoreReportDialog()
                        }


                    }

                }
            }
            REQUEST_TAKE_PHOTO ->
            {
                val file =  File(mPhotoFile?.path)

                if (file != null) {
                    // callImageUploadApi(file)
                    qualityCheckAllAdapter.updateData(adtPosition)

                    lifecycleScope.launch {
                        val compressedImage = Compressor.compress(requireActivity(), file) {
                            resolution(580,800)
                            quality(60)
                            format(Bitmap.CompressFormat.JPEG)
                            size(1_097_152) // 2 MB
                        }
                      openAddMoreReportDialog()
                      callImageUploadApi(File(compressedImage.path))
                    }
                }
            }
        }

    }

    private fun callImageUploadApi(myFile: File) {


        // Full custom
        val pd = requireActivity().getProgress()

        val reqFile = RequestBody.create("image/*".toMediaTypeOrNull(), myFile)
        val body = MultipartBody.Part.createFormData("File", myFile.name, reqFile)
        val inwardId: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            inwordID
        )
        Log.e("inwardId", "callImageUploadApi: "+inwardId )
        qualityCheckViewModel.uploadImage(body,inwardId).observe(requireActivity()) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()

                        /*if (it.data!!. == "ok") {

                        }*/
                    }
                    Status.ERROR -> {
                        pd.dismiss()

                    }
                    else -> {}
                }
            }
        }
    }

    private fun openAddMoreReportDialog() {
        val dialog= AddMoreReportDialog(requireActivity()).apply {
            setDialogDismissListener {
                if (it == "more"){
                    openImageChoseOption()
                }

            }
        }
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    }








