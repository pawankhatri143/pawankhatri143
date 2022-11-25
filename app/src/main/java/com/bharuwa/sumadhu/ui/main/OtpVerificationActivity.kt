package com.bharuwa.sumadhu.ui.main

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bharuwa.sumadhu.ui.dashboard.view.DashboardActivity
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Constants
import com.bharuwa.sumadhu.constants.ContextUtils
import com.bharuwa.sumadhu.constants.Util.collapse
import com.bharuwa.sumadhu.constants.Util.expand
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.databinding.ActivityOtpVerificationBinding
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.ui.trader.activity.TraderDeshboardActivity
import com.bharuwa.sumadhu.ui.vendor.activity.VendorDashboardActivity
import com.bharuwa.sumadhu.ui.viewmodel.LoginViewModel
import java.lang.Exception
import java.util.*

class OtpVerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpVerificationBinding
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        loginViewModel=ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.textMobileNumber.text = "+91-"+intent.getStringExtra("mobileNumber").toString()
        addTextWatcher(binding.et1)
        addTextWatcher(binding.et2)
        addTextWatcher(binding.et3)
        addTextWatcher(binding.et4)

        startCountDownTimer()
      /*  var otp=intent.getStringExtra("otp")
        fillOtp(otp)*/

    }
    private fun fillOtp(otp:String?){
        Handler(Looper.getMainLooper()).postDelayed({
            try{
                if(!otp.isNullOrEmpty()){
                    var arr=otp!!.toCharArray()
                    binding.et1.setText(arr[0].toString())
                    binding.et2.setText(arr[1].toString())
                    binding.et3.setText(arr[2].toString())
                    binding.et4.setText(arr[3].toString())
                }
            }
            catch (e : Exception){
            }
        },2000)
    }
    private fun addTextWatcher(et: EditText) {
        et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                when (et.id) {
                    R.id.et1 -> if (et.length() == 1) {
                        binding.et2.requestFocus()
                    }
                    R.id.et2 -> if (et.length() == 1) {
                        binding.et3.requestFocus()
                    } else if (et.length() == 0) {
                        binding.et1.requestFocus()
                    }
                    R.id.et3 -> if (et.length() == 1) {
                        binding.et4.requestFocus()
                    } else if (et.length() == 0) {
                        binding.et2.requestFocus()
                    }
                    R.id.et4 -> if (et.length() == 1) {
                        hideSoftInputKeyboard()
                        et.clearFocus()
                        /* verifyOTP(getOTPFromText())*/
                    } else if (et.length() == 0) {
                        binding.et3.requestFocus()
                    }
                }
            }
        })
    }


    private fun startCountDownTimer() {
        binding.resendView.visibility = View.GONE
        binding.resendView.collapse()
        binding.progressBar.visibility = View.VISIBLE
        val timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val d = millisUntilFinished / 1000
                binding.textTimer.text = "$d"
            }

            override fun onFinish() {
                binding.resendView.visibility = View.VISIBLE
                binding.resendView.expand()
                binding.progressBar.visibility = View.GONE
            }
        }
        timer.start()
    }

    private fun getOTPFromText(): String {
        return binding.et1.text.toString() + binding.et2.text.toString() + binding.et3.text.toString() + binding.et4.text.toString()
    }

    private fun verifyOTP(otp: String) {
        val pd = getProgress()
        loginViewModel!!.verifyOTP(intent.getStringExtra("mobileNumber").toString(),otp).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        if (it.data?.profile != null) {
                            showToast(it.data.message!!)
                            MyApp.get().setProfile(it.data.profile)
                            MyApp.get().setDefaultLocation(it.data.location)
                            when(it.data.profile?.userType){
                                Constants.FARMER ->  startActivity(Intent(applicationContext, DashboardActivity::class.java))
                                Constants.VENDOR ->  startActivity(Intent(applicationContext, VendorDashboardActivity::class.java))
                                else -> startActivity(Intent(applicationContext, TraderDeshboardActivity::class.java))
                            }
                            finishAffinity()

                        }else{
                            startActivity(Intent(applicationContext, RegistrationActivity::class.java). putExtra("mobileNumber",intent.getStringExtra("mobileNumber").toString()))
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
            loginViewModel!!.login(intent.getStringExtra("mobileNumber")!!).observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                        startCountDownTimer()
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

    fun editMobileNumber(view: View) {
        finish()
    }

    fun resendOtp(view: View) {
        binding.et1.setText("")
        binding.et2.setText("")
        binding.et3.setText("")
        binding.et4.setText("")
        resendOtp()
    }

    fun verifyOTP(view: View) {
        verifyOTP(getOTPFromText())
    }

    override fun attachBaseContext(newBase: Context?) {
        val locale = Locale(MyApp.get().getLocale())
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase!!, locale)
        super.attachBaseContext(localeUpdatedContext)
    }
}