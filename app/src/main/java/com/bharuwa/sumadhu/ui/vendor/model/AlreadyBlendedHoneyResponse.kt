package com.bharuwa.sumadhu.ui.vendor.model

data class AlreadyBlendedHoneyResponse(
	val result: List<BlendedItem>? = null,
	val ex: Any? = null,
	val message: String? = null,
	val result2: Any? = null,
	val status: String? = null
)

data class BlendedItem(
	val blendingStatus: String? = null,
	val flora: String? = null,
	/*val manufacturId: String? = null,
	val manufacturName: String? = null,
	val honeyNetWeight: String? = null,
	val honeyWeight: String? = null,
*/
	val vendorId: String? = null,
	val blendingId: String? = null,
	val batchId: String? = null,
	val remark5: Any? = null,
	val remark4: Any? = null,
	val location: String? = null,
	val desiredQuantity: Int? = null,
	val remark6: Any? = null,
	val remark1: String? = null,
	val blendingDate: String? = null,
	val remark3: Any? = null,
	val status: String? = null,
	val isChecked: Boolean = false,
	val remark2: Any? = null,

  val manufacturerWarehouseAddress: String? = null,
  val manufacturerWarehouseCity: String? = null,
  val manufacturerWarehouseState: String? = null,
  val manufacturerWarehousePincode: String? = null,
  val manufacturerWarehouseContact: String? = null,
  val totalWeight: String? = null,
  val netWeight: String? = null,

	val afterBlendingId: String? = null,
	val blendingBatchId: String? = null,
	val vendorWarehouse: String? = null,
	val batchCode: String? = null,
	val manufacturerId: String? = null,
	val manufacturerName: String? = null,
	val manufacturerContact: String? = null,
	val manufacturerWarehouse: String? = null,

)

