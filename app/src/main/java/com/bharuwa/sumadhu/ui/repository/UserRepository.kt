package com.bharuwa.sumadhu.ui.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.bharuwa.sumadhu.network.ApiClient
import com.bharuwa.sumadhu.network.ApiEndPoint
import com.bharuwa.sumadhu.network.ApiRequest
import com.bharuwa.sumadhu.network.NoConnectivityException
import kotlinx.coroutines.Dispatchers

class UserRepository : ViewModel() {
    private var apiService: ApiEndPoint = ApiClient.get()
    fun getState() = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response = apiService.getState()
            emit(ApiRequest.success(data = response))
        }catch (exception: Exception){
            val type = if (exception is NoConnectivityException) "internet" else exception.message.toString()
            emit(ApiRequest.error(data = null, message = type))
        }
    }
    fun getDistrict(stateId:String) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response = apiService.getDistrict(stateId)
            emit(ApiRequest.success(data = response))
        }catch (exception: Exception){
            val type = if (exception is NoConnectivityException) "internet" else exception.message.toString()
            emit(ApiRequest.error(data = null, message = type))
        }
    }
    fun getCity(districtId:String) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response = apiService.getCity(districtId)
            emit(ApiRequest.success(data = response))
        }catch (exception: Exception){
            val type = if (exception is NoConnectivityException) "internet" else exception.message.toString()
            emit(ApiRequest.error(data = null, message = type))
        }
    }

}