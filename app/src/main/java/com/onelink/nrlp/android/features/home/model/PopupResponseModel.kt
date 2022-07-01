package com.onelink.nrlp.android.features.home.model

import com.google.gson.annotations.SerializedName

data class PopupResponseModel(
    @SerializedName("records") val popupData: PopupData
)

data class PopupData(
    @SerializedName("display_text") val displayText: String?
)