package com.bharuwa.sumadhu.ui.trader2.model

data class ConfirmTraderOrderResponse(
	val result: List<ResultItem?>? = null,
	val ex: Any? = null,
	val message: String? = null,
	val result2: OrderDetail? = null,
	val status: String? = null
)

data class OrderDetail(
	val orderNo: String? = null
)
data class ResultItem(
	val containerAmount: Double? = null,
	val containerFillFarmId: String? = null,
	val orderId: String? = null,
	val orderNo: String? = null,
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
	val status: String? = null
)

