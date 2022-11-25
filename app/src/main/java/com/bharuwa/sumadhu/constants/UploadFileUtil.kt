package com.bharuwa.sumadhu.constants

import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

object UploadFileUtil {
   // val onItemClick: ((RequestBody)-> Unit)? =null
    fun uploadFile(sourceFile: File, uploadedFileName: String? = null) {
        Thread {
            val mimeType = getMimeType(sourceFile);
            if (mimeType == null) {
                Log.e("file error", "Not able to get mime type")
                return@Thread
            }
            val fileName: String = if (uploadedFileName == null)  sourceFile.name else uploadedFileName
          //  toggleProgressDialog(true)
            try {
                val requestBody: RequestBody =
                    MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("uploaded_file", fileName,sourceFile.asRequestBody(mimeType.toMediaTypeOrNull()))
                        .build()
              //  onItemClick?.invoke(requestBody)
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("File upload", "failed")
               // showToast("File uploading failed")
            }
          //  toggleProgressDialog(false)
        }.start()
    }

    // url = file path or whatever suitable URL you want.
    fun getMimeType(file: File): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }



    /*  fun toggleProgressDialog(show: Boolean) {
          activity.runOnUiThread {
              if (show) {
                  dialog = ProgressDialog.show(activity, "", "Uploading file...", true);
              } else {
                  dialog?.dismiss();
              }
          }
      }*/
}