package com.onelink.nrlp.android.features.splash.model

import com.google.gson.annotations.SerializedName

data class AuthResponseModel(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: AuthKeyDataModel)

data class AuthKeyDataModel(
    @SerializedName("key") val key: String
)

