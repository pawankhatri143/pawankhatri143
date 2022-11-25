package com.bharuwa.sumadhu.ui.dialogs

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.databinding.AppAlertLayoutBinding
import com.bharuwa.sumadhu.databinding.SingleEdittextLayoutBinding
import kotlinx.android.synthetic.main.single_edittext_layout.*

class EnterPriceDialog(val context: Activity): Dialog(context) {

//    private lateinit var _binding: SingleEdittextLayoutBinding

    private var itemClick: ((String) -> Unit)? = null


    fun setItemClick(action: (String) -> Unit) {
        this.itemClick = action
    }

    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        window?.attributes?.windowAnimations = R.style.DialogAnimation
       /* _binding= SingleEdittextLayoutBinding.inflate(layoutInflater)
        setContentView(_binding.root)*/
        setContentView(R.layout.single_edittext_layout)

        btNoSubmit.setOnClickListener {
            dismiss()
        }

        btYesSubmit.setOnClickListener {
            if (!edtPrice.text.isNullOrBlank()){
                itemClick?.invoke(edtPrice.text.toString())
                dismiss()
            }

            //context.deleteAccount()
        }
    }


}