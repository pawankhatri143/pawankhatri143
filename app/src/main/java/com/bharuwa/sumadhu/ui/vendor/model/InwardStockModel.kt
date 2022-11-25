package com.bharuwa.sumadhu.ui.vendor.model

data class InwardStockModel(
    val status : String?= null,
    val result : List<InwardStockResult>?= null,
   /* val ex : String,
    val result2 : String,*/
    val message : String?= null,
)

data class InwardStockResult (
    val traderDispatchId : String?= null,
    val disptchId : String?= null,
    val traderId : String?= null,
    val traderName : String?= null,
    val vendorId : String?= null,
    val vendorName : String?= null,
    val vendorWarehouse : String?= null,
    val traderOrders : List<String>?= null,
    val flora : String?= null,
    val poNumber : String?= null,
    val poAmount : Double?= null,
    val invoiceNumber : String?= null,
    val invoiceAmount : Float = 0f,
    val vehicalDispatchId : String?= null,
    val vehicalNumber : String?= null,
    val driverName : String?= null,
    val driverNumber : String?= null,
    val containerCount : String?= null,
    val transport : String?= null,
    val dispatchNetWeight : Double?= null,
    val dispatchHoneyWeight : Double?= null,
    val dispatchHoneyPricePerKG : Float?= null,
    val dispatchTotalHoneyPrice : Float?= null,
    val dispatchType : String?= null,
    val dispatchDate : String?= null,
    val dispatchStatus : String?= null,
    val dispatchStatusDate : String?= null,
    val vehicalLodadedBy : String?= null,
    val remark1 : String?= null,
    val remark2 : String?= null,
    val remark3 : String?= null,
    val remark4 : String?= null,
    val remark5 : String?= null,
    val status : String?= null,

    val farmerId : String?= null,// farmer name
    val extraRemark1 : String?= null,// farmer phone number
    val extraRemarkarr6 : List<String>?= null, // bee name
    val actualHoneyNetWeight : String?= null, // honey weigth

    val orderId : String?= null,
    val orderNo : String?= null,
)
