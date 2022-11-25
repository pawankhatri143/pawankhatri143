package com.bharuwa.sumadhu.network.model

data class LoginResponse (
    val mobileNumber: String?,
    val otp: String?,
    val message: String?
)