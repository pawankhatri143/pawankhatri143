package com.bharuwa.sumadhu.ui.vendor.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.fragment.app.FragmentActivity
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.constants.Util.showToast
import kotlinx.android.synthetic.main.weigth_edittext_layout.*
import java.util.*

/*
class EditWeigth {
}
*/


class CustomizeWeigth(val context: Activity, val weigth: String?) : Dialog(context) {

    private var onDialogDismiss: ((Float) -> Unit)? = null
    fun setDialogDismissListener(listener: ((Float) -> Unit)){
        onDialogDismiss = listener
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.weigth_edittext_layout)

      //  if (edtPrice.text.toString())
        edtHoneyWeigth.setText(weigth)
        btnCancel.setOnClickListener {  dismiss() }
        btYesSubmit.setOnClickListener {

            when{
                edtHoneyWeigth.text.isBlank()|| edtHoneyWeigth.text.toString().toFloat() < 1 ->context.showToast("Please enter valid weight")
                weigth!!.toFloat() <= edtHoneyWeigth.text.toString().toFloat() -> context.showToast("Entered weigth should be grater then orignel weight")
                else ->  {
                    onDialogDismiss?.invoke(edtHoneyWeigth.text.toString().toFloat())
                    dismiss()
                }
            }

        }

    }



}