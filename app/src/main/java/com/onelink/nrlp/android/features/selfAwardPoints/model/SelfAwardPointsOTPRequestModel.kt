package com.onelink.nrlp.android.features.selfAwardPoints.model

import com.google.gson.annotations.SerializedName


class SelfAwardPointsOTPRequestModel(
    @SerializedName("otp")
    val otp: String,
    @SerializedName("beneficiary_nic_nicop")
    var beneficiary_nic_nicop: String?
)