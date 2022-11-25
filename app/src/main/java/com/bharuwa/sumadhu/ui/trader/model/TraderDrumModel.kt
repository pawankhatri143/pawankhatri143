package com.bharuwa.sumadhu.ui.trader.viewModel

data class TraderDrumModel(
	val result: List<DrumItem?>? = null,
	val ex: Any? = null,
	val message: Any? = null,
	val result2: Any? = null,
	val status: String? = null
)

data class DrumItem(
	val drumID: String? = null,
	val drumCode: String? = null,
	val createdAt: String? = null,
	val drumStatus: String? = null,
	val drumValidity: String? = null,
	val traderId: String? = null,
	val drumCapacity: Float? = null,
	val status: String? = null
)

