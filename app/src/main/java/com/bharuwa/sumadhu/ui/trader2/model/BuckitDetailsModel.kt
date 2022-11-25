package com.bharuwa.sumadhu.ui.trader2.model

data class BuckitDetailsModel(
	val containerAmount: Double? = null,
	val containerFillFarmId: String? = null,
	val orderId: String? = null,
	val containerType: String? = null,
	val containerEmptyDate: String? = null,
	val honeyPerKgAmount: Double? = null,
	val emptyContainerWeight: Double? = null,
	val traderId: String? = null,
	val containerFillFarmerId: String? = null,
	val _id: String? = null,
	val containerCode: String? = null,

	val containerNetWeight: Double? = null,
	val containerId: String? = null,
	val containerEmptyStatus: String? = null,
	val containerFillDate: String? = null,
	val containerHoneyWeight: Double? = null,
	val containerFillStatus: String? = null,
	val status: String? = null,
	val purchaseBy: String? = null,
	val containerCount: String? = "1",
	var actualHoneyTotalWeight: String? = "0",
	var actualHoneyNetWeight: String? = "0",

	val flora: String? = null,
)

