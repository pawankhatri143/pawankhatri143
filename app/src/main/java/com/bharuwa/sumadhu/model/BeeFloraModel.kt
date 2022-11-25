package com.bharuwa.sumadhu.model

data class BeeFloraModel(
    var bee: List<Bee>?,
    var flora: List<Flora>?
)

data class Bee(
    var name_hi: String?,
    var name: String?,
    var code: String?
)/*{
    override fun toString()=name.toString()
}
*/
data class Flora(
    var nameHi: String?,
    var nameEn: String?,
    var code: String?,
    var category: String?,
    var catHi: String?
)