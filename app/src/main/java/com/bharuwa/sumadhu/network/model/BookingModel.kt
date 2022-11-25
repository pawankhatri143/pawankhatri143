package com.bharuwa.sumadhu.network.model

data class BookingModel (
    val actionResult: BookingActionResult
)
data class BookingActionResult(
    val success:Boolean,
    val errMsg:String?,
    val result:List<List<BookingResult>>?
)
data class BookingResult(
    val userId: String?,
    val groupNumber: String?,
    val isGrouped: Boolean?,
    val isGrouphead: Boolean?,
    val otherGroupstatus: Boolean?,
    val bookingNumber: String?,
    val bookingType: String?,
    val periodFrom: String?,
    val periodUpto: String?,
    val bookingFor: String?,
    val clinicId: String?,
    val clinicName: String?,
    val bookingStatus: String?,
    val slotBookedOn: String?,
    val doctorName: String?,
    val slotBookingId: String?,
    val isRemarks: Boolean?,
    val isUnBillItem: Boolean?,
    val isBillItem: Boolean?
)