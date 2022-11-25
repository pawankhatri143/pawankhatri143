package com.bharuwa.sumadhu.ui.trader.model

data class AddNewTraderDrumModel(
	val addNewTraderDrumModel: List<AddNewTraderDrumModelItem?>? = null
)

data class AddNewTraderDrumModelItem(
	val drumCode: String? = null,
	val createdAt: String? = null,
	val drumStatus: String? = null,
	val id: String? = null,
	val drumValidity: String? = null,
	val traderId: String? = null,
	val drumCapacity: String? = null,
	val status: String? = null
)

