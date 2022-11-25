package com.bharuwa.sumadhu.network.model

data class CityStateVillageModel (
    val state_id: String,
    val district_id: String,
    val tehsil_id: String,
    val name: String?,
    val short_name: String?
)