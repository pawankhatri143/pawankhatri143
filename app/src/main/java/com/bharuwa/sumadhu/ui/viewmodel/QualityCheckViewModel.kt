package com.bharuwa.sumadhu.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.network.base.ApiEndPoint
import com.bharuwa.sumadhu.network.base.RetrofitTrader
import com.bharuwa.sumadhu.network.config.ApiRequest
import com.bharuwa.sumadhu.network.tool.ErrorUtils
import com.bharuwa.sumadhu.network.tool.NoConnectivityException
import com.bharuwa.sumadhu.ui.vendor.model.QualityCheckAllModel
import com.bharuwa.sumadhu.ui.vendor.model.QualityCheckAllResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class QualityCheckViewModel  @Inject constructor(@RetrofitTrader private val apiService: ApiEndPoint) : ViewModel() {

    private val _listAll = MutableLiveData<List<QualityCheckAllResult>>()
    val listAllData:LiveData<List<QualityCheckAllResult>> = _listAll
    private val _listApproved = MutableLiveData<List<QualityCheckAllResult>>()
    val listApprovedData:LiveData<List<QualityCheckAllResult>> = _listApproved
    private val _listRejected = MutableLiveData<List<QualityCheckAllResult>>()
    val listRejectedData:LiveData<List<QualityCheckAllResult>> = _listRejected

    fun getVendorInward(userId: String) {


        viewModelScope.launch {
            try {
                val response = apiService.getVendorInward(userId)
                if (response.isSuccessful && response.body() != null && response.body()!!.result != null) {
                    _listAll.postValue(response.body()!!.result!!.filter { it.qualityTestStatus == "PENDING" })
                    filterList(response.body()!!.result!!)
                }
            }catch (exception: Exception) {
                Log.e("exception", exception.message.toString())
            }
        }
    }

    private fun filterList(data: List<QualityCheckAllResult>) {
        val approveData = data.filter { it.qualityTestStatus == "APPROVED" }
        val rejectedData = data.filter { it.qualityTestStatus == "REJECTED" }
        _listApproved.postValue(approveData)
        Log.e("rejectedList",""+ _listRejected?.json())
        _listRejected.postValue(rejectedData)
    }

    fun getVendorInwardDetails(inwardID: String) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response = apiService.getVendorInwardDetails(inwardID)
            if (response.isSuccessful && response.body() != null) {
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

    fun inwardHoneyTestingApproved(body: HashMap<String, String>) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response = apiService.inwardHoneyTestingApproved(body)
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

    fun inwardHoneyTestingRejected(body: HashMap<String, String>) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response = apiService.inwardHoneyTestingRejected(body)
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

    fun inwardStatusUpdate(body: HashMap<String, String>) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response = apiService.inwardStatusUpdate(body)
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
    fun inwardStatusUpdateFarmer(body: HashMap<String, String>) = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response = apiService.inwardStatusUpdateFarmer(body)
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

    fun uploadImage(body: MultipartBody.Part, inwardId: RequestBody): LiveData<ApiRequest<Any>> = liveData(Dispatchers.IO) {
        emit(ApiRequest.loading(data = null))
        try {
            val response = apiService.uploadImage(body,inwardId)
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