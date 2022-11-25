package com.bharuwa.sumadhu.ui.vendor.model

import com.bharuwa.sumadhu.network.model.Bee

data class BeeModel(
    val status : String,
    val result : List<Bee>,
    val message : String
)

data class Bee (
    val beeId : String,
    val beeCode : String,
    val beeNameEn : String,
    val beeNameHi : String,
    val beeCategoryEn : String,
    val beeCategoryHi : String,
    val remark : String,
    val sortOrder : Int,
    val createDate : String,
    val status : String
)
