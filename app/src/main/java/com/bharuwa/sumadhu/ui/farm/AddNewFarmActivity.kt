package com.bharuwa.sumadhu.ui.farm

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Geocoder
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bharuwa.sumadhu.BuildConfig
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.AppPermission.enableLocationSettings
import com.bharuwa.sumadhu.app.AppPermission.isLocationEnabled
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.ContextUtils
import com.bharuwa.sumadhu.constants.Permissions.MY_PERMISSIONS_REQUEST
import com.bharuwa.sumadhu.constants.Permissions.checkCameraPermission
import com.bharuwa.sumadhu.constants.Util.fromListJson
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.name
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.databinding.ActivityAddNewLocationBinding
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.network.model.Bee
import com.bharuwa.sumadhu.network.model.Flora
import com.bharuwa.sumadhu.network.model.MyAddress
import com.bharuwa.sumadhu.network.model.Result
import com.bharuwa.sumadhu.ui.adapter.FloraAdapter
import com.bharuwa.sumadhu.ui.dashboard.view.DashboardActivity.Companion.beeAndFlora
import com.bharuwa.sumadhu.ui.viewmodel.BeeViewModel
import com.bharuwa.sumadhu.ui.viewmodel.LocationViewModel
import com.bharuwa.sumadhu.ui.viewmodel.MapMyIndiaAddressModel
import com.bumptech.glide.Glide
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.android.synthetic.main.activity_add_new_location.*
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class AddNewFarmActivity : AppCompatActivity() {
    @Inject
    lateinit var aAdapter: FloraAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest : LocationRequest
    private var latitude = 0.0
    private var longitude = 0.0
    private var _binding:ActivityAddNewLocationBinding?=null
    private val binding get() = _binding!!
    private var beeName:String?=null
    private var beeCode:String?=null
    private var selectedFlora = mutableListOf<String>()
    private var selectedFloraCodes = mutableListOf<String>()

    private lateinit var beeViewModel: BeeViewModel
    private lateinit var locatioViewModel: LocationViewModel
    private lateinit var mapMyIndiaAddressModel :MapMyIndiaAddressModel
    private var locationAddress:MyAddress?=null
    private var pd: Dialog?=null
    private val FLORACODE = 301
    private var floraDataList: List<Flora>? = null

    private var index = "1"
    private var fileOne: File? = null
    private var fileSecond: File? = null
    private val REQUEST_TAKE_PHOTO = 454
    private var mPhotoFile: File? = null
    private val attachments = HashMap<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityAddNewLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        attachments.clear()
        beeViewModel= ViewModelProvider(this)[BeeViewModel::class.java]
        locatioViewModel= ViewModelProvider(this)[LocationViewModel::class.java]
        mapMyIndiaAddressModel= ViewModelProvider(this)[MapMyIndiaAddressModel::class.java]

        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.alignItems = AlignItems.FLEX_START
        layoutManager.flexWrap = FlexWrap.WRAP

        binding.recyclerViewCat.layoutManager = layoutManager
        binding.recyclerViewCat.adapter = aAdapter.apply {
            setItemClick {
                binding.recyclerViewCat.adapter = it
            }
        }

        binding.tvGetLocation.setOnClickListener {
            initLocationSettings()
        }
        binding.layoutSaveLocation.setOnClickListener {
            Log.e("dfdfd", "layoutSaveLocation: "+attachments.size )
            when{
                latitude==0.0 || longitude==0.0-> showToast("Please get current location")
                binding.edtFarmName.text.trim().isEmpty()-> showToast("Please enter your farm name")
                selectedFlora.size==0-> showToast("Please select flora")
                beeName.isNullOrEmpty()-> showToast("Please select bee type")
                attachments.size != 2 -> showToast("Please take farm photos")
                else -> setLocationData()
            }
        }
        binding.include.textTitle.text=getString(R.string.new_location)
        binding.include.imageViewBack.setOnClickListener {
            finish()
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        layoutAddFlora.setOnClickListener {
            startActivityForResult(Intent(this, FloraSelectionActivity::class.java)
                .putExtra("floraData",floraDataList?.json()), FLORACODE)
        }
        addFlora.setOnClickListener {
            startActivityForResult(Intent(this, FloraSelectionActivity::class.java).putExtra("floraData",floraDataList?.json()), FLORACODE)
        }
        getBeeAndFlora()


        fmTakePhoto1.setOnClickListener {
            index ="1"
            dispatchTakePictureIntent()
            }

        fmTakePhoto2.setOnClickListener {
            index ="2"
         dispatchTakePictureIntent()
        }

    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val mFileName = "sumadhu$timeStamp"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(mFileName, ".jpg", storageDir)
    }
    private fun dispatchTakePictureIntent()
    {
        if (checkCameraPermission(this))
        {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                try {
                    val photoFile = createImageFile()
                    val photoURI = FileProvider.getUriForFile(
                        this,
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

        Log.e("Baba", "onRequestPermissionsResult: ")
        when (requestCode) {
            MY_PERMISSIONS_REQUEST -> if (grantResults[0] === PackageManager.PERMISSION_GRANTED) {

                dispatchTakePictureIntent()
               /* if (isCameraClicked){
                    dispatchTakePictureIntent()
                }else{
                    openGallery()
                }*/
            } else {
                Toast.makeText(this, "GET_ACCOUNTS Denied", Toast.LENGTH_SHORT).show()
            }
            else -> super.onRequestPermissionsResult(
                requestCode, permissions,
                grantResults
            )
        }
    }
    private fun setLocationData(){
        val pd = getProgress()
        val body = HashMap<String, Any>()
        var profileData=MyApp.get().getProfile()!!
        profileData?.let {
            body["userId"] = it._id.toString()
            body["mobileNumber"] =it.mobileNumber.toString()
        }

        body["name"] =binding.edtFarmName.text.toString()
        body["beeName"] = beeName.toString()
        body["beeCode"] = beeCode.toString()
        body["flora"] =selectedFlora
        body["floraCodes"] =selectedFloraCodes
        locationAddress?.let {
            body["state"] =it.state.toString()
            body["district"] = it.district.toString()
            body["city"] =it.city.toString()
            body["address"] = it.address.toString()
            body["pincode"] = it.postalCode.toString()
            body["latitude"] = it.latitude
            body["longitude"] = it.longitude
        }

        Log.e("locationParams", body.json())
        locatioViewModel!!.saveLocation(body).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()

                            Log.e("farmid:","farmID"+ it.data?._id.toString())
                            showToast("Location saved")

                        if (it.data?._id.isNullOrEmpty()){

                            val intent = Intent()
                            setResult(RESULT_OK, intent)
                            finish()
                        }else{
                            callImageUploadApi(it.data?._id.toString())
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
                    else -> {}
                }
            }

        }
    }
    private fun initLocationSettings() {
        if (!isLocationEnabled())
            enableLocationSettings(432)
        else {
            fetchLocation()
        }
    }
    private fun fetchLocation() {
        if (hasLocationPermission()) {
            pd=getProgress()
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    p0?: return
                    val currentLoc = p0.locations.last()
                    if (currentLoc != null) {
                        latitude = currentLoc.latitude
                        longitude = currentLoc.longitude
                        val loc = LatLng(latitude,longitude)
                        if (Geocoder.isPresent()){
                            printLocation(loc)
                        }else showToast("Geocoder is not available for current network!")

//                        showToast("$loc")
                    } else
                        showToast("location not found")
                }
            }
            startLocationUpdates()
        }
    }


    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        locationRequest = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            maxWaitTime= 1000
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }



    private val PERMISSION_ID = 42
    private fun hasLocationPermission(): Boolean {
        val perm = listOf( Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
        val havePermissions = perm.toList().all {
            ContextCompat.checkSelfPermission(this,it) == PackageManager.PERMISSION_GRANTED
        }
        if (!havePermissions) {
            ActivityCompat.requestPermissions(this, perm.toTypedArray(), PERMISSION_ID)
            return false
        }
        return true
    }

    private fun getBeeAndFlora() {
        beeAndFlora?.let { list->
            bindBee(list.bee!!)
            /*adapter.setData(list.flora!!)*/
        }
        /* val pd = getProgress()
             beeViewModel!!.getBeeAndFlora().observe(this) {
                 it?.let { resource ->
                     when (resource.status) {
                         Status.SUCCESS -> {
                             pd.dismiss()
                             if (it.data?.isSuccessful == true) {
                                 Log.e("blockData:", it.data.body()!!.json())
                                 it.data.body()?.bee?.let { list->
                                     bindBee(list)
                                 }
                                 it.data.body()?.flora?.let { listFlora->
                                     adapter.setData(listFlora)
                                 }
                             } else {
                                 val apiError = ErrorUtils.parseError(it.data!!)
                                 showAlert(getString(R.string.error),if (it.data?.code() == 500) getString(R.string.internal_server_error) else apiError.message)
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
                     }
                 }
            }*/
    }

    private fun bindBee(beeData: List<Bee>) {
      //  Log.e("fdfdfds", "bindBee:getLanguage=  "+MyApp.get().getLanguage())
        //val list: List<String> = beeData.map { if(MyApp.get().getLanguage()=="Hindi") it.name_hi!! else it.name!! }
        val list: List<String> = beeData.map { if(MyApp.get().getLanguage()=="Hindi") it.beeNameHi!! else it.beeNameEn!! }
        binding.spinnerBee.adapter = getAdapter(list)
        binding.spinnerBee.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (position!=0) {
                    /*binding.spinnerBee.tag = beeData[position].code
                    beeName = beeData[position].name
                    beeCode = beeData[position].code*/
                    binding.spinnerBee.tag = beeData[position-1].beeCode
                    beeName = beeData[position-1].beeNameEn
                    beeCode = beeData[position-1].beeCode


                }else {
                    binding.spinnerBee.tag = "0"
                    beeName = null
                    beeCode = null
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun getAdapter(list: List<String> ): ArrayAdapter<String> {
        var adapter: ArrayAdapter<String>? = null
        if (!list.isNullOrEmpty()) {
            val arrayList = list.toMutableList()
            arrayList.add(0, getString(R.string.select))
            adapter = ArrayAdapter(this, R.layout.spinner_source_layout, arrayList)
        }else  adapter = ArrayAdapter(this, R.layout.spinner_source_layout, list)
        adapter.setDropDownViewResource(R.layout.spinner_source_layout)
        return adapter
    }

    private fun printLocation(latLng: LatLng, byGooglePlaceAPi : Boolean = false) {
        val geoCoder = Geocoder(this, Locale.getDefault())
        try{
            val addresses = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            val completeAddress = addresses[0].getAddressLine(0)
            var city = addresses[0].locality
            val state = addresses[0].adminArea
            val country = addresses[0].countryName
            val postalCode = addresses[0].postalCode
            val district = addresses[0].subAdminArea
            val address = addresses[0].featureName
            var knownName = ""
            if (byGooglePlaceAPi){
                val knownNameArray = address.toString().split(",")
                knownName = "${knownNameArray[0]}, ${knownNameArray[1]}, ${knownNameArray[2]}"
            }else{
                knownName = addresses[0].featureName
            }

            val subAdminArea = addresses[0].subAdminArea
            city = city ?: subAdminArea
            Log.e("fatchLocationLatLng", "printLocation: ${latLng.latitude.toString()}")
            locationAddress = MyAddress(
                "${latLng.latitude}",
                "${latLng.longitude}",
                address,
                city,
                district,
                state,
                postalCode,
                knownName,
                subAdminArea)

            val myAddress =
                "address=$address, city=$city, state=$state, country=$country, postalCode=$postalCode, knownName=$knownName, subAdminArea=$subAdminArea"
            Log.e("fatchLocationAddress", myAddress)

            binding.tvAddress.text="$completeAddress"
            binding.tvAddress.visibility=VISIBLE
            pd!!.dismiss()
        }catch (e:Exception){
            getAddressFromMapMyIndia(latLng.latitude,latLng.longitude)
        } finally {
            stopLocationUpdates()
        }
    }

    private fun getAddressFromMapMyIndia(latitude: Double, longitude: Double) {
        mapMyIndiaAddressModel.getAddress(latitude,longitude).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd!!.dismiss()

                            Log.e("locationData:", it.data?.results!!.json())
                        it.data?.results?.let {
                            setAddressData(it)
                        }?: run {
                            showToast("Geo location not return address. Please connect another working network")
                        }
                    }
                    Status.ERROR -> {
                        pd!!.dismiss()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun setAddressData(results: List<Result>) {
        var address=results[0]
        locationAddress = MyAddress(
            "${address.lat}",
            "${address.lng}",
            address.street,
            address.city?:address.subDistrict,
            address.district,
            address.state,
            address.pincode,
            address.village,
            address.subLocality?:address.street_dist)
        binding.tvAddress.text="${address.formatted_address}"
        binding.tvAddress.visibility=VISIBLE
        pd!!.dismiss()
    }
    private fun stopLocationUpdates() {
        if(::locationCallback.isInitialized)
            fusedLocationClient.removeLocationUpdates(locationCallback)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when{
            requestCode == FLORACODE && resultCode === RESULT_OK-> {
                selectedFlora.clear()
                selectedFloraCodes.clear()
                floraDataList = data?.getStringExtra("floraData")?.fromListJson()
                floraDataList?.let {
                    aAdapter.setData(floraDataList!!)
                    addFlora.visibility=if(floraDataList!!.isEmpty()) VISIBLE else GONE
                    layoutAddFlora.visibility=if(floraDataList!!.isNotEmpty()) VISIBLE else GONE
                    it.forEach { flora->
                        selectedFlora.add(flora.name())
                        selectedFloraCodes.add(flora.code.toString())
                    }
                    Log.e("floraId:", selectedFlora.toString())
                }

            }
            requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK-> {



                val file =  File(mPhotoFile?.path)

                if (file != null) {
                    compressImage(file)
                }
                if (index =="1"){
                    cameraImgOne.visibility= View.GONE
                    Glide.with(this).load(file.path).into(imgOne)
                }
                else {
                    cameraImgTwo.visibility= View.GONE
                    Glide.with(this).load(file.path).into(imgTwo)
                }


            }
            else -> {
                Log.e("dfdfd", "onActivityResult: " )
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        stopLocationUpdates()
    }
    override fun attachBaseContext(newBase: Context?) {
        val locale = Locale(MyApp.get().getLocale())
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase!!, locale)
        super.attachBaseContext(localeUpdatedContext)
    }


    private fun compressImage(actualImage: File) {

        lifecycleScope.launch {
            val pd = getProgress()
            val compressedImage = Compressor.compress(this@AddNewFarmActivity, actualImage) {
                resolution(580, 800)
                quality(60)
                format(Bitmap.CompressFormat.JPEG)
                size(1_097_152) // 2 MB
            }
            Log.e("dfdfd", "compressImage:index "+index)
            attachments[index] = compressedImage.path
            pd.dismiss()
            Log.e("dfdfd", "compressImage: "+attachments.size)
          //  callImageUploadApi(File(compressedImage.path))

        }
    }

    private fun getImageList(): List<MultipartBody.Part>{
        val list = mutableListOf<MultipartBody.Part>()
        for ((key, url) in attachments) {

                val file =  File(url)
                val reqFile = RequestBody.create("image/*".toMediaTypeOrNull(),file)
                val body = MultipartBody.Part.createFormData("files", file.name, reqFile)
                list.add(body)
        }
        return list
    }

    private fun callImageUploadApi(farmID: String) {

        val pd = getProgress()

        val body= getImageList()
        Log.e("dfdfd", "callImageUploadApi: "+body.size )
        val farmID: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            farmID
        )

        locatioViewModel.uploadFarmImage(body,farmID).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Log.e("dfdfd", "callImageUploadApi: it.data._id= "+ it.data?.json() )
                        pd.dismiss()
                        val intent = Intent()
                        setResult(RESULT_OK, intent)
                        finish()

                    }
                    Status.ERROR -> {
                        pd.dismiss()

                    }
                    else -> {}
                }
            }
        }
    }



}