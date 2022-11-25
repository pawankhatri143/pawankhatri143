package com.bharuwa.sumadhu.constants

import android.Manifest
import android.R
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object Permissions {
    const val MY_PERMISSIONS_REQUEST = 123

    fun checkCameraPermission(
        context: Context?
    ): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        return if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (context as Activity?)!!,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    showDialog(
                        "External storage", context,
                        arrayOf(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        //  Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                } else {
                    ActivityCompat
                        .requestPermissions((context as Activity?)!!,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA),
                            MY_PERMISSIONS_REQUEST)
                }
                false
            } else {
                true
            }
        } else {
            true
        }
    }

    fun hasStorageReadWritePermission(
        context: Context?
    ): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        return if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (context as Activity?)!!,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    showDialog(
                        "External storage", context,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        //  Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                } else {
                    ActivityCompat
                        .requestPermissions((context as Activity?)!!,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_PERMISSIONS_REQUEST)
                }
                false
            } else {
                true
            }
        } else {
            true
        }
    }

    fun Activity.hasStoragePermission(): Boolean {
        val array = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in array) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }
    fun showDialog(
        msg: String, context: Context?,
        permission: Array<String>
    ) {
        val alertBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle("Permission necessary")
        alertBuilder.setMessage("$msg permission is necessary")
        alertBuilder.setPositiveButton(
            R.string.yes,
            DialogInterface.OnClickListener { dialog, which ->
                ActivityCompat.requestPermissions(
                    (context as Activity?)!!, permission,
                    MY_PERMISSIONS_REQUEST
                )
            })
        val alert: AlertDialog = alertBuilder.create()
        alert.show()
    }
}