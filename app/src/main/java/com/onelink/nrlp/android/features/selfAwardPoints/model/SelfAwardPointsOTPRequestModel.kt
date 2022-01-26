package com.onelink.nrlp.android.features.selfAwardPoints.model

import com.google.gson.annotations.SerializedName


class SelfAwardPointsOTPRequestModel(
    @SerializedName("otp")
    val otp: String,
    @SerializedName("sp_respone_row_id")
    var response_row_id: String?
)