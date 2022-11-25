package com.bharuwa.sumadhu.ui.vendor.model

data class QualityCheckTabDetailModel(
    val status : String,
    val result : List<QualityCheckTabDetailResult>?,
    val message : String
)

data class QualityCheckTabDetailResult (
    val inwardDetailId : String,
    val inwardId : String,
    val containerType : String,
    val containerCapacity : Int,
    val containerHoneyWeight : Double,
    val containerEmptyWeight : Double,
    val containerNetWeight : Double,
    val containerAmount : Double,
    val containerFlora : String,
    val inwardDetailDate : String,
    val inwardDetailStatus : String
)