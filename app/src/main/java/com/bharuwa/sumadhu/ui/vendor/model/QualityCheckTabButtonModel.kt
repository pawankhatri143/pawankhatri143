package com.bharuwa.sumadhu.ui.vendor.model

data class QualityCheckTabButtonModel(
    val status : String,
    val result : QualityCheckTabButtonResult,
    val message : String
)

data class QualityCheckTabButtonResult (
    val inwardId : String,
    val qualityTestStatus : String,
    val remark : String,
    val remarkOther : String
)