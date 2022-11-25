package com.bharuwa.sumadhu.ui.trader2.model

data class TraderDashboardResponse(
	val result: TraderBusnessOverView? = null,
	val message: String? = null,
	val status: String? = null
)

data class TraderBusnessOverView(
	val farmerCount: Int? = null,
	val orderCount: Int? = null,
	val dispatchOrders: Int? = null
)

