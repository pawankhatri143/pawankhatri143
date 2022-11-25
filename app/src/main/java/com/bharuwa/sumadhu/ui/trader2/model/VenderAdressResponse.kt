package com.bharuwa.sumadhu.ui.trader2.model

data class VenderAdressResponse(
	val result: List<WarehouseDetails?>? = null,
	val ex: Any? = null,
	val message: String? = null,
	val result2: Any? = null,
	val status: String? = null
)

data class WarehouseDetails(
	val warehouseAddress: String? = null,
	val warehouseState: String? = null,
	val warehousePincode: String? = null,
	val warehouseLocation: String? = null,
	val warehouseName: String? = null,
	val warehouseCode: String? = null,
	val warehouseLandmark: String? = null,
	val warehouseCity: String? = null,
	val warehouseBlock: String? = null,
	val warehouseId: String? = null,
	val warehousePhone: String? = null,
	val warehouseDistrict: String? = null,
	val warehouseCapacity: String? = null,
	val warehouseEmail: String? = null,
	val warehouseVendorId: String? = null,
	val createDate: String? = null,
	val status: String? = null
)

