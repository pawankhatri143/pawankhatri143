package com.bharuwa.sumadhu.ui.dashboard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bharuwa.sumadhu.ui.dashboard.view.DashboardActivity
import com.bharuwa.sumadhu.ui.adapter.GroupLocationAdapter
import com.bharuwa.sumadhu.ui.adapter.MyBoxesAdapter
import com.bharuwa.sumadhu.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyBoxesFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    @Inject lateinit var locationAdapter: GroupLocationAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val act = requireActivity() as DashboardActivity
        locationAdapter.bindWith(act)
        binding.recycleViewMybox.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = locationAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing=false
        }
        binding.emptyList.visibility=if(act.farmList.isEmpty()) View.VISIBLE else View.GONE
        binding.recycleViewMybox.visibility=if(act.farmList.isEmpty()) View.GONE else View.VISIBLE
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}