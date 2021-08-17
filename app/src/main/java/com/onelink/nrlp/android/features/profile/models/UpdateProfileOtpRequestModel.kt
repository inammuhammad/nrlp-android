package com.onelink.nrlp.android.features.profile.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Qazi Abubakar
 */
class UpdateProfileOtpRequestModel(
    @SerializedName("mobile_no")
    val mobileNo: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("otp")
    val otp: String
)