package com.bharuwa.sumadhu.ui.vendor.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.widget.PopupMenu
import com.bharuwa.sumadhu.R
import com.bharuwa.sumadhu.constants.Util.showToast
import com.bharuwa.sumadhu.ui.vendor.model.CompanyInfoModel
import kotlinx.android.synthetic.main.activity_pre_blending.*
import kotlinx.android.synthetic.main.company_info_dialog.*

class CompanyInfoDialog(context: Context, val desireQty: Float): Dialog(context) {

    private var onDialogDismiss: ((CompanyInfoModel)-> Unit)?= null

    fun setDialogDismissListener(action: ((CompanyInfoModel)-> Unit)){
        onDialogDismiss= action
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.company_info_dialog)

        edtCompanyName.setOnClickListener {
            setCompanyName()
        }
        btnSubmit.setOnClickListener {
            when{
                edtCompanyName.text.isBlank()-> context.showToast("Enter company name")
                edtAddress.text.isBlank()-> context.showToast("Enter company address")
                edtNetWeight.text.isBlank()-> context.showToast("Enter net weight")
                edtNetWeight.text.toString().toFloat() >= desireQty-> context.showToast("Loss quantity should be less than desired quantity")
                else -> {
                    onDialogDismiss?.invoke( CompanyInfoModel(edtCompanyName.text.toString(), edtAddress.text.toString(), edtNetWeight.text.toString().trim().toFloat()))
                    dismiss()
                }
            }
        }

    }



    private fun setCompanyName() {

        val popupMenu = PopupMenu(context,edtCompanyName)
         val list = mutableListOf<String>()
        list.add("Prakriti Organics India Private Limited")
        list.add("Patanjali Ayurved Private Limited")

        list?.forEach {
            popupMenu.menu.add(it)
        }

        popupMenu.setOnMenuItemClickListener { item ->
            edtCompanyName.setText(item.title.toString())
            true
        }
        popupMenu.show()
    }
}