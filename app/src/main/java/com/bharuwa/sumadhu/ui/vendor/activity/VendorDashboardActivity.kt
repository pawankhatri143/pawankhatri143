package com.bharuwa.sumadhu.ui.vendor.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Util.updatePageTransformer
import com.bharuwa.sumadhu.databinding.ActivityDashboardBinding
import com.bharuwa.sumadhu.ui.dialogs.ConfirmAlertDialog
import com.bharuwa.sumadhu.ui.main.AuthActivity
import com.bharuwa.sumadhu.ui.main.ChangeLanguageActivity
import com.bharuwa.sumadhu.ui.trader2.FindUserDetailsActivity
import com.bharuwa.sumadhu.ui.vendor.adapter.BannerPagerAdapter
import kotlinx.android.synthetic.main.activity_dashboard.*

class VendorDashboardActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityDashboardBinding
    private var isResume = false
    private lateinit var handler: Handler

    val bannerList=  listOf<Int>(R.drawable.banner1,R.drawable.banner2,R.drawable.banner3,
     R.drawable.banner4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(_binding.root)


        clickEventFunctionality()
        MyApp.get().getProfile()?.name?.let {
            txtUserName.text= it
        }
        handler = Handler(mainLooper)
        _binding.imgLogout.setOnClickListener {
            ConfirmAlertDialog(
                this,getString(R.string.are_you_sure),getString(R.string.do_you_want_to_logout),"Logout"
            ).apply {
                setDialogDismissListener {
                    MyApp.get().logout()
                    startActivity(Intent(this@VendorDashboardActivity, ChangeLanguageActivity::class.java))
                   // startActivity(Intent(this@VendorDashboardActivity, AuthActivity::class.java))
                    finishAffinity()
                }
            }.show()
        }

        _binding.viewpager.adapter = BannerPagerAdapter(bannerList)
        _binding.viewpager.addOnPageChangeListener(pageListener)
        _binding.viewpager.updatePageTransformer()
         pageListener.onPageSelected(0)
        dots_indicator.attachTo(viewpager)
    }

    override fun onPause() {
        super.onPause()
        isResume = false
        handler.removeCallbacksAndMessages(null)
    }
    private val pageListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageScrollStateChanged(state: Int) {}
        override fun onPageSelected(i: Int) {
            position = i
            //  Log.e("count", "${pagerItems.size}")
            if (!bannerList.isNullOrEmpty() && bannerList.size > 1 && isResume) {
                //   Log.e("position", "$position")
                if ((bannerList.size - 1) == position) {
                    Handler(mainLooper).postDelayed({
                        viewpager.adapter = BannerPagerAdapter(bannerList)
                        //  bannerAdapter.setBannerData(pagerItems)
                        onPageSelected(0)
                    }, 5000)
                }else{
                    changePageAt(position + 1)
                }
            }
        }
    }

    private fun changePageAt(index: Int){
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({
            viewpager.currentItem = index
        }, 4000)
    }
    private var position = 0
    override fun onResume() {
        super.onResume()
        isResume = true
        pageListener.onPageSelected(position)
    }

    private fun clickEventFunctionality() {
        llBuyHoney.setOnClickListener {

            val intent = Intent(this, FindUserDetailsActivity::class.java)
            .putExtra("userType", "vendor")
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }

        linearLayout1.setOnClickListener {
            startActivity(Intent(applicationContext, InwardStockActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        linearLayout2.setOnClickListener {
            startActivity(Intent(applicationContext, QualityCheckActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        linearLayout3.setOnClickListener {
            startActivity(Intent(applicationContext, BlendingActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        linearLayout6.setOnClickListener {
           // startActivity(Intent(applicationContext, DispatchActivity::class.java))
            startActivity(Intent(applicationContext, DispatchStatusActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

    }

}