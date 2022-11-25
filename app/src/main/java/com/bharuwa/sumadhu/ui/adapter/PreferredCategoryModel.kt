package com.bharuwa.sumadhu.ui.adapter

data class PreferredCategoryModel(
    val masterCode: String? = null,
    val name: String? = null,
    val categoryList: List<PreferredSubCategoryModel>? = null
)

data class PreferredSubCategoryModel(
    val masterCode: String? = null,
    val name: String? = null
)
