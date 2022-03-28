package com.onelink.nrlp.android.features.register.models

import com.google.gson.annotations.SerializedName

data class TermsAndConditionsResponseModel(@SerializedName("data") val termsAndConditions: TermsAndConditions)

data class TermsAndConditions(
    @SerializedName("content")
    val content: String,
    @SerializedName("version_no")
    val versionNum: String,
    @SerializedName("id")
    val termsAndConditionsId: Int
)