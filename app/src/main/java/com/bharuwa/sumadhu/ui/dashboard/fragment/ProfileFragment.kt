package com.bharuwa.sumadhu.ui.dashboard.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.databinding.FragmentProfileBinding
import com.bharuwa.sumadhu.ui.main.ChangeLanguageActivity
import com.bharuwa.sumadhu.ui.dashboard.view.UpdateProfileActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setProfileData()
        binding.layoutEditProfile.setOnClickListener {
            profileUpdateResult.launch(Intent(requireContext(), UpdateProfileActivity::class.java))
        }
        binding.layoutChangeLang.setOnClickListener {
            changeLanguageResult.launch(Intent(requireContext(), ChangeLanguageActivity::class.java).putExtra("fromHome",true))
        }
        return root
    }
   private fun setProfileData(){
       MyApp.get().getProfile()?.let {
           binding.tvName.text=it.name
           binding.tvEmail.text=it.email
           binding.tvUserType.text=it.userType
           binding.tvState.text=it.state
           binding.tvDistrict.text=it.district
           binding.tvCity.text=it.city
           binding.tvAddress.text=it.address
           binding.rbMale.isChecked=it.gender == "Male"
           binding.rbFemale.isChecked=it.gender == "Female"
           binding.rbOther.isChecked=it.gender == "Other"
       }
    }
    private val profileUpdateResult =registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        setProfileData()
        try {
            requireActivity().findViewById<TextView>(R.id.tvName).apply {
                MyApp.get().getProfile()?.let {
                    this.text = it.name
                }
            }
            requireActivity().findViewById<TextView>(R.id.txtUserName).apply {
                MyApp.get().getProfile()?.let {
                    this.text = it.name
                }
            }
        }catch (e: Exception){}

    }
    private val changeLanguageResult =registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        setProfileData()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}