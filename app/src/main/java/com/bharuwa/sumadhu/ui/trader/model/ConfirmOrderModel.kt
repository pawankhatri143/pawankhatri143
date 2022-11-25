package com.bharuwa.sumadhu.ui.trader.model

data class ConfirmOrderModel(
	val result: List<ResultItem?>? = null,
	val ex: Any? = null,
	val message: String? = null,
	val result2: ResultItem2? = null,
	val status: String? = null
)

data class ResultItem2(
	val orderId: String? = null,
	val drumCount: String? = null,
	val totalHoneyWeight: String? = null,
	val totalNetWeight: String? = null,
	val totalEmptyDrumWeight: String? = null,
	val farmerId: String? = null,
	val farmId: String? = null,
	val honeyPricePerKg: String? = null,
	val totalOrderAmount: String? = null,
	val orderStatus: String? = null,
)
data class ResultItem(
	val drumId: String? = null,
	val drumNetWeight: Double? = null,
	val drumHoneyWeight: Double? = null,
	val orderId: String? = null,
	val emptyDrumWeight: Double? = null,
	val drumAmount: Double? = null,
	val drumFillFarmId: String? = null,
	val honeyPerKgAmount: Double? = null,
	val drumEmptyDate: String? = null,
	val traderId: String? = null,
	val orderDetailId: String? = null,
	val drumCode: String? = null,
	val drumFillDate: String? = null,
	val drumEmptyStatus: String? = null,
	val drumFillStatus: String? = null,
	val drumFillFarmerId: String? = null,
	val status: String? = null
)

