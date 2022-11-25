package com.bharuwa.sumadhu.dialogs

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.bharuwa.sumadhu.R
import kotlinx.android.synthetic.main.del_acc_layout.*

class ConfirmAlertDialog(val context: Activity, val title: String, val message: String, val type: String) : Dialog(context) {
    private var onActionYes: (() -> Unit)? = null

    fun setDialogDismissListener(listener: (() -> Unit)) {
        onActionYes = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        setContentView(R.layout.confirm_alert_layout)
        setContentView(R.layout.del_acc_layout)
        textTitle.text = title
        messageTxt.text = message
        when (type) {
            "Empty" -> imageView.setImageResource(R.drawable.ic_deliver)
            "Relocate" -> imageView.setImageResource(R.drawable.relocate)
            else -> imageView.setImageResource(R.drawable.ic_logout_icon)
        }
        btnNo.setOnClickListener {
            dismiss()
        }

        btnYes.setOnClickListener {
            dismiss()
            onActionYes?.invoke()
        }
    }
}