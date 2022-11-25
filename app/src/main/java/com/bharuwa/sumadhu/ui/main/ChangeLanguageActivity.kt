package com.bharuwa.sumadhu.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.ui.dashboard.view.DashboardActivity
import kotlinx.android.synthetic.main.activity_change_language.*
import kotlinx.android.synthetic.main.titlebarlayout.*

class ChangeLanguageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_language)
        include.visibility = if (intent.getBooleanExtra("fromHome", false)) View.VISIBLE else View.GONE
        textTitle.text = getString(R.string.select_language)

        cardViewHindi.setOnClickListener {
            MyApp.get().setLanguage("Hindi")
            MyApp.get().setLocale("hi")
            radioButton1.isChecked = true
            radioButton2.isChecked = false
        }

        cardViewEnglish.setOnClickListener {
            MyApp.get().setLanguage("English")
            MyApp.get().setLocale("en")
            radioButton1.isChecked = false
            radioButton2.isChecked = true
        }

        layoutSetLang.setOnClickListener {
            if (radioButton1.isChecked || radioButton2.isChecked)
                nextScreen()
            else
                showToast(getString(R.string.please_select_language))
        }

        imageViewBack.setOnClickListener {
            finish()
        }

    }

    private fun nextScreen() {
        if (intent.getBooleanExtra("fromHome", false)) {
            startActivity(Intent(applicationContext, DashboardActivity::class.java))
        }else {
            startActivity(Intent(applicationContext, AuthActivity::class.java))
        }
        finishAffinity()
    }
}