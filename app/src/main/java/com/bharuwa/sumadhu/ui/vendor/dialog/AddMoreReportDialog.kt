package com.bharuwa.sumadhu.ui.vendor.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.bharuwa.sumadhu.R
import kotlinx.android.synthetic.main.add_more_report_dialog.*

class AddMoreReportDialog(context: Context): Dialog(context) {

    private var onActionYes: ((String) -> Unit)? = null

    fun setDialogDismissListener(listener: ((String) -> Unit)) {
        onActionYes = listener
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.add_more_report_dialog)

        btnAddMore.setOnClickListener {
            dismiss()
            onActionYes?.invoke("more")
        }
        btnNoMore.setOnClickListener {
            dismiss()
            onActionYes?.invoke("no")
        }
    }
}