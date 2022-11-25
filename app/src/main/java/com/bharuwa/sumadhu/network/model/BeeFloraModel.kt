package com.bharuwa.sumadhu.network.model

data class BeeFloraModel(
    var bee: List<Bee>?,
    var flora: MutableList<Flora>?
)

data class Bee(
    /*var name_hi: String?,
    var name: String?,
    var code: String?,*/

    var beeId: String?,
    var beeCode: String?,
    var beeNameEn: String?,
    var beeNameHi: String?,
    var beeCategoryEn: String?,
    var beeCategoryHi: String?,

)

data class Flora(
    /*var nameHi: String?,
    var nameEn: String?,
    var code: String?,
    var category: String?,
    var catHi: String?,*/

    val floraId: String?,
    val category: String?,
    val catHi: String?,
    val nameEn: String?,
    val nameHi: String?,
    val code: String?,

)