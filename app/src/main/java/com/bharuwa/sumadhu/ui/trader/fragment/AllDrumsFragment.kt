package com.bharuwa.sumadhu.ui.trader.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Util.getProgress
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.constants.Util.showAlert
import com.bharuwa.sumadhu.databinding.FragmentAllDrumsBinding
import com.bharuwa.sumadhu.network.config.Status
import com.bharuwa.sumadhu.ui.trader.activity.AddDrumActivity
import com.bharuwa.sumadhu.ui.trader.adapter.AllDrumsAdapter
import com.bharuwa.sumadhu.ui.trader.viewModel.AddDrumViewModel
import com.bharuwa.sumadhu.ui.trader.viewModel.DrumItem
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AllDrumsFragment : Fragment() {
    @Inject
    lateinit var allDrumsAdapter: AllDrumsAdapter
    private lateinit var _binding: FragmentAllDrumsBinding
    private val addDrumViewModel: AddDrumViewModel by viewModels()
    private val TAG = "AllDrumsFragmentBaba"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentAllDrumsBinding.inflate(layoutInflater)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.recyclerViewAllDrums.apply {

            layoutManager = LinearLayoutManager(requireActivity())
             adapter = allDrumsAdapter
        }

        _binding.recyclerViewAllDrums.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    if (_binding.fabAddDrum.isShown) {
                        _binding.fabAddDrum.hide()
                    }
                } else if (dy < 0) {
                    if (!_binding.fabAddDrum.isShown) {
                        _binding.fabAddDrum.show()
                    }
                }
            }
        })

        _binding.fabAddDrum.setOnClickListener {
            startActivityForResult(Intent(requireActivity(), AddDrumActivity::class.java),343)
        }

        MyApp.get().getProfile()?.let {
            getMyDrumList(it._id.toString())
        }

    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK && requestCode == 343)
        {
            MyApp.get().getProfile()?.let {
                getMyDrumList(it._id.toString())
            }
        }
    }
    private fun getMyDrumList(userID: String) {


        val pd = requireActivity().getProgress()

        Log.e(TAG, "userID=$userID")
        addDrumViewModel.getTraderAllDrumList(userID).observe(requireActivity()) {
            it?.let { resource ->
                Log.e(TAG, "getMyDrumList: ==")
                when (resource.status) {
                    Status.SUCCESS -> {
                        pd.dismiss()
                        if (resource.data!=null && !resource.data.result.isNullOrEmpty()){
                            Log.e(TAG, "getMyDrumList: "+resource.data.result?.json() )

                            allDrumsAdapter.setData(resource.data.result as MutableList<DrumItem>)
                        }

                    }
                    Status.ERROR -> {
                        pd.dismiss()
                        if (it.message == "internet") {
                            requireActivity().showAlert(getString(R.string.network_error),getString(R.string.no_internet))
                        } else {
                            requireActivity().showAlert(getString(R.string.error), it?.message.toString())
                        }
                    }
                    else -> { pd.dismiss() }
                }
            }

        }
    }


}
