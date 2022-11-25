package com.bharuwa.sumadhu.app

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import android.util.Log
import com.bharuwa.sumadhu.constants.Constants.*
import com.bharuwa.sumadhu.constants.Util.fromJson
import com.bharuwa.sumadhu.constants.Util.json
import com.bharuwa.sumadhu.locale.LocaleManager
import com.bharuwa.sumadhu.network.model.LocationsModel
import com.bharuwa.sumadhu.network.model.Profile
import com.bharuwa.sumadhu.ui.trader.model.DrumDetailsModel
import com.bharuwa.sumadhu.ui.trader2.model.BuckitDetailsModel
import dagger.hilt.android.HiltAndroidApp
import okhttp3.MultipartBody


@HiltAndroidApp
class MyApp : Application() {
    private lateinit var localStorage: SharedPreferences
    lateinit var buckitList: MutableList<BuckitDetailsModel>

    override fun onCreate() {
        super.onCreate()
        app = this
        localStorage = getSharedPreferences(packageName, MODE_PRIVATE)
        LocaleManager.init()
        buckitList = ArrayList()
    }

    private fun getString(key: String) = localStorage.getString(key, "").toString()
    private fun putString(key: String, value: String) {
        localStorage.edit().putString(key, value).apply()
    }

    fun setProfile(profile: Profile?) {
        profile?.let { putString(PROFILE, it.json()) }
    }
    fun logout() {
        localStorage.edit().clear().apply()
    }
    fun getProfile(): Profile? {
        return if(getString(PROFILE).isNullOrBlank()) null else getString(PROFILE)?.fromJson()
    }
    fun setDefaultLocation(location: LocationsModel?) {
        location?.let { putString(DEFAULT_LOCATION, it.json()) }
    }

    fun getDefaultLocation(): LocationsModel? {
        return if(getString(DEFAULT_LOCATION)=="") null else getString(DEFAULT_LOCATION)?.fromJson()
    }
    fun getRole(): String{
        var type = ""
        val s = getString(PROFILE)
        if (s.isNotEmpty()){
            type = s.fromJson<Profile>()._id!!
        }
        return type
    }

    fun setLocale(locale: String) {
        putString(LOCALE, locale)
    }

    fun getLocale()  =  if(getString(LOCALE).isEmpty()) "en" else getString(LOCALE)
    companion object {
        private lateinit var app: MyApp
        fun get(): MyApp = app
    }
    fun setLanguage(language: String) {
        putString(LANGUAGE, language)
    }
    fun getLanguage()  =  if(getString(LANGUAGE).isEmpty()) "English" else getString(LANGUAGE)


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleManager.get().updateResources(this, "hi")
    }


   /* fun uploadAdPics(id: String, files: List<MultipartBody.Part>){
        if (MyApp.get().getProfile()?.partyType.equals(UESER_ROLE_FARMER,true)
            || MyApp.get().getProfile()?.partyType.isNullOrBlank()){
            ApiClient.get().uploadFarmerAdPics(id, files).enqueue {
                onSuccess = {
                    Log.e("uploadAdPics", "publishAd: ="+it.body().toString() )
                    CreateAdActivity.attachments.clear()
                    imageAttachments.clear()

                }
                onFailure = {}
            }
        }else{
            ApiClient.get().uploadAdPics(id, files).enqueue {

                onSuccess = {
//                    Log.e("uploadAdPics", "publishAd: ="+it.body().toString() )
                    CreateAdActivity.attachments.clear()
                    imageAttachments.clear()
                }
                onFailure = {
                    Log.e("uploadAdPics", "publishAd: = onFailure" )
                }
            }
        }

    }*/


}