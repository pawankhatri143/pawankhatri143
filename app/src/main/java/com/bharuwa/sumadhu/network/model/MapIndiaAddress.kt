package com.bharuwa.sumadhu.network.model

data class MapIndiaAddress(
    var responseCode: String?,
    var version: String?,
    var results: List<Result>?
)
data class Result(
    var houseNumber: String?,
    var houseName: String?,
    var poi: String?,
    var poi_dist: String?,
    var street: String?,
    var street_dist: String?,
    var subSubLocality: String?,
    var subLocality: String?,
    var locality: String?,
    var village: String?,
    var district: String?,
    var subDistrict: String?,
    var city: String?,
    var state: String?,
    var pincode: String?,
    var lat: String?,
    var lng: String?,
    var area: String?,
    var formatted_address: String?
)