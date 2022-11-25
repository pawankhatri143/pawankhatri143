package com.bharuwa.sumadhu.ui.dialogs

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.bharuwa.sumadhu.R
import kotlinx.android.synthetic.main.del_acc_layout.*

class DeleteAccountDialog(val context: Activity) : Dialog(context) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        window?.attributes?.windowAnimations = R.style.DialogAnimation
        setContentView(R.layout.del_acc_layout)

        btnNo.setOnClickListener {
            dismiss()
        }

        btnYes.setOnClickListener {
            dismiss()
            //context.deleteAccount()
        }
    }
}