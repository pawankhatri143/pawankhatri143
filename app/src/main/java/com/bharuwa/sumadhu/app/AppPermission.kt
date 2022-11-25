package com.bharuwa.sumadhu.app


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import androidx.core.app.ActivityCompat
import com.bharuwa.sumadhu.constants.Util.showToast
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse


object AppPermission {
    fun Activity.hasLocationPermissions(): Boolean {
        val array = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in array) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }

    fun Activity.askLocationPermission(requestCode: Int = 11, neverAsk : Boolean = false) {
        Log.e("dfdfd", "askLocationPermission:= "+ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Log.e("dfdfd", "askLocationPermission: "+neverAsk)

            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), requestCode
            )


           //
        } else {

            Log.e("dfdfd", "askLocationPermission:else ")
            if (neverAsk) openAppSettings(this)
            else{
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ), requestCode
                )
            }

        }
    }


    fun openAppSettings(activity: Activity) {
        val packageUri: Uri = Uri.fromParts(
            "package",
            activity.packageName,
            null
        )
        val applicationDetailsSettingsIntent = Intent()
        applicationDetailsSettingsIntent.action = ACTION_APPLICATION_DETAILS_SETTINGS
        applicationDetailsSettingsIntent.data = packageUri
        applicationDetailsSettingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity
            .startActivity(applicationDetailsSettingsIntent)
    }

    fun Activity.hasCameraPermission(): Boolean {
        val array = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
    fun Activity.askStoragePermission(requestCode: Int = 12) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), requestCode
            )
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), requestCode
            )
        }
    }

    fun Activity.askCameraPermission(requestCode: Int = 11) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), requestCode
            )
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), requestCode
            )
        }
    }

    fun Activity.isLocationEnabled(): Boolean {
        var isGpsEnabled = false
        var isNetworkEnabled = false
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        try {
            isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (e: Exception) {

        }

        return isGpsEnabled && isNetworkEnabled
    }

    fun Activity.enableLocationSettings(code: Int) {
        val locationRequest: LocationRequest = LocationRequest.create()
            .setInterval(1000)
            .setFastestInterval(1000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        LocationServices
            .getSettingsClient(this)
            .checkLocationSettings(builder.build())
            .addOnSuccessListener(this) { response: LocationSettingsResponse? -> }
            .addOnFailureListener(this) { ex ->
                if (ex is ResolvableApiException) {
                    try {
                        ex.startResolutionForResult(this,code)
                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Ignore the error.
                        showToast("Location setting exception: ${sendEx.message}")
                    }
                }
            }
    }
}