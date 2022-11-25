package com.bharuwa.sumadhu.ui.trader.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.constants.Constants
import com.bharuwa.sumadhu.network.base.ApiEndPoint
import com.bharuwa.sumadhu.network.base.RetrofitFarmer
import com.bharuwa.sumadhu.network.base.RetrofitTrader
import com.bharuwa.sumadhu.network.config.ApiRequest
import com.bharuwa.sumadhu.network.model.StoreHoneyModel
import com.bharuwa.sumadhu.network.tool.ErrorUtils
import com.bharuwa.sumadhu.network.tool.NoConnectivityException
import com.bharuwa.sumadhu.ui.trader.model.DrumDetailsModel
import com.bharuwa.sumadhu.ui.trader2.model.BuckitDetailsModel
import com.bharuwa.sumadhu.ui.trader2.model.ConfirmTraderOrderResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import retrofit2.http.Body
import javax.inject.Inject

@HiltViewModel
class BuyHoneyViewModel @Inject constructor(@RetrofitTrader private val apiService: ApiEndPoint) : ViewModel() {



//    fun placeOrder(body: List<HashMap<String, String>>) = liveData(Dispatchers.IO) {
    fun placeOrder(body: MutableList<BuckitDetailsModel>) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            var response: Response<ConfirmTraderOrderResponse>? = null

            Log.e("placeOrder", "placeOrder: userType "+ MyApp.get().getProfile()?.userType )
            response = if (MyApp.get().getProfile()?.userType.equals(Constants.VENDOR)) {
                apiService.confirmOrderFromVenderSide(body)
            } else apiService.confirmOrder(body)

            if (response.isSuccessful && response.body() != null){
                emit(ApiRequest.success(data = response.body()!!))
            }else{
                val apiError = ErrorUtils.parseError(response)
                emit(ApiRequest.error(data = null, message = apiError.message))
            }
        }catch (exception: Exception){
            val type = if (exception is NoConnectivityException) "internet" else exception.message.toString()
            emit(ApiRequest.error(data = null, message = type))
        }
    }

    fun findTraderDetails(mobile: String) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response =apiService.getTraderByMobile(mobile)
            if (response.isSuccessful && response.body() != null){
                emit(ApiRequest.success(data = response.body()!!))
            }else{
                val apiError = ErrorUtils.parseError(response)
                emit(ApiRequest.error(data = null, message = apiError.message))
            }
        }catch (exception: Exception){
            val type = if (exception is NoConnectivityException) "internet" else exception.message.toString()
            emit(ApiRequest.error(data = null, message = type))
        }
    }

    fun getTraderOrdersByItem(bucketCode: String, traderId: String?) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response =apiService.getTraderOrdersByItem(bucketCode,traderId)
            if (response.isSuccessful && response.body() != null){
                emit(ApiRequest.success(data = response.body()!!))
            }else{
                val apiError = ErrorUtils.parseError(response)
                emit(ApiRequest.error(data = null, message = apiError.message))
            }
        }catch (exception: Exception){
            val type = if (exception is NoConnectivityException) "internet" else exception.message.toString()
            emit(ApiRequest.error(data = null, message = type))
        }
    }


    fun getMyAllOrder(userID: String) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response =apiService.getMyAllOrder(userID)
            if (response.isSuccessful && response.body() != null){
                emit(ApiRequest.success(data = response.body()!!))
            }else{
                val apiError = ErrorUtils.parseError(response)
                emit(ApiRequest.error(data = null, message = apiError.message))
            }
        }catch (exception: Exception){
            val type = if (exception is NoConnectivityException) "internet" else exception.message.toString()
            emit(ApiRequest.error(data = null, message = type))
        }
    }

    fun getMyOrder(userID: String) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response =apiService.getMyOrder(userID)
            if (response.isSuccessful && response.body() != null){
                emit(ApiRequest.success(data = response.body()!!))
            }else{
                val apiError = ErrorUtils.parseError(response)
                emit(ApiRequest.error(data = null, message = apiError.message))
            }
        }catch (exception: Exception){
            val type = if (exception is NoConnectivityException) "internet" else exception.message.toString()
            emit(ApiRequest.error(data = null, message = type))
        }
    }
    fun vendorList() = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response =apiService.getVendorList()
            if (response.isSuccessful && response.body() != null){
                emit(ApiRequest.success(data = response.body()!!))
            }else{
                val apiError = ErrorUtils.parseError(response)
                emit(ApiRequest.error(data = null, message = apiError.message))
            }
        }catch (exception: Exception){
            val type = if (exception is NoConnectivityException) "internet" else exception.message.toString()
            emit(ApiRequest.error(data = null, message = type))
        }
    }
    fun getVendorWarehouseList(vendorID: String?) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response =apiService.getVendorWarehouseList(vendorID)
            if (response.isSuccessful && response.body() != null){
                emit(ApiRequest.success(data = response.body()!!))
            }else{
                val apiError = ErrorUtils.parseError(response)
                emit(ApiRequest.error(data = null, message = apiError.message))
            }
        }catch (exception: Exception){
            val type = if (exception is NoConnectivityException) "internet" else exception.message.toString()
            emit(ApiRequest.error(data = null, message = type))
        }
    }
    fun getTraderDispatchOrderList(traderID: String) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response =apiService.getTraderDispatchOrderList(traderID)
            if (response.isSuccessful && response.body() != null){
                emit(ApiRequest.success(data = response.body()!!))
            }else{
                val apiError = ErrorUtils.parseError(response)
                emit(ApiRequest.error(data = null, message = apiError.message))
            }
        }catch (exception: Exception){
            val type = if (exception is NoConnectivityException) "internet" else exception.message.toString()
            emit(ApiRequest.error(data = null, message = type))
        }
    }
    fun getTraderDispatchOrderPennding(traderID: String) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response =apiService.getTraderDispatchOrderPennding(traderID)
            if (response.isSuccessful && response.body() != null){
                emit(ApiRequest.success(data = response.body()!!))
            }else{
                val apiError = ErrorUtils.parseError(response)
                emit(ApiRequest.error(data = null, message = apiError.message))
            }
        }catch (exception: Exception){
            val type = if (exception is NoConnectivityException) "internet" else exception.message.toString()
            emit(ApiRequest.error(data = null, message = type))
        }
    }

    fun getMyPendingOrder(userID: String,flora: String) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response =apiService.getMyPendingOrder(userID,flora)
            if (response.isSuccessful && response.body() != null){
                emit(ApiRequest.success(data = response.body()!!))
            }else{
                val apiError = ErrorUtils.parseError(response)
                emit(ApiRequest.error(data = null, message = apiError.message))
            }
        }catch (exception: Exception){
            val type = if (exception is NoConnectivityException) "internet" else exception.message.toString()
            emit(ApiRequest.error(data = null, message = type))
        }
    }


    fun addTraderDispatch(@Body body: HashMap<String, Any>) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response = apiService.addTraderDispatch(body)
            if (response.isSuccessful && response.body() != null){
                emit(ApiRequest.success(data = response.body()!!))
            }else{
                val apiError = ErrorUtils.parseError(response)
                emit(ApiRequest.error(data = null, message = apiError.message))
            }
        }catch (exception: Exception){
            val type = if (exception is NoConnectivityException) "internet" else exception.message.toString()
            emit(ApiRequest.error(data = null, message = type))
        }
    }

    fun loadDispatch(@Body body: HashMap<String, Any>) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response = apiService.assignVehicalToDispatch(body)
            if (response.isSuccessful && response.body() != null){
                emit(ApiRequest.success(data = response.body()!!))
            }else{
                val apiError = ErrorUtils.parseError(response)
                emit(ApiRequest.error(data = null, message = apiError.message))
            }
        }catch (exception: Exception){
            val type = if (exception is NoConnectivityException) "internet" else exception.message.toString()
            emit(ApiRequest.error(data = null, message = type))
        }
    }

    fun getContainerList() = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response = apiService.getContainerList()
            if (response.isSuccessful && response.body() != null){
                emit(ApiRequest.success(data = response.body()!!))
            }else{
                val apiError = ErrorUtils.parseError(response)
                emit(ApiRequest.error(data = null, message = apiError.message))
            }
        }catch (exception: Exception){
            val type = if (exception is NoConnectivityException) "internet" else exception.message.toString()
            emit(ApiRequest.error(data = null, message = type))
        }
    }

    fun storeHoney(body: List<StoreHoneyModel>) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response =apiService.addInventoryDetailList(body)
            if (response.isSuccessful && response.body() != null){
                emit(ApiRequest.success(data = response.body()!!))
            }else{
                val apiError = ErrorUtils.parseError(response)
                emit(ApiRequest.error(data = null, message = apiError.message))
            }
        }catch (exception: Exception){
            val type = if (exception is NoConnectivityException) "internet" else exception.message.toString()
            emit(ApiRequest.error(data = null, message = type))
        }
    }

}