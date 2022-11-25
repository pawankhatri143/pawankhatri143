package com.bharuwa.sumadhu.ui.main

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.ContextUtils
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.databinding.ActivityLoginBinding
import com.bharuwa.sumadhu.network.tool.ErrorUtils
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.ui.viewmodel.LoginViewModel
import java.util.*

class LoginActivity : AppCompatActivity(){
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        loginViewModel= ViewModelProvider(this)[LoginViewModel::class.java]
        binding.layoutLogin.setOnClickListener {
            when {
                binding.edtMobile.text.trim().isEmpty() -> showToast(getString(R.string.enter_your_mob_number))
                binding.edtMobile.text.trim().length < 10 -> showToast(getString(R.string.enter_valid_number))
                else -> login()
            }
        }
    }

    private fun login() {
        var pd = getProgress()
        loginViewModel!!.login(binding.edtMobile.text.toString()).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()

                        it.data?.let { it1 -> it1?.json()
                            ?.let { it2 -> Log.e("loginResponse", it2) } }
                            startActivity(Intent(applicationContext, OtpVerificationActivity::class.java).putExtra("mobileNumber",binding.edtMobile.text.toString()).putExtra("otp",it.data?.otp))

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


    var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(this, getString(R.string.to_exit), Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed(
            { doubleBackToExitPressedOnce = false },
            2000
        )
    }
    override fun attachBaseContext(newBase: Context?) {
        val locale = Locale(MyApp.get().getLocale())
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase!!, locale)
        super.attachBaseContext(localeUpdatedContext)
    }
}