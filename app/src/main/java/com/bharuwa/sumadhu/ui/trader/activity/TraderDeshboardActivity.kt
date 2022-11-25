package com.bharuwa.sumadhu.ui.trader.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.databinding.ActivityTraderDeshboardBinding
import com.bharuwa.sumadhu.ui.dashboard.fragment.ProfileFragment
import com.bharuwa.sumadhu.ui.dialogs.ConfirmAlertDialog
import com.bharuwa.sumadhu.ui.main.AuthActivity
import com.bharuwa.sumadhu.ui.main.ChangeLanguageActivity
import com.bharuwa.sumadhu.ui.trader.fragment.*
import com.bharuwa.sumadhu.ui.trader2.DashboardFragmentTrader2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_trader_deshboard.*
import kotlinx.coroutines.Dispatchers

@AndroidEntryPoint
class TraderDeshboardActivity : AppCompatActivity() {
    private val TAG = "TraderDeshboardScreen"
    private lateinit var _binding: ActivityTraderDeshboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_trader_deshboard)

        _binding = ActivityTraderDeshboardBinding.inflate(layoutInflater)
        setContentView(_binding.root)


        txtUserName.text = MyApp.get().getProfile()?.name.toString()
        loadFragment(DashboardFragmentTrader2())
        _binding.navigationTrader.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_dashboard -> {
                    loadFragment(DashboardFragmentTrader2())
                }
               /* R.id.nav_drums -> {
                    loadFragment(DrumFragment())
                }*/
                R.id.nav_network ->  workInProgress() /*{

                  //  loadFragment(NetworkFragment())
                }*/
                R.id.nav_reports -> {
                    loadFragment(ReportsFragment())
                }
                R.id.nav_profile -> {
                    loadFragment(ProfileFragment())
                }
                else -> false
            }
        }

        _binding.imgLogout.setOnClickListener {
            ConfirmAlertDialog(
                this,getString(R.string.are_you_sure),getString(R.string.do_you_want_to_logout),"Logout"
            ).apply {
                setDialogDismissListener {
                    MyApp.get().logout()
                    startActivity(Intent(this@TraderDeshboardActivity, ChangeLanguageActivity::class.java))
                    finishAffinity()
                }
            }.show()
        }
    }

    private fun workInProgress(): Boolean {
        showToast("Comming Soon")
        return true
    }

    private var currentFragment = ""
    private fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null && fragment.javaClass.name != currentFragment) {
            currentFragment = fragment.javaClass.name
            Log.e(TAG, currentFragment)
            supportFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_container, fragment)
                .commit()
            return true
        }
        return false
    }
}