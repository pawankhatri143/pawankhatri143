package com.bharuwa.sumadhu.ui.trader2.model

data class ContainerResponse(
	val result: List<ContainerDetails?>? = null,
	val ex: Any? = null,
	val message: String? = null,
	val result2: Any? = null,
	val status: String? = null
)

data class ContainerDetails(
	val unit: String? = null,
	val containerType: String? = null,
	val containerCapacity: Int? = null,
	val emptyContainerWeight: Float? = null,
	val containerId: String? = null,
	val status: String? = null,
	var manualWeigth: String = "0", // for personal use
	var numberOfBucket: String = "0" // for personal use
)

