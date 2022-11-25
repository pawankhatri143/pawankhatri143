package com.bharuwa.sumadhu.ui.dashboard.view

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.ContextUtils
import com.bharuwa.sumadhu.constants.Util.fromJson
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.databinding.ActivityUpdateProfileBinding
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.network.model.CityStateVillageModel
import com.bharuwa.sumadhu.network.model.Profile
import com.bharuwa.sumadhu.ui.main.CityStateActivity
import com.bharuwa.sumadhu.ui.trader2.TraderAndFarmDetailsActivity
import com.bharuwa.sumadhu.ui.trader2.model.SearchTraderModel
import com.bharuwa.sumadhu.ui.viewmodel.RegisterViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class UpdateProfileActivity : AppCompatActivity() {
    private val viewModel by viewModels<RegisterViewModel>()

    private lateinit var binding: ActivityUpdateProfileBinding
    private var stateId:String?=null
    private var districtId:String?=null
    private var cityId:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyApp.get().getProfile().let {

           /* if (it?.userType?.contains(",") == true) binding.switchUserType.visibility= View.GONE
            else binding.switchUserType.visibility= View.GONE*/
            when(it?.userType?.lowercase()){
                "farmer" -> binding.switchUserType.text= "Become Trader"
                "trader" -> binding.switchUserType.text= "Become Farmer"
                else -> binding.switchUserType.visibility= View.GONE
            }
            binding.edtUserType.setText(it?.userType)
            binding.edtName.setText(it?.name)
            binding.edtEmail.setText(it?.email)
            binding.edtState.setText(it?.state)
            binding.edtDistrict.setText(it?.district)
            binding.edtCity.setText(it?.city)
            binding.edtAddress.setText(it?.address)
            binding.rbMale.isChecked = it?.gender == "Male"
            binding.rbFemale.isChecked = it?.gender == "Female"
            binding.rbOther.isChecked = it?.gender == "Other"
        }

        binding.edtState.setOnClickListener {
            stateResult.launch(Intent(this, CityStateActivity::class.java).putExtra("type","state"))
        }
        binding.edtDistrict.setOnClickListener {
            if(stateId.isNullOrEmpty()) showToast("Please select state first")
            else {
                districtResult.launch(Intent(this, CityStateActivity::class.java).putExtra("type","district").putExtra("id",stateId))
            }
        }
        binding.switchUserType.setOnClickListener {

            when(MyApp.get().getProfile()?.userType?.lowercase()){
                "farmer" ->  dialogForBuying("Become Trader","Are you sure want become a trader?")
                "trader" -> dialogForBuying("Become Farmer","Are you sure want become a farmer?")
                else -> binding.switchUserType.visibility= View.GONE
            }

        }
        binding.edtCity.setOnClickListener {
            districtId?.let {
                cityResult.launch(Intent(this, CityStateActivity::class.java).putExtra("type","city").putExtra("id",it))
            }
        }

        binding.include.textTitle.text=getString(R.string.update_profie)
        binding.include.imageViewBack.setOnClickListener {
            this.finish()
        }

        binding.layoutSave.setOnClickListener {
            when {
                binding.edtName.text.trim().isEmpty() -> showToast(getString(R.string.enter_name))
               // binding.edtEmail.text.trim().isEmpty() -> showToast(getString(R.string.enter_email_id))
                binding.radioGroupGender.checkedRadioButtonId == -1 -> showToast(getString(R.string.select_gender))
                binding.edtState.text.toString().isNullOrEmpty() -> showToast(getString(R.string.please_select_state))
                binding.edtDistrict.text.toString().isNullOrEmpty() -> showToast(getString(R.string.please_select_district))
                binding.edtCity.text.toString().isNullOrEmpty()-> showToast(getString(R.string.please_select_city))
                else -> updateProfile()
            }
        }
    }

    private fun dialogForBuying(tittle: String,msg: String) {

        MaterialAlertDialogBuilder(this)
            .setTitle(tittle)
            .setMessage(msg)
            .setCancelable(false)

            .setNegativeButton(resources.getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->

                updateProfile()
            }
            .show()
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
    private val districtResult =registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            var dataObject=data?.getStringExtra("data")?.fromJson<CityStateVillageModel>()
            binding.edtDistrict.setText(dataObject?.name)
            binding.edtCity.setText("")
            districtId=dataObject?.district_id
            cityId=null
        }
    }
    private val cityResult =registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            var dataObject=data?.getStringExtra("data")?.fromJson<CityStateVillageModel>()
            binding.edtCity.setText(dataObject?.name)
            cityId=dataObject?.tehsil_id
        }
    }
    private fun updateProfile() {
        val radio: RadioButton = findViewById(binding.radioGroupGender.checkedRadioButtonId)
        val pd = getProgress()
        val body = HashMap<String, String>()
        body["_id"] =  MyApp.get().getProfile()?._id.toString()
        body["name"] = binding.edtName.text.toString()
        body["email"] = binding.edtEmail.text.toString()
        body["gender"] = radio.text.toString()
        body["mobileNumber"] =  MyApp.get().getProfile()?.mobileNumber.toString()
        body["state"] = binding.edtState.text.toString()
        body["district"] = binding.edtDistrict.text.toString()
        body["city"] =binding.edtCity.text.toString()
        body["address"] = binding.edtAddress.text.toString()

        when{
            MyApp.get().getProfile()?.userType?.lowercase() == "farmer" -> body["userType"] = "Farmer,Trader"
            MyApp.get().getProfile()?.userType?.lowercase() == "trader" -> body["userType"] = "Trader,Farmer"
            else -> body["userType"] = binding.edtUserType.text.toString()
        }



        Log.e("updateParams", body.json())
        viewModel.profileUpdate(body).observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    pd.dismiss()

                    if (MyApp.get().getProfile()?.userType?.lowercase() == "farmer") binding.edtUserType.setText("Farmer,Trader")
                    else binding.edtUserType.setText("Trader,Farmer")
                    binding.switchUserType.visibility= View.GONE
                    MyApp.get().setProfile(it.data!!.profile)
                    showToast(it.data.message.toString())
                  //  setResult(RESULT_OK)
                    finish()
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
    override fun attachBaseContext(newBase: Context?) {
        val locale = Locale(MyApp.get().getLocale())
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase!!, locale)
        super.attachBaseContext(localeUpdatedContext)
    }
}