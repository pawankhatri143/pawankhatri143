package com.bharuwa.sumadhu.network.model

data class DashboardModel(
    var boxCount:Int?,
    var totalBoxCount:Int?,
    var emptyBoxCount:Int?,
    var boxes:List<BoxesModel>?,
    var emptyBoxes:List<BoxesModel>?,
    var farm:LocationsModel?,
    var date:String?,

)