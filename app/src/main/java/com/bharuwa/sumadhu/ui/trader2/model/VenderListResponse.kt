package com.bharuwa.sumadhu.ui.trader2.model

data class VenderListResponse(
	val result: List<VendorDetail?>? = null,
	val ex: Any? = null,
	val message: String? = null,
	val result2: Any? = null,
	val status: String? = null
)

data class VendorDetail(
	val pincode: String? = null,
	val address: String? = null,
	val city: String? = null,
	val gstno: String? = null,
	val phoneFirm: String? = null,
	//val vendorId: String? = null,
	val name: String? = null,
	val userId: String? = null,
	//val vendorName: String? = null,
	val warehouse: List<String?>? = null,
	val vendorCode: String? = null,
	val userCode: String? = null,
	val district: String? = null,
	val firmName: String? = null,
	val block: String? = null,
	val state: String? = null,
	val phoneVendor: String? = null,
	val landmark: String? = null,
	val email: String? = null,
	val createDate: String? = null,
	val status: String? = null
)/*{
	override fun toString()= vendorName.toString()
}*/

