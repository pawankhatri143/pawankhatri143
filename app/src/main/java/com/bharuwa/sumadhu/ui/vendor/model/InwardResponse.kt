package com.bharuwa.sumadhu.ui.vendor.model

data class InwardResponse(
	val result: List<ResultItem?>? = null,
	val ex: Any? = null,
	val message: String? = null,
	val result2: Any? = null,
	val status: String? = null
)

data class ResultItem(
	val disptchId: String? = null,
	val poAmount: Int? = null,
	val dispatchType: String? = null,
	val vehicalDispatchId: String? = null,
	val dispatchHoneyPricePerKG: Int? = null,
	val dispatchDate: String? = null,
	val flora: String? = null,
	val vendorId: String? = null,
	val invoiceAmount: Int? = null,
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
	val dispatchTotalHoneyPrice: Int? = null,
	val remark5: Any? = null,
	val remark4: String? = null,
	val driverName: String? = null,
	val remark1: String? = null,
	val poNumber: String? = null,
	val remark3: String? = null,
	val traderName: String? = null,
	val remark2: String? = null,
	val status: String? = null
)

