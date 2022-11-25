package com.bharuwa.sumadhu.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.bharuwa.sumadhu.network.base.ApiEndPoint
import com.bharuwa.sumadhu.network.base.RetrofitFarmer
import com.bharuwa.sumadhu.network.config.ApiRequest
import com.bharuwa.sumadhu.network.tool.ErrorUtils
import com.bharuwa.sumadhu.network.tool.NoConnectivityException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import retrofit2.http.Body
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(@RetrofitFarmer private val apiService: ApiEndPoint) : ViewModel() {

    fun userRegistration(@Body body: HashMap<String, String>) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response = apiService.userRegistration(body)
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
    fun profileUpdate(@Body body: HashMap<String, String>) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response = apiService.profileUpdate(body)
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