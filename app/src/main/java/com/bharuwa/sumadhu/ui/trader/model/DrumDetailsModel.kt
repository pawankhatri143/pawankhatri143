package com.bharuwa.sumadhu.ui.trader.model

data class DrumDetailsModel(
    val drumId: String? = null,
    val drumNetWeight: Float? = null,
    val drumHoneyWeight: Float? = null,
//	val orderId: String? = null,
    val emptyDrumWeight: Float? = null,
    var drumAmount: Float? = null,
    var drumFillFarmId: String? = null,
    var honeyPerKgAmount: Float? = null,
    val drumEmptyDate: String? = null,
    val traderId: String? = null,
    val drumCode: String? = null,
    val drumFillDate: String? = null,
//	val id: String? = null,
    val drumEmptyStatus: String? = null,
    val drumFillStatus: String? = null,
    var drumFillFarmerId: String? = null,
    val status: String? = null
)

