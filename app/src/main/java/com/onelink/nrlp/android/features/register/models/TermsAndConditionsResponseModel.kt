package com.onelink.nrlp.android.features.register.models

import com.google.gson.annotations.SerializedName

data class TermsAndConditionsResponseModel(@SerializedName("data") val termsAndConditions: TermsAndConditions)

data class TermsAndConditions(val content: String)