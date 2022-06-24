package com.onelink.nrlp.android.features.home.model

import com.google.gson.annotations.SerializedName

data class PopupMessageRequest(
    @SerializedName("cust_type") val custType: String,
    @SerializedName("acc_status") val accountStatus: String,
)