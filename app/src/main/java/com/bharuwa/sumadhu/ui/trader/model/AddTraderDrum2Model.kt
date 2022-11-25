package com.bharuwa.sumadhu.ui.trader.model

data class AddTraderDrum2Model(
	val result: List<DrumRespone?>? = null,
	val ex: Any? = null,
	val message: String? = null,
	val result2: Any? = null,
	val status: String? = null
)

data class DrumRespone(
	val drumID: String? = null,
	val drumCode: String? = null,
	val createdAt: String? = null,
	val drumStatus: String? = null,
	val drumValidity: String? = null,
	val traderId: String? = null,
	val drumCapacity: String? = null,
	val status: String? = null
)

