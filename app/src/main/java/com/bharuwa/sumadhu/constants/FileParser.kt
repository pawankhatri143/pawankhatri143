package com.bharuwa.sumadhu.constants

import android.app.Activity

val farmerCategory = hashMapOf<String, List<String>>().apply {
    put("Hindi", listOf("सामान्य", "अन्य पिछड़ा वर्ग", "अनुसूचित जाति", "अनुसूचित जनजाति", "अन्य"))
    put("English", listOf("General", "OBC", "SC", "ST", "Other"))
}
val listPropertyType = hashMapOf<String, List<String>>().apply {
    put("Hindi", listOf("भूमि का प्रकार चुनें","निजी","सरकारी","कब्रिस्तान"))
    put("English", listOf("Select property type","Governmental","private","Institutional"))
}
val listOthers = hashMapOf<String, List<String>>().apply {
    put("Hindi", listOf("टिप्पणी चुनें","सरकारी खसरा","निजी","खसरा मौजूद नहीं है","पिता के नाम खसरा","खसरा में नाम मौजूद नहीं है","अन्य"))
    put("English", listOf("Select remark","Govt. Khasra","Khasra not exist","Khasra under father name","Name not exist in khasra","Other"))
}
val listIrrigation = hashMapOf<String, List<String>>().apply {
    put("Hindi", listOf(/*"Select irrigation type",*/"Rain fed","Tube well","Canal","Pond"))
    put("English", listOf(/*"Select irrigation type",*/"Rain fed","Tube well","Canal","Pond"))
}
//val listIrrigation = listOf("Select irrigation type","Rain Sad","Tube Well","Canal","Pond")

val categoryList = hashMapOf<String, List<String>>().apply {
    put("Hindi", listOf("वर्ग का चयन करें","सामान्य","अन्य पिछड़ा वर्ग","अनुसूचित जाति","अनुसूचित जनजाति"))
    put("English", listOf("Select category","General","OBC","SC","ST"))
}
val listOwnerShip = listOf("PRIVATE","Private","private", "निजी","निज","निजि","नीजी",)

val getFarmingTypes = listOf(
//    "Select farming type",
    "Organic Farming",
    "Mixed 25% organic",
    "Mixed 50% organic",
    "Mixed 75% organic",
    "Inorganic Farming"
)
fun getFarmingType(farmingType: String?): String {
    return when (farmingType) {
        "0" -> "ORGANIC"
        "1" -> "MIX25"
        "2" -> "MIX50"
        "3" -> "MIX75"
        "4" -> "INORGANIC"
        else -> "INORGANIC"
    }
}

val getNValueHint = listOf(
    "Low, less than 280",
    "Medium, between 280-460",
    "High, more than 460"
)
val getPValueHint = listOf(
    "Low, less than 12.5",
    "Medium, between 12.50-25",
    "High,more than 25"
)
val getKValueHint = listOf(
    "Low, less than 115",
    "Medium, between 115-280",
    "High, more than 280"
)
val getOCValueHint = listOf(
    "Low,less than 0.50 ",
    "Medium, between 0.50-0.75",
    "High, more than 0.75"
)
val getPHValueHint = listOf(
    "Acidic, less than 7",
    "Neutral, equal to 7",
    "Alkaline, more than 7"
)
val getFilterList = listOf(
    "All",
    "Soil Collector",
    "Lab",
    "GIS"
)

fun Activity.getUserRole(userTypeId:String):String{
    return when (userTypeId) {
        "611b6d811003138c9d40a8b7" -> "SuperAdmin"
        "607546c500f6f4c43c5d54a0" -> "Admin"
        "611b73931003138c9d40a8b8" -> "CallCenter"
        "6075479300f6f4c43c5d54a2" -> "Doctor"
        "60e5582069bec5646f115686" -> "Therapist"
        "60ed26557f6bda738e03c049" -> "Meditation"
        "6075474600f6f4c43c5d54a1" -> "Patient"
        "60ee67c8b43250ad3a1d362d" -> "Pathologist"
        "60e5597069bec5646f115688" -> "Cafeteria"
        else -> "User not define"
    }
}
