package com.bharuwa.sumadhu.ui.main

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bharuwa.sumadhu.ui.dashboard.view.DashboardActivity
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Constants
import com.bharuwa.sumadhu.constants.ContextUtils
import com.bharuwa.sumadhu.constants.Util.fromJson
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.databinding.ActivityRegistrationBinding
import com.bharuwa.sumadhu.network.model.CityStateVillageModel
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.ui.trader.activity.TraderDeshboardActivity
import com.bharuwa.sumadhu.ui.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {
    private val registerViewModel: RegisterViewModel by viewModels()

    private var stateId:String?=null
    private var districtId:String?=null
    private var cityId:String?=null
    private lateinit var binding: ActivityRegistrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        registerViewModel= ViewModelProvider(this).get(RegisterViewModel::class.java)

        binding.edtState.setOnClickListener {
            stateResult.launch(Intent(this, CityStateActivity::class.java).putExtra("type","state"))
        }
        binding.edtDistrict.setOnClickListener {
            stateId?.let {
                districtResult.launch(Intent(this, CityStateActivity::class.java).putExtra("type","district").putExtra("id",it))
            }
        }
        binding.edtCity.setOnClickListener {
            districtId?.let {
                cityResult.launch(Intent(this, CityStateActivity::class.java).putExtra("type","city").putExtra("id",it))
            }
        }
        binding.layoutNext.setOnClickListener {
            when {
                binding.edtName.text.trim().isEmpty() -> showToast(getString(R.string.enter_name))
                binding.edtEmail.text.trim().isNotEmpty() && !isValidEmail(binding.edtEmail.text.trim().toString()) -> showToast(getString(R.string.enter_email_id))
                binding.radioGroupGender.checkedRadioButtonId == -1 -> showToast(getString(R.string.select_gender))
                stateId == null -> showToast(getString(R.string.please_select_state))
                districtId == null -> showToast(getString(R.string.please_select_district))
                cityId == null -> showToast(getString(R.string.please_select_city))
                else -> register()
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private val stateResult =registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            var dataObject=data?.getStringExtra("data")?.fromJson<CityStateVillageModel>()
            binding.edtState.setText(dataObject?.name)
            binding.edtDistrict.setText("")
            binding.edtCity.setText("")
            stateId=dataObject?.state_id
            districtId=null
            cityId=null
        }
    }
    private val districtResult =registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            var dataObject=data?.getStringExtra("data")?.fromJson<CityStateVillageModel>()
            binding.edtDistrict.setText(dataObject?.name)
            binding.edtCity.setText("")
            districtId=dataObject?.district_id
            cityId=null
        }
    }
    private val cityResult =registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            var dataObject=data?.getStringExtra("data")?.fromJson<CityStateVillageModel>()
            binding.edtCity.setText(dataObject?.name)
            cityId=dataObject?.tehsil_id
        }
    }
    private fun register() {
        val radio: RadioButton = findViewById(binding.radioGroupGender.checkedRadioButtonId)
        val radioUserType: RadioButton = findViewById(binding.radioGroupUserType.checkedRadioButtonId)
        val pd = getProgress()
        val body = HashMap<String, String>()
        body["name"] = binding.edtName.text.toString()
        body["email"] = binding.edtEmail.text.toString()
        body["gender"] = radio.text.toString()
        body["mobileNumber"] = intent.getStringExtra("mobileNumber").toString()
        body["state"] = binding.edtState.text.toString()
        body["district"] = binding.edtDistrict.text.toString()
        body["city"] =binding.edtCity.text.toString()
        body["address"] = binding.edtAddress.text.toString()
        body["userType"] = radioUserType.text.toString()

        Log.e("registrationParams", body.json())
       registerViewModel!!.userRegistration(body).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()

                            Log.e("registrationResponse", it.data!!.json())
                            MyApp.get().setProfile(it.data.profile)

                        if (it.data.profile?.userType == null || it.data.profile?.userType?.equals(Constants.FARMER) == true) {
                            startActivity(Intent(applicationContext, DashboardActivity::class.java))
                        }else  startActivity(Intent(applicationContext, TraderDeshboardActivity::class.java))

                            finishAffinity()

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
    override fun attachBaseContext(newBase: Context?) {
        val locale = Locale(MyApp.get().getLocale())
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase!!, locale)
        super.attachBaseContext(localeUpdatedContext)
    }
}