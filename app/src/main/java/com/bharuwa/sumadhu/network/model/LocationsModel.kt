package com.bharuwa.sumadhu.network.model

data class LocationsModel(

    var _id:String?,
    var userId:String?,
    var flora:List<String>?,
    var beeName:String?,
    var name:String?,
    var address:String?,
    var city:String?,
    var mobileNumber:String?,
    var district:String?,
    var state:String?,
    var pincode:String?,
    var latitude:Double?,
    var longitude:Double?,
    var default:Boolean?,
    var createdAt:String?,
    var images: List<String>?= null
)