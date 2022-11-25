package com.bharuwa.sumadhu.ui.viewmodel

data class UploadImageResponse(
    val _id: String,
    val address: String,
    val beeCode: String,
    val beeName: String,
    val city: String,
    val createdAt: String,
    val default: Boolean,
    val district: String,
    val flora: List<String>,
    val floraCodes: List<String>,
    val images: List<String>,
    val latitude: Double,
    val longitude: Double,
    val mobileNumber: String,
    val name: String,
    val pincode: String,
    val state: String,
    val userId: String
)