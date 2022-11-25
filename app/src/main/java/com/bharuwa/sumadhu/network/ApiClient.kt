package com.bharuwa.sumadhu.network

import com.bharuwa.sumadhu.app.MyApp
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private val config = UrlConfig.get(UrlConfig.ENVIRONMENT_TEST)
    private var API_URL = config.BASE_URL
    var API_Main_URL = config.IMAGE_URL
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(InternetConnectionInterceptor(MyApp.get()))
        .addInterceptor(ErrorInterceptor())
        .addInterceptor { chain ->
//            val authToken = MyApp.get().getAuthToken()
            val  language = MyApp.get().getLanguage()
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .method(original.method, original.body)
//                .addHeader("Authorization", "Bearer $authToken")
                .addHeader("language", language)
            val request = requestBuilder.build()
            chain.proceed(request)
        }.connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS).build()


    fun get(): ApiEndPoint = instance

//    fun getWareHouseURL(): ApiEndPoint = wareHouseAPIInstance

    /*private val instance: ApiEndPoint by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        retrofit.create(ApiEndPoint::class.java)
    }*/
    /*private val wareHouseAPIInstance: ApiEndPoint by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_BASE_WAREHOUSE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        retrofit.create(ApiEndPoint::class.java)
    }*/

    private val instance: ApiEndPoint by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        retrofit.create(ApiEndPoint::class.java)
    }
}