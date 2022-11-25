package com.bharuwa.sumadhu.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.constants.Constants.*
import com.bharuwa.sumadhu.databinding.FragmentImageChooserDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ImageChooserDialog : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentImageChooserDialogBinding
    private var onItemClick: ((String) -> Unit)? = null
    fun setItemClickListener(cb: (String) -> Unit){onItemClick = cb}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentImageChooserDialogBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {

       // dismissAllowingStateLoss()
        binding.linearLayoutCamera.setOnClickListener { onItemClick?.invoke(MY_CAMERA)
            dismissAllowingStateLoss()}
        binding.linearLayoutGallery.setOnClickListener { onItemClick?.invoke(MY_GALLERY)
            dismissAllowingStateLoss()}
        binding.linearLayoutPhotoes.setOnClickListener { onItemClick?.invoke(MY_OTHER)
            dismissAllowingStateLoss()}
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): ImageChooserDialog {
            val fragment = ImageChooserDialog()
            fragment.arguments = bundle
            return fragment
        }
    }


}