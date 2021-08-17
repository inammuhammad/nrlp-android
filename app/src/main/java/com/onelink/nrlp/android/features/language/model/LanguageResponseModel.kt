package com.onelink.nrlp.android.features.language.model

import com.google.gson.annotations.SerializedName


data class LanguageResponseModel(
    @SerializedName("data") val languageTypeList: ArrayList<LanguageTypeModel>
)

data class LanguageTypeModel(
    @SerializedName("language_name") var languageName: Int,
    @SerializedName("id") var id: String
)

