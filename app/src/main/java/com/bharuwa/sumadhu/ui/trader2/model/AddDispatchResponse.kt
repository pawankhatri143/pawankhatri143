package com.bharuwa.sumadhu.ui.trader2.model

data class AddDispatchResponse(
	val result: AddDispatchDetails? = null,
	val ex: Any? = null,
	val message: String? = null,
	val result2: Any? = null,
	val status: String? = null
)

data class AddDispatchDetails(
	val disptchId: String? = null,
	val poAmount: Double? = null,
	val dispatchType: String? = null,
	val dispatchHoneyPricePerKG: Double? = null,
	val dispatchDate: String? = null,
	val flora: String? = null,
	val vendorId: String? = null,
	val invoiceAmount: Double? = null,
	val traderId: String? = null,
	val vendorWarehouse: String? = null,
	val driverNumber: String? = null,
	val traderOrders: List<String?>? = null,
	val dispatchHoneyWeight: Double? = null,
	val invoiceNumber: String? = null,
	val traderDispatchId: String? = null,
	val dispatchStatusDate: String? = null,
	val vehicalLodadedBy: String? = null,
	val transport: String? = null,
	val vendorName: String? = null,
	val vehicalNumber: String? = null,
	val dispatchStatus: String? = null,
	val dispatchNetWeight: Double? = null,
	val dispatchTotalHoneyPrice: Double? = null,
	val remark5: String? = null,
	val remark4: String? = null,
	val driverName: String? = null,
	val remark1: String? = null,
	val poNumber: String? = null,
	val remark3: String? = null,
	val traderName: String? = null,
	val remark2: String? = null,
	val status: String? = null
)

