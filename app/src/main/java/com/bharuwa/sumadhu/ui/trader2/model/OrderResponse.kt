package com.bharuwa.sumadhu.ui.trader2.model

data class OrderResponse(
	val result: List<ResultItem12?>? = null,
	val ex: Any? = null,
	val message: String? = null,
	val status: String? = null
)

data class ResultItem12(
	val honeyPricePerKg: Double? = null,
	val orderId: String? = null,
	val orderNo: String? = null,
	val purchaseFrom: String? = null,
	val orderStatus: String? = null,
	val farmId: String? = null,
	val orderTransferTo: Any? = null,
	val totalHoneyWeight: Double? = null,
	val traderId: String? = null,
	val recievedOrderId: String? = null,
	val farmerId: String? = null,
	val totalOrderAmount: Double? = null,
	val orderRecievedFrom: Any? = null,
	val totalEmptyContainerWeight: Double? = null,
	val totalNetWeight: Double? = null,
	val containerCount: Int? = null,
	val orderDate: String? = null,
	val remarks: String? = null,
	val flora: String? = null,
	val beekeeper: String? = null,
	val extraRemark1: String? = null,
	val extraRemarkarr6: List<String?>? = null,
	val extraRemark2: String? = null,
	val extraRemark3: String? = null,
	val extraRemark4: String? = null,
	val status: String? = null,
	val actualHoneyNetWeight: Float? = 0f,
	val remainHoneyWeight: Float? = 0f,
	val actualHoneyTotalWeight: Float? = 0f,

	//val netWeight : Float? = 0F,
	var tempHoneyWeigth : Float? = 0F,
	var remainHoneyWeigth : Float? = 0F,
	var remainHoneyWeightForDispatch : Float? = 0F,
	var alreadyAddedHoney : Float? = 0F,
	var isChecked: Boolean,


)

