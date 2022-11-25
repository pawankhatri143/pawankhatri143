package com.bharuwa.sumadhu.network.base

import androidx.annotation.Nullable
import com.bharuwa.sumadhu.app.MyApp
import com.bharuwa.sumadhu.network.config.UrlConfig
import com.bharuwa.sumadhu.network.model.Profile
import com.bharuwa.sumadhu.network.tool.ErrorInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun getOkHttpClient(errorInterceptor: ErrorInterceptor, logging: HttpLoggingInterceptor): OkHttpClient {
//    fun getOkHttpClient(logging: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(errorInterceptor)
            .addInterceptor(logging)
            .addInterceptor { chain ->
//                val authToken = App.get().getAuthToken()
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .method(original.method, original.body)
//                    .addHeader("Authorization", "Bearer $authToken")
//                    .addHeader("X-DeviceId", App.get().getDeviceId())
                val request = requestBuilder.build()
                chain.proceed(request)
            }.connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS).build()
    }

    @Singleton
    @Provides
    fun getGsonFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    @RetrofitFarmer
    fun getRetroFarmerClient(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(UrlConfig.get(UrlConfig.ENV_TEST).BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    @RetrofitTrader
    fun getRetroTraderClient(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(UrlConfig.get(UrlConfig.ENV_TEST).BASE_URL2)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    @RetrofitMapMyIndia
    fun getRetroMapIndiaClient(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(UrlConfig.BASE_URL_MAP)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }



    @Singleton
    @Provides
    fun getLogger(): HttpLoggingInterceptor{
        val logging  = HttpLoggingInterceptor()
        logging .setLevel(HttpLoggingInterceptor.Level.BASIC)
        return logging
    }

    @Singleton
    @Provides
    fun getErrorInterceptor() = ErrorInterceptor()


    @Singleton
    @Provides
    @RetrofitFarmer
    fun getFarmerApiService(@RetrofitFarmer retrofit: Retrofit): ApiEndPoint = retrofit.create(ApiEndPoint::class.java)

    @Singleton
    @Provides
    @RetrofitTrader
    fun getTraderApiService(@RetrofitTrader retrofit: Retrofit): ApiEndPoint = retrofit.create(ApiEndPoint::class.java)

    @Singleton
    @Provides
    @RetrofitMapMyIndia
    fun getMapIndiaApiService(@RetrofitMapMyIndia retrofit: Retrofit): ApiEndPoint = retrofit.create(ApiEndPoint::class.java)


   /* @Singleton
    @Provides
    fun getApiServiceForTrader(@Named("trader") retrofit: Retrofit): ApiEndPointTrader = retrofit.create(ApiEndPointTrader::class.java)
*/
    /*@Singleton
    @Provides
    fun getUserProfile() = MyApp.get().getProfile()*/
   // fun getUserProfile(): Profile? = MyApp.get().getProfile()





}