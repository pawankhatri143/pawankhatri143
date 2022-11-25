package com.bharuwa.sumadhu.ui.vendor.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.ui.trader2.model.ResultItem12
import kotlinx.android.synthetic.main.app_alert_layout.*
import kotlinx.android.synthetic.main.app_alert_layout.imgClose
import kotlinx.android.synthetic.main.cancel_reasion_dialog.*

class CancelReasionDialog(val context: Activity): Dialog(context) {

    private var onDialogDismiss: ((String) -> Unit)? = null
    fun setDialogDismissListener(listener: ((String) -> Unit)){
        onDialogDismiss = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.cancel_reasion_dialog)


        imgClose.setOnClickListener {
            dismiss()
        }


        btnOkConfirm.setOnClickListener {
            if ( edtMessage.text.toString().isNotBlank()){

                dismiss()
                onDialogDismiss?.invoke(edtMessage.text.toString())
            }else context.showToast("Enter reason")
        }
    }
}