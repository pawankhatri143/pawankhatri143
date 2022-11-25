package com.bharuwa.sumadhu.ui.vendor.model

data class BlendingLocationModel(
    val status : String,
    val result : List<BlendingLocationResult>?,
    val message : String
)

data class BlendingLocationResult (
    val warehouseId : String,
    val warehouseCode : String,
    val warehouseName : String,
    val warehousePhone : String,
    val warehouseEmail : String,
    val warehouseLocation : String,
    val warehouseAddress : String,
    val warehouseLandmark : String,
    val warehouseCity : String,
    val warehouseBlock : String,
    val warehouseDistrict : String,
    val warehouseState : String,
    val warehousePincode : String,
    val warehouseCapacity : String,
    val warehouseVendorId : String,
    val createDate : String,
    val status : String
)
