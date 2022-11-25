package com.bharuwa.sumadhu.ui.farm

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bharuwa.sumadhu.BuildConfig
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Util.formatTo
import com.bharuwa.sumadhu.constants.Util.fromJson
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.toDate
import com.bharuwa.sumadhu.network.model.DashboardModel
import com.bharuwa.sumadhu.ui.dashboard.view.DashboardActivity.Companion.beeAndFlora
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import kotlinx.android.synthetic.main.activity_farm_details.*
import kotlinx.android.synthetic.main.deliver_address_item_layout2.view.*
import kotlinx.android.synthetic.main.titlebarlayout.*
import net.glxn.qrgen.android.QRCode
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

class FarmDetailsActivity : AppCompatActivity() {
    private var file:File?=null
    private var bitmap:Bitmap?=null
    private var farmName:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farm_details)
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        textTitle.text = getString(R.string.farm_details)

        imageViewBack.setOnClickListener { finish() }

        val farm = intent.getStringExtra("data")?.fromJson<DashboardModel>()
        farm?.let {

            val data = it.farm?.json()
            mapView.setOnClickListener { startActivity(Intent(this, FarmOnMapActivity::class.java).putExtra("location",data)) }
            farmView.setOnClickListener { startActivity(Intent(this, FarmSetupActivity::class.java).putExtra("from","Add More")
                .putExtra("locationObject", data)) }

           if ( !it.farm?.images.isNullOrEmpty() && it.farm?.images!!.size > 1){
               Glide.with(this).load("http://182.18.155.165:8080/Sumadhu/images/${it.farm?.images?.get(0)}").transform(
                   CenterCrop()
               ).into(imageFarm1)

               Glide.with(this).load("http://182.18.155.165:8080/Sumadhu/images/${it.farm?.images?.get(0)}").transform(
                   CenterCrop()
               ).into(imageFarm1)

               ll_image.visibility= View.VISIBLE
               txtFarmsPhotos.visibility= View.VISIBLE

           }
            val lang = MyApp.get().getLanguage()
            val floraList = it.farm?.flora
            val bees = beeAndFlora?.bee
            //val beeName = bees?.find { b -> b.name == it.farm?.beeName || b.name_hi == it.farm?.beeName}
            val beeName = bees?.find { b -> b.beeNameEn == it.farm?.beeName || b.beeNameHi == it.farm?.beeName}
            val flora = beeAndFlora?.flora?.filter { f -> f.nameEn in floraList!! || f.nameHi in floraList }?.map { f -> if (lang == "Hindi") f.nameHi else f.nameEn }

            txtDate.text = "${resources.getString(R.string.setup_date)} ${it.date?.toDate()?.formatTo()}"
            txtAddressName.text = "${it.farm?.name}"
            //txtBee.text = ": ${if (lang == "Hindi") beeName?.name_hi else beeName?.name}"
            txtBee.text = ": ${if (lang == "Hindi") beeName?.beeNameHi else beeName?.beeNameEn}"
            txtFlora.text = ": ${flora?.joinToString()}"
            tvTotalboxCount.text = "${resources.getString(R.string.total_boxes)}: ${it.boxCount}"
            tvLocation.text = "${it.farm?.address}, ${it.farm?.city}, ${it.farm?.district}, ${it.farm?.state}-${it.farm?.pincode}"

            val map = hashMapOf<String, String?>()
            map["farmerName"] = MyApp.get().getProfile()?.name
            map["farmerMob"] = it.farm?.mobileNumber
            map["farmName"] = it.farm?.name
            map["farmId"] = it.farm?._id
            map["farmerName"] = MyApp.get().getProfile()?._id
            map["boxes"] = it.boxCount.toString()
            map["farmLocation"] = it.farm?.city+", "+it.farm?.district+", "+it.farm?.state
            map["beeName"] = it.farm?.beeName
            map["flora"] = it.farm?.flora?.joinToString()
            map["setupDate"] = "${it.date?.toDate()?.formatTo()}"
            map["mapLink"] = "http://maps.google.com/maps?q=loc:${it.farm?.latitude}, ${it.farm?.longitude}"
            bitmap = QRCode.from(map.json()).withSize(800, 800).bitmap()
            imgQrCode.setImageBitmap(bitmap)
            farmName=it.farm?.name!!
        }
        layoutShare.setOnClickListener {
            saveMediaToStorage(bitmap!!,farmName!!)
        }
    }
    private fun saveMediaToStorage(bitmap: Bitmap,name:String) {
        //Generating a file name
        val filename = "${name}.jpg"

        //Output stream
        var fos: OutputStream? = null

        //For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //getting the contentResolver
            contentResolver?.also { resolver ->

                //Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {

                    //putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                //Inserting the contentValues to contentResolver and getting the Uri
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                //Opening an outputstream with the Uri that we got
                file = File(getPath(imageUri))
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            //These for devices running on android < Q
            //So I don't think an explanation is needed here
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            file = File(imagesDir, filename)
            fos = FileOutputStream(file)
        }

        fos?.use {
            //Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            val absolutePath: String = file!!.getAbsolutePath()
//            showToast("Saved to Photos")
        }
        file?.let {
            val apkURI =FileProvider.getUriForFile(
                Objects.requireNonNull(applicationContext),
                BuildConfig.APPLICATION_ID + ".provider", file!!)
            val intent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                this.putExtra(Intent.EXTRA_STREAM, apkURI)
                this.setDataAndType(apkURI, "image/jpg")
            }
            startActivity(Intent.createChooser(intent, null))
        }
    }
    private fun getPath(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri!!, projection, null, null, null) ?: return null
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val s = cursor.getString(column_index)
        cursor.close()
        return s
    }
}
/*

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bharuwa.sumadhu.BuildConfig
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Util.formatTo
import com.bharuwa.sumadhu.constants.Util.fromJson
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.toDate
import com.bharuwa.sumadhu.network.model.DashboardModel
import com.bharuwa.sumadhu.ui.dashboard.view.DashboardActivity.Companion.beeAndFlora
import kotlinx.android.synthetic.main.activity_farm_details.*
import kotlinx.android.synthetic.main.titlebarlayout.*
import net.glxn.qrgen.android.QRCode
import java.io.*
import java.util.*


class FarmDetailsActivity : AppCompatActivity() {
    private var file:File?=null
    private var bitmap:Bitmap?=null
    private var farmName:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farm_details)
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        textTitle.text = getString(R.string.farm_details)

        imageViewBack.setOnClickListener { finish() }

        val farm = intent.getStringExtra("data")?.fromJson<DashboardModel>()
        farm?.let {

            val data = it.farm?.json()
            mapView.setOnClickListener { startActivity(Intent(this, FarmOnMapActivity::class.java).putExtra("location",data)) }
            farmView.setOnClickListener { startActivity(Intent(this, FarmSetupActivity::class.java).putExtra("from","Add More")
                .putExtra("locationObject", data)) }

            val lang = MyApp.get().getLanguage()
            val floraList = it.farm?.flora
            val bees = beeAndFlora?.bee
            val beeName = bees?.find { b -> b.name == it.farm?.beeName || b.name_hi == it.farm?.beeName}
            val flora = beeAndFlora?.flora?.filter { f -> f.nameEn in floraList!! || f.nameHi in floraList }?.map { f -> if (lang == "Hindi") f.nameHi else f.nameEn }
//            val flora = beeAndFlora?.flora?.filter { f -> f.name in floraList!! || f.name_hi in floraList }?.map { f -> if (lang == "Hindi") f.name_hi else f.name }

            txtDate.text = "${resources.getString(R.string.setup_date)} ${it.date?.toDate()?.formatTo()}"
            txtAddressName.text = "${it.farm?.name}"
            txtBee.text = ": ${if (lang == "Hindi") beeName?.name_hi else beeName?.name}"
            txtFlora.text = ": ${flora?.joinToString()}"
            tvTotalboxCount.text = "${resources.getString(R.string.total_boxes)}: ${it.boxCount}"
            tvLocation.text = "${it.farm?.address}, ${it.farm?.city}, ${it.farm?.district}, ${it.farm?.state}-${it.farm?.pincode}"

            val map = hashMapOf<String, String?>()

            map["farmName"] = it.farm?.name
            map["farmId"] = it.farm?._id // add by pawan
            map["farmerName"] = MyApp.get().getProfile()?.name
            map["farmerMob"] = it.farm?.mobileNumber
            map["boxes"] = it.boxCount.toString()
            map["farmLocation"] = it.farm?.city+", "+it.farm?.district+", "+it.farm?.state
            map["beeName"] = it.farm?.beeName
            map["flora"] = it.farm?.flora?.joinToString()
            map["setupDate"] = "${it.date?.toDate()?.formatTo()}"
            map["mapLink"] = "http://maps.google.com/maps?q=loc:${it.farm?.latitude}, ${it.farm?.longitude}"
             bitmap = QRCode.from(map.json()).withSize(800, 800).bitmap()
            imgQrCode.setImageBitmap(bitmap)
            farmName=it.farm?.name!!
        }
        layoutShare.setOnClickListener {
            saveMediaToStorage(bitmap!!,farmName!!)
        }
    }
    private fun saveMediaToStorage(bitmap: Bitmap,name:String) {
        //Generating a file name
        val filename = "${name}.jpg"

        //Output stream
        var fos: OutputStream? = null

        //For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //getting the contentResolver
            contentResolver?.also { resolver ->

                //Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {

                    //putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                //Inserting the contentValues to contentResolver and getting the Uri
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                //Opening an outputstream with the Uri that we got
                file = File(getPath(imageUri))
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            //These for devices running on android < Q
            //So I don't think an explanation is needed here
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            file = File(imagesDir, filename)
            fos = FileOutputStream(file)
        }

        fos?.use {
            //Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            val absolutePath: String = file!!.getAbsolutePath()
//            showToast("Saved to Photos")
        }
        file?.let {
            val apkURI =FileProvider.getUriForFile(
                Objects.requireNonNull(applicationContext),
                BuildConfig.APPLICATION_ID + ".provider", file!!)
            val intent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                this.putExtra(Intent.EXTRA_STREAM, apkURI)
                this.setDataAndType(apkURI, "image/jpg")
            }
            startActivity(Intent.createChooser(intent, null))
        }
    }
    private fun getPath(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri!!, projection, null, null, null) ?: return null
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val s = cursor.getString(column_index)
        cursor.close()
        return s
    }
}*/
