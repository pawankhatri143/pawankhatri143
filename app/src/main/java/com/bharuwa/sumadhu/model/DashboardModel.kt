package com.bharuwa.sumadhu.model

data class DashboardModel(
    var boxCount:Int?,
    var boxes:List<BoxesModel>?,
    var farm:LocationsModel?,
    var date:String?,
)