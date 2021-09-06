package com.onelink.nrlp.android.features.selfAwardPoints.model

import com.google.gson.annotations.SerializedName


class SelfAwardPointsRequestModel(
    @SerializedName("nic_nicop")
    val nicNicop: String,
    @SerializedName("user_type")
    val userType: String
)