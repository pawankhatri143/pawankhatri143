package com.bharuwa.sumadhu.ui.vendor.model

import com.bharuwa.sumadhu.network.model.Flora

data class FloraModel(
    val status : String,
    var result: MutableList<Flora>?,
    val message : String
)

data class Flora (
    val floraId : String,
    val category : String,
    val catHi : String,
    val nameEn : String,
    val nameHi : String,
    val code : String,
    val remark : String,
    val sortOrder : Int,
    val createDate : String,
    val status : String
)