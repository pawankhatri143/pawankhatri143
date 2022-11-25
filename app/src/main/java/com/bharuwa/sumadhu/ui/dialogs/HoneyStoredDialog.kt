package com.bharuwa.sumadhu.ui.dialogs

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.bharuwa.sumadhu.R
import kotlinx.android.synthetic.main.honey_stored_layout.*

class HoneyStoredDialog(val context: Activity) : Dialog(context) {
    private var onActionHome: (() -> Unit)? = null

    fun setDialogDismissListener(listener: (() -> Unit)) {
        onActionHome = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        setContentView(R.layout.confirm_alert_layout)
        setContentView(R.layout.honey_stored_layout)

        btnHome.setOnClickListener {
            dismiss()
            onActionHome?.invoke()
        }
    }
}