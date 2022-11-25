package com.bharuwa.sumadhu.model

data class MyAddress(
    var latitude: String,
    var longitude: String,
    var address: String?,
    var city: String?,
    var district: String?,
    var state: String?,
    var postalCode: String?,
    var knownName: String?,
    var subAdminArea: String?

) //: Serializable