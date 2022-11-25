package com.bharuwa.sumadhu.network.model

data class StoreHoneyModel(
	val actualHoneyNetWeight: String? = "0",
	val actualHoneyTotalWeight: String? = "0",
	val containerCode: String? = null,
	val containerCount: String? = null,
	val containerFillFarmId: String? = null,
	val containerFillFarmerId: String? = null,
	val containerHoneyWeight: Double? = 0.0,
	val containerNetWeight: Double? = 0.0,
	val containerType: String? = null,
	val flora: String? = null
)