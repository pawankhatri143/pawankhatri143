package com.bharuwa.sumadhu.network.base

import com.bharuwa.sumadhu.network.model.*
import com.bharuwa.sumadhu.ui.trader.model.*
import com.bharuwa.sumadhu.ui.trader.viewModel.TraderDrumModel
import com.bharuwa.sumadhu.ui.trader2.model.*
import com.bharuwa.sumadhu.ui.vendor.model.*
import com.bharuwa.sumadhu.ui.viewmodel.UploadImageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
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

    /*//   Bee and flora
    @GET("location/flora")
    suspend fun getBeeAndFlora(): Response<BeeFloraModel>*/

    @GET("location/floraWithCategory")
    suspend fun getBeeAndFlora(): Response<BeeFloraModel>

    //Get Flora List
    @GET("getFloraList")
    suspend fun getFloraList(): Response<FloraModel>

    //Get Bee List
    @GET("getBeeList")
    suspend fun getBeeList(): Response<BeeModel>


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


    //trader

    @JvmSuppressWildcards
    @POST("addTradersDrum")
    suspend fun addNewTradersDrum(@Body body: List<HashMap<String, String>>): Response<AddTraderDrum2Model>

    @GET("getTraderDrums/{userId}")
    suspend fun getTraderAllDrumList(@Path("userId") userId: String): Response<TraderDrumModel>

    @GET("getTraderByMobile/{mobile}")
    suspend fun getTraderByMobile(@Path("mobile") mobile: String): Response<SearchTraderModel>

    @GET("dashboard/getFarmDetailsById")
    suspend fun getFarmDetails(@Query("farmId") farmId: String): Response<FarmDetailsModel>


    @GET("getTraderOrdersByItem/{bucketCode}/{traderId}")
    suspend fun getTraderOrdersByItem(@Path("bucketCode") bucketCode: String, @Path("traderId") traderId: String?): Response<TraderOrdersByItemResponse>


    //myorder = last all for flora
    @GET("getTraderOrders/{userID}/ALL/ALL")
    suspend fun getMyOrder(@Path("userID") mobile: String): Response<OrderResponse>

    @GET("getAllTraderOrders/{userID}/ALL")
    suspend fun getMyAllOrder(@Path("userID") mobile: String): Response<OrderResponse>


    //add dispatch activity PENDING order
    @GET("getTraderOrders/{userID}/PENDING/{flora}")
    suspend fun getMyPendingOrder(@Path("userID") mobile: String, @Path("flora") flora: String): Response<OrderResponse>

    @GET("getVendorList/Active")
    suspend fun getVendorList(): Response<VenderListResponse>

    @GET("getVendorWarehouse/{vendorID}")
    suspend fun getVendorWarehouseList(@Path("vendorID") vendorID: String?): Response<VenderAdressResponse>


    @GET("getTraderDispatchOrderList/Trader/{traderID}/ALL")
    suspend fun getTraderDispatchOrderList(@Path("traderID") vendorID: String): Response<DispatchedOrderResponse>


    @GET("getTraderDispatchOrderList/Trader/{traderID}/PENDING")
    suspend fun getTraderDispatchOrderPennding(@Path("traderID") vendorID: String): Response<DispatchedOrderResponse>


    @JvmSuppressWildcards
    @POST("addTraderDispatch")
    suspend fun addTraderDispatch(@Body body: HashMap<String, Any>): Response<AddDispatchResponse>

    @JvmSuppressWildcards
    @POST("assignVehicalToDispatch")
    suspend fun assignVehicalToDispatch(@Body body: HashMap<String, Any>): Response<AddDispatchResponseNew>


    @GET("getContainer")
    suspend fun getContainerList(): Response<ContainerResponse>



    @JvmSuppressWildcards
    @POST("addDrumToOrderList")
    suspend fun confirmOrder(@Body body: MutableList<BuckitDetailsModel>): Response<ConfirmTraderOrderResponse>


    @JvmSuppressWildcards
    @POST("addDrumToVendorOrderList")
    suspend fun confirmOrderFromVenderSide(@Body body: MutableList<BuckitDetailsModel>): Response<ConfirmTraderOrderResponse>


    //Vendor API
    //MyApp.get().getProfile()?._id!!


    @GET("getTraderDispatchOrderList/Vendor/{userID}/DISPATCHED")
    suspend fun inWardStockFromTrader(@Path("userID") userID: String): Response<InwardStockModel>

    @GET("getVendorOrderListForInward/{userID}/ALL")
    suspend fun inWardStockFromFarmer(@Path("userID") userID: String): Response<InwardStockModel>

    @POST("addVendorInward")
    suspend fun addVendorInward(@Body body: HashMap<String, Any>): Response<addVendorInwardModel>

    @POST("addFarmerOrderVendorInward")
    suspend fun addFarmerOrderVendorInward(@Body body: HashMap<String, Any>): Response<addVendorInwardModel>

    @GET("getVendorInward/{userID}")
    suspend fun getVendorInward(@Path("userID") userID: String): Response<QualityCheckAllModel>

    @GET("getVendorInwardForBlending/{userID}/PENDING")
    suspend fun getVendorInwardForBlending(@Path("userID") userID: String): Response<QualityCheckAllModel>


    @GET("getTraderDashboardCount/{userID}")
    suspend fun getTraderDashboardCount(@Path("userID") userID: String): Response<TraderDashboardResponse>

    @GET("getVendorInwardDetails/{inwardID}")
    suspend fun getVendorInwardDetails(@Path("inwardID") inwardID: String): Response<QualityCheckTabDetailModel>

    @POST("inwardHoneyTesting")
    suspend fun inwardHoneyTestingApproved(@Body body: HashMap<String, String>): Response<QualityCheckTabButtonModel>

    @POST("inwardHoneyTesting")
    suspend fun inwardHoneyTestingRejected(@Body body: HashMap<String, String>): Response<QualityCheckTabButtonModel>


    @POST("inwardStatusUpdate")
    suspend fun inwardStatusUpdate(@Body body: HashMap<String, String>): Response<QualityCheckTabButtonModel>

    @POST("inwardStatusUpdateFarmer")
    suspend fun inwardStatusUpdateFarmer(@Body body: HashMap<String, String>): Response<QualityCheckTabButtonModel>

    @Multipart
    @POST("saveTestReport")
    suspend fun uploadImage (@Part files: MultipartBody.Part,
    @Part("inwardId") inwardId: RequestBody
    ) : Response<DefaultResponse>


    @Multipart
    @POST("location/uploadFarmPhotos")
    suspend fun uploadFarmPhotos (@Part files: List<MultipartBody.Part?>, @Part("farmId") farmId: RequestBody) : Response<UploadImageResponse>


    @GET("getVendorWarehouse/{inwardID}")
    suspend fun getVendorWarehouse(@Path("inwardID") inwardID: String): Response<BlendingLocationModel>

    @POST("addHoneyBlending")
    suspend fun addHoneyBlending(@Body body: HashMap<String, Any>): Response<AddProceedBlendingModel>

    @POST("addAfterBlending")
    suspend fun addCompanyDetails(@Body body: HashMap<String, Any>): Response<AddManufacturerResponse>

    @GET("getVendorHoneyBlending/{userID}/PENDING")
    suspend fun getBlendedHoneyList(@Path("userID") userID: String): Response<AlreadyBlendedHoneyResponse>

    @GET("getVendorHoneyBlending/{userID}/ALL")
    suspend fun getAllBlendedHoneyList(@Path("userID") userID: String): Response<AlreadyBlendedHoneyResponse>

    @JvmSuppressWildcards
    @POST("addVendorDispatch")
    suspend fun loadVenderDispatch(@Body body: HashMap<String, Any>): Response<DefaultResponse>

    @POST("addInventoryDetailList")
    suspend fun addInventoryDetailList(@Body body: List<StoreHoneyModel>): Response<StoreHoneyResponse>

    @GET("rev_geocode")
    suspend fun getAddress(@Query("lat") latitude: Double, @Query("lng")longitude: Double): Response<MapIndiaAddress>
}