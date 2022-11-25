package com.bharuwa.sumadhu.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class ErrorInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain!!.proceed(chain.request())
        Log.e("url", originalResponse.request.url.toString())
        Log.e("code", "${originalResponse.code}")
        return originalResponse

    }

}