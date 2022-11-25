package com.bharuwa.sumadhu.ui.trader.model

data class FarmDetailsModel(
	val boxes: List<BoxesItem?>? = null,
	val profile: Profile? = null,
	val farm: Farm? = null
)

data class Farm(
	val pincode: String? = null,
	val address: String? = null,
	val beeCode: Any? = null,
	val city: String? = null,
	val mobileNumber: String? = null,
	val latitude: Double? = null,
	val flora: List<String?>? = null,
	val userId: String? = null,
	val createdAt: String? = null,
	val jsonMemberDefault: Boolean? = null,
	val district: String? = null,
	val name: String? = null,
	val floraCodes: List<String?>? = null,
	val _id: String? = null,
	val state: String? = null,
	val beeName: String? = null,
	val longitude: Double? = null
)

data class BoxesItem(
	val createdAt: String? = null,
	val locationId: String? = null,
	val id: String? = null,
	val userId: String? = null,
	val barcode: String? = null,
	val status: String? = null
)

data class Profile(
	val pincode: Any? = null,
	val code: Any? = null,
	val address: String? = null,
	val gender: String? = null,
	val city: String? = null,
	val mobileNumber: String? = null,
	val beeSpecies: Any? = null,
	val boxCount: Int? = null,
	val district: String? = null,
	val name: String? = null,
	val id: String? = null,
	val userType: Any? = null,
	val state: String? = null,
	val email: String? = null
)

