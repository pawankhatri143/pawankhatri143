package com.bharuwa.sumadhu.network.config

object UrlConfig {
    const val ENV_PRODUCTION = "_production"
    const val ENV_TEST = "_test"
    const val TEST_DOMAIN = "http://182.18.155.165:8080"
    const val TEST_DOMAIN2 = "http://182.18.155.165:6003"
    const val MAP_INDIA_DOMAIN = "http://apis.mapmyindia.com/advancedmaps/v1/212392fa3ba0712d1831d16e99c0bfb2/"
//    private const val TEST_DOMAIN = "http://172.16.18.141:8080"
//    private const val PROD_DOMAIN = "http://182.18.155.165:8080/"
    private const val PROD_DOMAIN = "http://182.18.155.172:8080"
    var BASE_URL = ""
    var BASE_URL2 = ""
    var BASE_URL_MAP = ""
    var IMAGE_URL = ""
    fun get(type: String): UrlConfig {
        val postFix = "/Sumadhu/"
       val postFix2 = "/api/"
        val isProd = type == ENV_PRODUCTION
        BASE_URL = if (isProd) "$PROD_DOMAIN$postFix" else "$TEST_DOMAIN$postFix"
        BASE_URL2 = if (isProd) "$PROD_DOMAIN$postFix" else "$TEST_DOMAIN2$postFix2"
        BASE_URL_MAP ="$MAP_INDIA_DOMAIN"
        IMAGE_URL = if (isProd) PROD_DOMAIN else TEST_DOMAIN
        return this
    }
}