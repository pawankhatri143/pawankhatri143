package com.bharuwa.sumadhu.ui.vendor.model

data class QualityCheckAllModel(
    val status : String,
    val result : List<QualityCheckAllResult>?,
    //val ex : String,
    //val result2 : String,
    val message : String
)

data class QualityCheckAllResult (
    val inwardId : String,
    var isChecked: Boolean,
    val inwardCode : String,
    val traderDispatchId : String,
    val disptchId : String,
    val traderId : String,
    val traderName : String,
    val vendorId : String,
    val vendorName : String,
    val vendorWarehouse : String,
    val traderOrders : List<String>,
    val poNumber : String? =null,
    val floraName : String,
    val vehicalDispatchId : String,
    val vehicalNumber : String,
    val driverName : String,
    val driverMobile : String,
    val totalContainer : Float?,
    val remainingContainer : String,
    val honeyTotalWeight : Double,
    val containerWeight : Double,

    val totalAmount : Double,
    val recievedBy : String,
    val inwardDetailId : String,
    val inwardDate : String,
    val inwardStatus : String,
    val status : String,
    val qualityTestStatus : String,
    val remark : String,
    val remarkOther : String,
    var reportUpload : Boolean = false,

    val netWeight : Float? = 0F,
    val remainHoneyTotalWeight : Float = 0F,
    var tempHoneyWeigth : Float? = 0F,
    var remainHoneyWeigth : Float? = 0F,
    //added by me
    var remainHoneyWeightForDispatch : Float = 0F,
    var alreadyAddedHoney : Float? = 0F,
    var imagePath : List<String>? = null

)