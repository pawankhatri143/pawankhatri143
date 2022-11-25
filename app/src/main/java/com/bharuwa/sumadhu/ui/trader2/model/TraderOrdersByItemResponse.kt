package com.bharuwa.sumadhu.ui.trader2.model

data class TraderOrdersByItemResponse(
	val result: List<ResultItem4?>? = null,
	val ex: Any? = null,
	val message: String? = null,
	val result2: Any? = null,
	val status: String? = null
)

data class ResultItem4(
	val containerAmount: Double? = null,
	val containerFillFarmId: String? = null,
	val purchaseBy: String? = null,
	val orderId: String? = null,
	val containerType: String? = null,
	val containerEmptyDate: String? = null,
	val honeyPerKgAmount: Double? = null,
	val emptyContainerWeight: Double? = null,
	val traderId: String? = null,
	val orderDetailId: String? = null,
	val containerFillFarmerId: String? = null,
	val containerCode: String? = null,
	val containerNetWeight: Double? = null,
	val containerId: Any? = null,
	val containerEmptyStatus: String? = null,
	val containerFillDate: String? = null,
	val containerHoneyWeight: Double? = null,
	val containerFillStatus: String? = null,
	val extraRemarkarr6: List<String?>?= null,
	val status: String? = null,
	val flora: String? = null

)

