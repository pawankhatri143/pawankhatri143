package com.bharuwa.sumadhu.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.bharuwa.sumadhu.network.base.ApiEndPoint
import com.bharuwa.sumadhu.network.base.RetrofitFarmer
import com.bharuwa.sumadhu.network.config.ApiRequest
import com.bharuwa.sumadhu.network.tool.ErrorUtils
import com.bharuwa.sumadhu.network.tool.NoConnectivityException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import javax.inject.Inject


@HiltViewModel
class LocationViewModel @Inject constructor(@RetrofitFarmer private val apiService: ApiEndPoint)  : ViewModel() {

    fun getLocation(userId: String) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response = apiService.getLocation(userId)
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
    fun saveLocation(@Body body: HashMap<String, Any>) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response = apiService.saveLocation(body)
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
    fun saveDefaultLocation(locationId:String,userId:String) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response = apiService.saveDefaultLocation(locationId,userId)
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


    fun uploadFarmImage(body: List<MultipartBody.Part?>, farmId: RequestBody) =liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response = apiService.uploadFarmPhotos(body,farmId)
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