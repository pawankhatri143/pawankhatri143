package com.bharuwa.sumadhu.ui.trader.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.ui.trader.adapter.AllDrumsAdapter
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_empty_drums.*
import kotlinx.android.synthetic.main.fragment_full_drums.*

class FullDrumsFragment : Fragment() {
    val allDrumsAdapter = AllDrumsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?, ): View? {
        return inflater.inflate(R.layout.fragment_full_drums_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewFullDrums.layoutManager = LinearLayoutManager(requireActivity())
        recyclerViewFullDrums.adapter = allDrumsAdapter
    }

}
