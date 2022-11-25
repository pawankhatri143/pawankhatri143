package com.bharuwa.sumadhu.ui.vendor.model

data class AddManufacturerResponse(
	val result: DispatchedCompany? = null,
	val ex: Any? = null,
	val message: String? = null,
	val result2: Any? = null,
	val status: String? = null
)

data class DispatchedCompany(
	val manufacturerName: String? = null,
	val manufacturerContact: String? = null,
	val flora: String? = null,
	val vendorId: String? = null,
	val blendingId: String? = null,
	val vendorWarehouse: String? = null,
	val completeDate: String? = null,
	val manufacturerWarehousePincode: String? = null,
	val manufacturerWarehouse: String? = null,
	val blendingStatus: String? = null,
	val manufacturerWarehouseState: String? = null,
	val batchCode: String? = null,
	val afterBlendingId: String? = null,
	val manufacturerId: String? = null,
	val netWeight: Double? = null,
	val manufacturerWarehouseContact: String? = null,
	val totalWeight: Double? = null,
	val manufacturerWarehouseAddress: String? = null,
	val blendingBatchId: String? = null,
	val desiredQuantity: Int? = null,
	val manufacturerWarehouseCity: String? = null,
	val remark1: String? = null,
	val remark3: String? = null,
	val status: String? = null,
	val remark2: String? = null
)

