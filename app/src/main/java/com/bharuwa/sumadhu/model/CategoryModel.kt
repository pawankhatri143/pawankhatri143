package com.bharuwa.sumadhu.model


data class CategoryModel(

    val cname: String? = null,
    val cmasterCode: String? = null,
    val masterCode: String? = null,
    val name: String? = null,
    val subCategoryImage: String? = null,
    val categoryList: List<SubCategoryModel>? = null
){
    override fun toString(): String = name.toString()
}

data class SubCategoryModel(

    val masterCode: String? = null,
    val subCategoryImage: String? = null,
    val name: String? = null
){
    override fun toString(): String = name.toString()
}

data class MergeCategory(
    val masterCodeCategory: String? = null,
    val nameCategory: String? = null,

    val masterCode: String? = null,
    val name: String? = null,
)