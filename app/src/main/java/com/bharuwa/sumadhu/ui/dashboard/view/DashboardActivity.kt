package com.bharuwa.sumadhu.ui.dashboard.view

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.ContextUtils
import com.bharuwa.sumadhu.databinding.ActivityMainBinding
import com.bharuwa.sumadhu.network.model.BeeFloraModel
import com.bharuwa.sumadhu.network.model.DashboardModel
import com.bharuwa.sumadhu.ui.dialogs.ConfirmAlertDialog
import com.bharuwa.sumadhu.ui.main.AuthActivity
import com.bharuwa.sumadhu.ui.main.ChangeLanguageActivity
import com.bharuwa.sumadhu.ui.vendor.model.BeeModel
import com.bharuwa.sumadhu.ui.vendor.model.FloraModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var farmList = listOf<DashboardModel>().toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

         intent?.getBooleanExtra("comeFrom",false)?.let {
             comeFrom = it
         }
         inits()
    }

    private fun inits() {
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard, R.id.navigation_myboxes,
                R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        var profileData = MyApp.get().getProfile()
        profileData?.let {
            binding.appBarMain.tvName.text=it.name
            binding.appBarMain.tvMobileNo.text=it.mobileNumber
        }
        binding.appBarMain.imgLogout.setOnClickListener {
            ConfirmAlertDialog(
                this,getString(R.string.are_you_sure),getString(R.string.do_you_want_to_logout),"Logout"
            ).apply {
                setDialogDismissListener {
                    MyApp.get().logout()
                    startActivity(Intent(this@DashboardActivity, ChangeLanguageActivity::class.java))
                    finishAffinity()
                }
            }.show()
        }
    }

    var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        val manager: FragmentManager = supportFragmentManager
        if (manager.backStackEntryCount > 0) {
            super.onBackPressed()
        } else {
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
    }

    companion object {
       // var beeAndFlora:BeeFloraModel? = null
        var beeAndFlora:BeeFloraModel? = BeeFloraModel(null, null)
        var comeFrom = false
    }

    override fun attachBaseContext(newBase: Context?) {
        val locale = Locale(MyApp.get().getLocale())
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase!!, locale)
        super.attachBaseContext(localeUpdatedContext)
    }


}