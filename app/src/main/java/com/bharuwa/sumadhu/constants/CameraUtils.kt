package com.bharuwa.sumadhu.constants

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import com.bharuwa.sumadhu.ui.dialogs.ImageChooserDialog
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

object CameraUtils {
    fun FragmentActivity.pickImageOptions(cb: ((Boolean) -> Unit)) {

        supportFragmentManager.let {
            ImageChooserDialog.newInstance(Bundle()).apply {
                setItemClickListener { item ->
                    when (item) {
                        Constants.MY_CAMERA -> cb?.invoke(true)
                        Constants.MY_GALLERY -> cb?.invoke(false)
                        Constants.MY_OTHER -> cb?.invoke(false)
                        else -> cb?.invoke(false)
                    }
                }
                show(it, tag)
            }



        }

    }




    @Throws(IOException::class)
    fun FragmentActivity.createImageFiles(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val mFileName = "@annadata_image$timeStamp"
        val storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(mFileName, ".jpg", storageDir)
    }

    /*fun FragmentActivity.compressImage(
        actualImage: File,
        index: String,
        attachments: HashMap<String, String>
    ) {
       // Log.e("actual-fileSize:", "${computeFileSizeInKB(actualImage!!)} KB")
        Luban.compress(this, actualImage)
            .setMaxSize(99)                // limit the final image size（unit：Kb）
            .setMaxHeight(920)             // limit image height
            .setMaxWidth(580)              // limit image width
            .putGear(Luban.CUSTOM_GEAR)
            .launch(object : OnCompressListener {

                override fun onStart() {  }

                override fun onSuccess(photoFile: File?) {
                    Log.e("file compression", "SUCCESS")
                    Log.e("compressed-fileSize:", "${computeFileSizeInKB(photoFile!!)} KB")
                    attachments[index] = photoFile.path
                   // CreateAdFarmerActivity.imageAttachments[index] = photoFile

                }

                override fun onError(e: Throwable?) {
                    Log.e("file compression", "FAILED!: " + e?.message.toString())
                }

            })

    }*/



    private fun computeFileSizeInKB(file: File): Long {
        val fileSizeInBytes = file.length()
        return (fileSizeInBytes / 1024)
    }

}