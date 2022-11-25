package com.bharuwa.sumadhu.ui.trader2.model

 data class SearchTraderModel(
	val result: Result? = null,
	val ex: Any? = null,
	val message: String? = null,
	val result2: List<Result2Item?>? = null,
	val status: String? = null
)

data class Result2Item(
	val pincode: String? = null,
	val address: String? = null,
	val city: String? = null,
	val mobileNumber: String? = null,
	val latitude: Double? = null,
	val flora: List<String?>? = null,
	val farmId: String? = null,
	val userId: String? = null,
	val createdAt: String? = null,
	val isDefault: Boolean? = null,
	val district: String? = null,
	val name: String? = null,
	val state: String? = null,
	val beeName: String? = null,
	val longitude: Double? = null,
	var clickedOnLocation: Boolean? = false
)

data class Result(
	val bankAccount: String? = null,
	val gender: String? = null,
	val pincode: String? = null,
	val userType: String? = null,
	val city: String? = null,
	val address: String? = null,
	val regDate: String? = null,
	val panNumber: String? = null,
	val traderId: String? = null,
	val name: String? = null,
	val gstNumber: String? = null,
	val mobileNumber: String? = null,
	val traderStatus: String? = null,
	val district: String? = null,
	val block: String? = null,
	val state: String? = null,
	val ifscCode: String? = null,
	val email: String? = null,
	val status: Int? = null
)

