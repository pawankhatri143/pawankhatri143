package com.bharuwa.sumadhu.dialogs

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.bharuwa.sumadhu.databinding.AppAlertLayoutBinding

class AppAlertDialog(val context: Activity, val title: String, val message: String?) : Dialog(context) {
    private lateinit var binding: AppAlertLayoutBinding
    private var onDialogDismiss: (() -> Unit)? = null
    fun setDialogDismissListener(listener: (() -> Unit)){
        onDialogDismiss = listener
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding= AppAlertLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textView8.text = title
        binding.messageAlert.text = "$message"

        binding.imgClose.setOnClickListener {
            dismiss()
            onDialogDismiss?.invoke()
        }

        binding.btnYes.setOnClickListener {
            dismiss()
            onDialogDismiss?.invoke()
        }
    }
}