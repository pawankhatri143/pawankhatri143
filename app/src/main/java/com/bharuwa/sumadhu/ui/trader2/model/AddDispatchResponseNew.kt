package com.bharuwa.sumadhu.ui.trader2.model

data class AddDispatchResponseNew(
	val result: AddDispatchDetailsNew?,
	val ex: Any? = null,
	val message: String? = null,
	val result2: Any? = null,
	val status: String? = null
)

data class AddDispatchDetailsNew(
	val vehicalDispatchId: String?=null,
	val vehicalId: String?=null,
	val traderId: String?=null,
	val traderName: String?=null,
	val vendorId: String?=null,
	val vendorName: String?=null,
	val vehicalType: String?=null,
	val transport: String?=null,
	val vehicalNumber: String?=null,
	val driverName: String?=null,
	val driverNumber: String?=null,
	val startPoint: String?=null,
	val endPoint: String?=null,
	val invoiceNumber: String?=null,
	val poNumber: String?=null,
//	val dispatchOrders: DispatchOrders?=null,
	val vehicalStartDate: String?=null,
	val vehicalRunningStatus: String?=null,
	val vendorWarehouseId: String?=null,
	val vendorWarehouseName: String?=null,
	val status: String?=null,
	val remark1: String?=null,
	val remark2: String?=null,
	val remark3: String?=null,
	val remark4: String?=null,
	val remark5: String?=null
)


