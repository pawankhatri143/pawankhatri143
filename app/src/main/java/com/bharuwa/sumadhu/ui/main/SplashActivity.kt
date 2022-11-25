package com.bharuwa.sumadhu.ui.main

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentSender
import android.os.*
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bharuwa.sumadhu.ui.dashboard.view.DashboardActivity
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Constants
import com.bharuwa.sumadhu.constants.ContextUtils
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.constants.Util.startXAnimation
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.ui.trader.activity.TraderDeshboardActivity
import com.bharuwa.sumadhu.ui.vendor.activity.VendorDashboardActivity
import com.bharuwa.sumadhu.ui.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.android.synthetic.main.activity_landing.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class SplashActivity : AppCompatActivity() {
    private var forceUpdate = false
    private var updateInfo: AppUpdateInfo? = null
//    private var loginViewModel: LoginViewModel? = null
    val loginViewModel: LoginViewModel by viewModels()

    private val appUpdateManager: AppUpdateManager by lazy { AppUpdateManagerFactory.create(this) }
    private val appUpdatedListener: InstallStateUpdatedListener by lazy {
        object : InstallStateUpdatedListener {
            override fun onStateUpdate(installState: InstallState) {
                when {
                    installState.installStatus() == InstallStatus.DOWNLOADED -> popupSnackbarForCompleteUpdate()
                    installState.installStatus() == InstallStatus.INSTALLED -> appUpdateManager.unregisterListener(
                        this
                    )
//                    else -> Timber.d("InstallStateUpdatedListener: state: %s", installState.installStatus())
                    else -> Log.e("state: %s", installState.installStatus().toString())
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
//        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        textVersion.visibility = View.GONE
        frameLayout.startXAnimation(x = 1000F, d = 1000)
        printVersion()
      //  checkForAppUpdate()
          nextScreen()
    }

    private fun printVersion(){
        lifecycleScope.launch {
            delay(1000)
            val pInfo = packageManager.getPackageInfo(packageName, 0)
            var localVersionName = pInfo.versionName
            textVersion.visibility = View.VISIBLE
            textVersion.text = "${getString(R.string.version)}: $localVersionName"
        }
    }

    private fun nextScreen() {
      lifecycleScope.launch {
          delay(2000)
          if (MyApp.get().getProfile() != null) {

              when{
                  MyApp.get().getProfile()?.userType.equals(Constants.FARMER) ->  startActivity(Intent(applicationContext, DashboardActivity::class.java))
                  MyApp.get().getProfile()?.userType.equals(Constants.VENDOR) ->  startActivity(Intent(applicationContext, VendorDashboardActivity::class.java))
                  else -> startActivity(Intent(applicationContext, TraderDeshboardActivity::class.java))
              }
          }

          else
              startActivity(Intent(applicationContext, ChangeLanguageActivity::class.java))
          finishAffinity()
      }
    }

    private fun checkForAppUpdate() {
        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            Log.e("appUpdateInfo:", "$appUpdateInfo")
            updateInfo = appUpdateInfo
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                // Request the update.
                Log.e("Status:", "Update available")
                checkVersionUpdate(updateInfo!!)
            } else {
                Log.e("Status:", "Update not available")
                nextScreen()
            }
        }
        appUpdateInfoTask.addOnFailureListener {
            Log.e("Status:", "Fields")
            nextScreen()
        }

    }

    private fun checkVersionUpdate(appUpdateInfo: AppUpdateInfo) {
        loginViewModel!!.checkVersionUpdate().observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                            it.data?.let { updateStatus ->
                                forceUpdate = updateStatus.forceUpdate
                                try {
                                    appUpdateManager.registerListener(appUpdatedListener)
                                    if (forceUpdate)
                                        appUpdateManager.startUpdateFlowForResult(
                                            appUpdateInfo,
                                            AppUpdateType.IMMEDIATE,
                                            this,
                                            APP_UPDATE_REQUEST_CODE
                                        )
                                    else
                                        appUpdateManager.startUpdateFlowForResult(
                                            appUpdateInfo,
                                            AppUpdateType.FLEXIBLE,
                                            this,
                                            APP_UPDATE_REQUEST_CODE
                                        )
                                } catch (e: IntentSender.SendIntentException) {
                                    e.printStackTrace()
                                    showToast("${e.printStackTrace()}")
                                    nextScreen()
                                }
                            }

                    }
                    Status.ERROR -> {
                        if (it.message == "internet") {
                            showAlert(
                                getString(R.string.network_error),
                                getString(R.string.no_internet)
                            )
                        } else {
                            showAlert(getString(R.string.error), it?.message.toString())
                        }
                    }
                    else -> {}
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == APP_UPDATE_REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                if (forceUpdate)
                    showToast("App Update failed, please try again on the next app launch.")
                else
                    nextScreen()
            }
        }
    }

    private fun popupSnackbarForCompleteUpdate() {
        val snackbar = Snackbar.make(
            findViewById(R.id.activity_splash),
            "An update has just been downloaded.",
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar.setAction("RESTART") {
            appUpdateManager.completeUpdate()
            nextScreen()
        }
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.purple_500))
        snackbar.show()
    }


    override fun onResume() {
        super.onResume()
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->

            // If the update is downloaded but not installed,
            // notify the user to complete the update.
            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate()
            }

            //Check if Immed   iate update is required
            try {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    // If an in-app update is already running, resume the update.
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        this,
                        APP_UPDATE_REQUEST_CODE
                    )
                }
            } catch (e: IntentSender.SendIntentException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private const val APP_UPDATE_REQUEST_CODE = 1991
    }

    override fun attachBaseContext(newBase: Context?) {
        val locale = Locale(MyApp.get().getLocale())
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase!!, locale)
        super.attachBaseContext(localeUpdatedContext)
    }
}