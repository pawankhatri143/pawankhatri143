package com.bharuwa.sumadhu.ui.farm

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.ContextUtils
import com.bharuwa.sumadhu.constants.Util.fromJson
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.databinding.ActivityFarmOnMapBinding
import com.bharuwa.sumadhu.network.model.LocationsModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.util.*


class FarmOnMapActivity : AppCompatActivity() {
    private var _binding: ActivityFarmOnMapBinding?=null
    private val binding get() = _binding!!
    private var location: LocationsModel?=null
    private var mMap: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding=ActivityFarmOnMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initMap()
        binding.include4.textTitle.text=getString(R.string.farm_location)
        binding.include4.imageViewBack.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("MissingPermission")
    private fun initMap(){
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            mMap = it
//            mMap?.isMyLocationEnabled = true
//            it.uiSettings.isZoomGesturesEnabled = true
//            it.uiSettings.isMyLocationButtonEnabled = true
            it.uiSettings.isZoomControlsEnabled = true
            it.mapType = GoogleMap.MAP_TYPE_SATELLITE

            location=intent.getStringExtra("location")!!.fromJson<LocationsModel>()
            if (location != null) {
                val loc = LatLng(location!!.latitude!!.toDouble(), location!!.longitude!!.toDouble())
                mMap?.addMarker(MarkerOptions().position(loc).title(location!!.name))
                mMap?.moveCamera(CameraUpdateFactory.newLatLng(loc))
                mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 14f))
                mMap?.addCircle(
                    CircleOptions()
                        .center(loc)
                        .radius(1000.toDouble())
                        .strokeWidth(6f)
                        .fillColor(0x400bab64)
                        .strokeColor(0x70155227)
                )
            } else {
                showToast(getString(R.string.location_not_fount))
            }
        }
    }
    override fun attachBaseContext(newBase: Context?) {
        val locale = Locale(MyApp.get().getLocale())
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase!!, locale)
        super.attachBaseContext(localeUpdatedContext)
    }
}