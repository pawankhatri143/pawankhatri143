package com.bharuwa.sumadhu.ui.trader2.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.bharuwa.sumadhu.R
import kotlinx.android.synthetic.main.del_acc_layout.*

class ScanMoreAlertDialog(val context: Activity, val title: String?, val message: String?, val type: String?) : Dialog(context) {
    private var onActionYes: ((String) -> Unit)? = null

    fun setDialogDismissListener(listener: ((String) -> Unit)) {
        onActionYes = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        setContentView(R.layout.confirm_alert_layout)
        setContentView(R.layout.scan_more_layout)

       /*
         textTitle.text = title
        messageTxt.text = message
        when (type) {
            "Empty" -> imageView.setImageResource(R.drawable.ic_deliver)
            "Relocate" -> imageView.setImageResource(R.drawable.relocate)
            else -> imageView.setImageResource(R.drawable.ic_logout_icon)
        }*/
        btnNo.setOnClickListener {
            dismiss()
            onActionYes?.invoke("no")
        }

        btnYes.setOnClickListener {
            dismiss()
            onActionYes?.invoke("more")
        }
    }
}