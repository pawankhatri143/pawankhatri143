package com.bharuwa.sumadhu.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.bharuwa.sumadhu.network.base.ApiEndPoint
import com.bharuwa.sumadhu.network.base.RetrofitFarmer
import com.bharuwa.sumadhu.network.config.ApiRequest
import com.bharuwa.sumadhu.network.model.ScanedBoxesModel
import com.bharuwa.sumadhu.network.model.StoreHoneyModel
import com.bharuwa.sumadhu.network.tool.ErrorUtils
import com.bharuwa.sumadhu.network.tool.NoConnectivityException
import com.bharuwa.sumadhu.ui.trader2.model.BuckitDetailsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import retrofit2.http.Body
import javax.inject.Inject

@HiltViewModel
class BoxesViewModel @Inject constructor(@RetrofitFarmer private val apiService: ApiEndPoint) : ViewModel() {

    fun getBoxes(userId: String,locationId: String) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response = apiService.getBoxes(userId,locationId)
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
    fun saveBulkBoxes(@Body scanedBarcodeArray: ArrayList<ScanedBoxesModel>) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response = apiService.saveBulkBoxes(scanedBarcodeArray)
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
    fun saveBox(@Body body: HashMap<String, String>) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response = apiService.saveBox(body)
            if (response.isSuccessful && response.body() != null){
                emit(ApiRequest.success(data = response.body()!!))
            }else{
                val apiError = ErrorUtils.parseError(response)
                emit(ApiRequest.error(data = null, message = if(response.code()==500) "Barcode already exists!" else apiError.message ))
            }
        }catch (exception: Exception){
            val type = if (exception is NoConnectivityException) "internet" else exception.message.toString()
            emit(ApiRequest.error(data = null, message = type))
        }
    }
    fun emptyBox(barcode:String,userId:String) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response = apiService.emptyBox(barcode,userId)
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
    fun emptyBoxBulk(boxIds :List<String>) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response = apiService.emptyBoxBulk(boxIds)
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
    fun relocateBox(userId:String) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response = apiService.relocateBox(userId)
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
    fun relocateBoxBulk(boxIds :List<String>,locationId:String) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response = apiService.relocateBoxBulk(boxIds,locationId)
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
    fun getActiveFarms(userId:String) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response = apiService.getActiveFarms(userId)
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