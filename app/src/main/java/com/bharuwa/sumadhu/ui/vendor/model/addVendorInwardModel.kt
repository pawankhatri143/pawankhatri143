package com.bharuwa.sumadhu.ui.vendor.model

data class addVendorInwardModel(
    val status : String,
    //val result : addVendorInwardResult,
    //val ex : String,
    //val result2 : String,
    val message : String
)

data class addVendorInwardResult (
    val inwardId : String,
    val inwardCode : String,
    val traderDispatchId : String,
    val disptchId : String,
    val traderId : String,
    val traderName : String,
    val vendorId : String,
    val vendorName : String,
    val vendorWarehouse : String,
    val traderOrders : List<String>,
    val poNumber : String,
    val floraName : String,
    val vehicalDispatchId : String,
    val vehicalNumber : String,
    val driverName : String,
    val driverMobile : String,
    val totalContainer : Int,
    val remainingContainer : Int,
    val honeyTotalWeight : Double,
    val containerWeight : Double,
    val netWeight : Double,
    val totalAmount : Double,
    val recievedBy : String,
    val inwardDetailId : String,
    val inwardDate : String,
    val inwardStatus : String,
    val status : String,
    val qualityTestStatus : String,
    val remark : String,
    val remarkOther : String
)