package com.bharuwa.sumadhu.network

import com.bharuwa.sumadhu.model.*
import retrofit2.Response
import retrofit2.http.*


interface ApiEndPoint {

    @POST("user/login")
    suspend fun login(@Query("mobileNumber") mobileNumber: String): Response<LoginResponse>

    @POST("user/verify-otp")
    suspend fun verifyOTP(@Query("mobileNumber") mobileNumber: String,@Query("otp") otp: String): Response<OtpVarificationResponse>

    @GET("sendDKDOtp")
    suspend fun sendOtp(@Query("mobileNumber") mobileNumber: String): Response<LoginResponse>

    @GET("updateVersion")
    suspend fun checkVersionUpdate(): Response<VersionStatus>

    @POST("user")
    suspend fun userRegistration(@Body body: HashMap<String, String>): Response<RegistrationResponse>

    @PUT("user")
    suspend fun profileUpdate(@Body body: HashMap<String, String>): Response<RegistrationResponse>

    @GET("box")
    suspend fun getBoxes(@Query("userId") userId: String,@Query("locationId") locationId: String): Response<List<BoxesModel>>

    //  Locations
    @GET("location")
    suspend fun getLocation(@Query("userId") userId: String): Response<List<LocationsModel>>


    @POST("location")
    suspend fun saveLocation(@Body body: HashMap<String, Any>): Response<LocationsModel>

    @PUT("location/default")
    suspend fun saveDefaultLocation(@Query("locationId") locationId: String,@Query("userId") userId: String): Response<LocationsModel>



    //   Bee and flora
   /* @GET("location/flora")
    suspend fun getBeeAndFlora(): Response<BeeFloraModel>*/

    @GET("location/floraWithCategory")
    suspend fun getBeeAndFlora(): Response<BeeFloraModel>


    @POST("box")
    suspend fun saveBox(@Body body: HashMap<String, String>): Response<BoxesModel>

    @POST("box/bulk")
    suspend fun saveBulkBoxes(@Body scanedBarcodeArray: ArrayList<ScanedBoxesModel>): Response<LoginResponse>

    @PUT("box/empty")
    suspend fun emptyBox(@Query("barcode") barcode: String,@Query("userId") userId: String): Response<BoxesModel>

   @PUT("box/empty-bulk")
    suspend fun emptyBoxBulk(@Query("boxIds") boxIds :List<String>): Response<LoginResponse>



    @PUT("box/relocate")
    suspend fun relocateBox(@Query("userId") userId: String): Response<LoginResponse>

    @PUT("box/relocate-bulk")
    suspend fun relocateBoxBulk(@Query("boxIds") boxIds :List<String>,@Query("locationId") locationId: String): Response<LoginResponse>


    // state district and city
    @GET("user/states")
    suspend fun getState(): Response<List<CityStateVillageModel>>

    @GET("user/districts")
    suspend fun getDistrict(@Query("stateId") stateId: String): Response<List<CityStateVillageModel>>

    @GET("user/tehsils")
    suspend fun getCity(@Query("districtId") districtId: String): Response<List<CityStateVillageModel>>

   @GET("box/activeFarms")
    suspend fun getActiveFarms(@Query("userId") userId: String): Response<List<DashboardModel>>

}