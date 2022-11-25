package com.bharuwa.sumadhu.ui.vendor.model

data class AddProceedBlendingModel(
    val status : String,
    val result : AddProceedBlendingResult,
    val message : String
)

    data class AddProceedBlendingResult (
    val blendingId : String,
    val batchId : String,
    val vendorId : String,
    val location : String,
    val desiredQuantity : Int,
    val flora : String,
    val blendingDate : String,
    val status : String,
    val blendingStatus : String,
    val remark1 : String?= "N/A",
    val remark2 : String,
    val remark3 : String,
    val remark4 : String,
    val remark5 : String,
    val remark6 : String
)