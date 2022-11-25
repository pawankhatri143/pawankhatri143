package com.bharuwa.sumadhu.network

object UrlConfig {
    const val ENVIRONMENT_PRODUCTION = "_production"
    const val ENVIRONMENT_TEST = "_test"
    private const val TEST_DOMAIN = "http://182.18.155.165:8080"
//    private const val TEST_DOMAIN = "http://172.16.18.141:8080"
    private const val PROD_DOMAIN = "http://182.18.155.172:8080"
    var BASE_URL = ""
    var IMAGE_URL = ""
    fun get(type: String): UrlConfig {
        val postFix = "/Sumadhu/"
//        val postFix = "/api/"
        val isProd = type == ENVIRONMENT_PRODUCTION
        BASE_URL = if (isProd) "$PROD_DOMAIN$postFix" else "$TEST_DOMAIN$postFix"
        IMAGE_URL = if (isProd) PROD_DOMAIN else TEST_DOMAIN
        return this
    }
}