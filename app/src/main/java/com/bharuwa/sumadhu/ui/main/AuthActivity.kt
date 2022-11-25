package com.bharuwa.sumadhu.ui.main

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Constants
import com.bharuwa.sumadhu.constants.ContextUtils
import com.bharuwa.sumadhu.constants.Util.fadeIn
import com.bharuwa.sumadhu.constants.Util.fadeOut
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.constants.Util.startXAnimation
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.ui.dashboard.view.DashboardActivity
import com.bharuwa.sumadhu.ui.trader.activity.TraderDeshboardActivity
import com.bharuwa.sumadhu.ui.vendor.activity.VendorDashboardActivity
import com.bharuwa.sumadhu.ui.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    val loginViewModel by viewModels<LoginViewModel>()
   // private val img = listOf(R.drawable.img_footer4, R.drawable.img_footer5,R.drawable.img_footer2, R.drawable.img_footer3, R.drawable.img_footer6, R.drawable.img_footer1)
    private val img = listOf(R.drawable.banner1, R.drawable.banner2,R.drawable.banner3, R.drawable.banner4, R.drawable.img_footer1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        addTextWatcher(et1)
        addTextWatcher(et2)
        addTextWatcher(et3)
        addTextWatcher(et4)
        layoutLogin.setOnClickListener {
            var stringArray: Array<String>?=null
            if ( edtMobile.text.trim().isNotEmpty() && edtMobile.text.trim().length >6)
             stringArray = edtMobile.text.trim().toString().map { it.toString() }.toTypedArray()

            when {
               edtMobile.text.trim().isEmpty() -> showToast(getString(R.string.enter_your_mob_number))
               edtMobile.text.trim().length < 10 -> showToast(getString(R.string.enter_valid_number))
               stringArray!![0]== "0" &&  stringArray[1]== "0" ->  showToast(getString(R.string.enter_valid_number_))
             //  stringArray!![0]== "1" ->  showToast(getString(R.string.enter_valid_number_))
               stringArray!![0]== "2" ->  showToast(getString(R.string.enter_valid_number_))
               stringArray!![0]== "3" ->  showToast(getString(R.string.enter_valid_number_))
               stringArray!![0]== "4" ->  showToast(getString(R.string.enter_valid_number_))
               stringArray!![0]== "5" ->  showToast(getString(R.string.enter_valid_number_))
               // edtMobile.text.trim().toString().toCharArray()[0].toString() = "0" -> showToast(getString(R.string.enter_valid_number))
                else -> login()
            }
        }

        btnVerify.setOnClickListener {
            verifyOTP(getOTPFromText())
        }

        textResend.setOnClickListener {
            et1.setText("")
            et2.setText("")
            et3.setText("")
            et4.setText("")
            resendOtp()
        }

        linearLayout6.startXAnimation()

        play()
    }

    private fun openOtpView() {
        linearLayout6.visibility = GONE
        verification.visibility = VISIBLE
        verification.startXAnimation()
    }

    private var i = 0
    private fun play() {
        lifecycleScope.launch {
            imageView3.fadeIn()
            delay(3000)
            imageView3.fadeOut()
            delay(3000)
            if(i > 4) i = 0
            imageView3.setImageResource(img[i])
            i++
            play()
        }
    }

    private fun addTextWatcher(et: EditText) {
        et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                when (et.id) {
                    R.id.et1 -> if (et.length() == 1) {
                        et2.requestFocus()
                    }
                    R.id.et2 -> if (et.length() == 1) {
                        et3.requestFocus()
                    } else if (et.length() == 0) {
                        et1.requestFocus()
                    }
                    R.id.et3 -> if (et.length() == 1) {
                        et4.requestFocus()
                    } else if (et.length() == 0) {
                        et2.requestFocus()
                    }
                    R.id.et4 -> if (et.length() == 1) {
                        hideSoftInputKeyboard()
                        et.clearFocus()
                        /* verifyOTP(getOTPFromText())*/
                    } else if (et.length() == 0) {
                        et3.requestFocus()
                    }
                }
            }
        })
    }

    private fun login(){
        val pd = getProgress()
        layoutLogin.visibility = GONE
        progressBar.visibility = VISIBLE
        loginViewModel!!.login(edtMobile.text.toString()).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()

                            progressBar.visibility = GONE
                         //   Log.e("loginResponse", it.data!!.json())
                            openOtpView()

                    }
                    Status.ERROR -> {
                        pd.dismiss()
                        layoutLogin.visibility = VISIBLE
                        progressBar.visibility = GONE
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

    private fun getOTPFromText(): String {
        return et1.text.toString() + et2.text.toString() + et3.text.toString() + et4.text.toString()
    }

    private fun verifyOTP(otp: String) {
        val pd = getProgress()
        loginViewModel!!.verifyOTP(edtMobile.text.toString(), otp).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()

                        if(it.data?.profile != null){
                            showToast(it.data.message.toString())
                            MyApp.get().setProfile(it.data.profile)
                            MyApp.get().setDefaultLocation(it.data.location)

                            when(it.data.profile?.userType){
                                Constants.FARMER ->  startActivity(Intent(applicationContext, DashboardActivity::class.java))
                                Constants.VENDOR ->  startActivity(Intent(applicationContext, VendorDashboardActivity::class.java))
                                else -> startActivity(Intent(applicationContext, TraderDeshboardActivity::class.java))
                            }
                            finishAffinity()

                        }else{
                            startActivity(Intent(applicationContext, RegistrationActivity::class.java). putExtra("mobileNumber", edtMobile.text.toString()))
                            finishAffinity()
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

    private fun resendOtp() {
        val pd = getProgress()
        loginViewModel!!.login(edtMobile.text.toString()).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                       /* if (it.data?.isSuccessful == true) {

                        } else {
                            val apiError = ErrorUtils.parseError(it.data!!)
                            showAlert(
                                getString(R.string.error),
                                if (it.data?.code() == 500) getString(R.string.internal_server_error) else apiError.message
                            )
                        }*/
                        pd.dismiss()
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

    fun hideSoftInputKeyboard() {
        val inputManager: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    override fun attachBaseContext(newBase: Context?) {
        val locale = Locale(MyApp.get().getLocale())
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase!!, locale)
        super.attachBaseContext(localeUpdatedContext)
    }
}