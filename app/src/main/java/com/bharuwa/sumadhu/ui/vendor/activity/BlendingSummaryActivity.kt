package com.bharuwa.sumadhu.ui.vendor.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bharuwa.sumadhu.constants.Util.fromJson
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.databinding.ActivityBlendingSummaryBinding
import com.bharuwa.sumadhu.ui.vendor.model.DispatchedCompany
import kotlinx.android.synthetic.main.activity_blending_summary.*
import java.io.File
import java.io.FileOutputStream


class BlendingSummaryActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var _binding: ActivityBlendingSummaryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityBlendingSummaryBinding.inflate(layoutInflater)
        setContentView(_binding.root)
      //  setContentView(R.layout.activity_blending_summary)

        _binding.include.textTitle.text = "Post Blending"
        _binding.include.imgBack.visibility= View.GONE


        intent?.getStringExtra("data")?.fromJson<DispatchedCompany>()?.let {

            _binding.txtBathNo.text= it.batchCode
          //  _binding.txtLocation.text= it.location
            _binding.txtWeigth.text= "${it.desiredQuantity} Kg"
            _binding.txtDate.text= it.remark2?.replace("T00:00:00","")
            _binding.txtFlora.text= it.flora
            _binding.txtRemark.text= if (it.remark1.isNullOrBlank()) "N/A" else it.remark1
            //   _binding.txtDifference.text= blendingBody["status"].toString()

            _binding.txtCompanyName.text= it.manufacturerName.toString()
            _binding.txtLocation.text= it.manufacturerWarehouseAddress.toString()
            _binding.txtOutWeigth.text= "${it.netWeight} Kg"
        }

        _binding.btnShare.setOnClickListener(this)
        _binding.btnBackToHome.setOnClickListener(this)

    }
    private fun goToHome() {

        val intent = Intent(this, VendorDashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finishAffinity()
    }

    override fun onBackPressed() {
        goToHome()
    }

    override fun onClick(v: View?) {
        when(v){
            _binding.btnShare->  {
                btnBackToHome.visibility = View.GONE
                btnShare.visibility = View.GONE
                takeScreenShort()
            }
            _binding.btnBackToHome->  goToHome()

        }
    }

    private fun takeScreenShort(){
        var fileUri: String? = null
        var bitmap: Bitmap = getBitmapFromView(
            layoutShareView,
            layoutShareView.getChildAt(0).getHeight(),
            layoutShareView.getChildAt(0).getWidth()
        )
        try {
            var mydir: File? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            {
                mydir =  File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)}/sumadhu");
            }else mydir = File(Environment.getExternalStorageDirectory().toString() + "/sumadhu")

            if (!mydir.exists()) {
                mydir.mkdirs()
            }

            fileUri = mydir.absolutePath + File.separator + System.currentTimeMillis() + ".jpg"
            val outputStream = FileOutputStream(fileUri)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            Toast.makeText(this, "Screen Shot Saved", Toast.LENGTH_SHORT).show()
            btnBackToHome.visibility = View.VISIBLE
            btnShare.visibility = View.VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("sfsfss", "onCreate: RECEIPT_SAVE_FAILED=${e.localizedMessage}")
            Toast.makeText(this, "Message.RECEIPT_SAVE_FAILED", Toast.LENGTH_SHORT).show()
        }


        try {

            val uri = Uri.parse(
                MediaStore.Images.Media.insertImage(
                    contentResolver, BitmapFactory.decodeFile(fileUri), null, null
                )
            )

            val share = Intent(Intent.ACTION_SEND)
            share.type = "image/*"
            share.putExtra(Intent.EXTRA_STREAM, uri)
            startActivity(Intent.createChooser(share, "Share Image"))
        }catch (e: Exception){}

    }

    private fun getBitmapFromView(scrollview: ScrollView, height: Int, width: Int): Bitmap {

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        var bgDrawable: Drawable?=null
        try{
            bgDrawable = scrollview.background
        }catch (e:Exception){
            Log.i("sfsfss", "getBitmapFromView: Error=${e.localizedMessage}")
        }
        if (bgDrawable == null) canvas.drawColor(Color.WHITE) else bgDrawable.draw(canvas)
        scrollview.draw(canvas)
        return bitmap
    }
}