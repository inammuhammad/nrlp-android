package com.onelink.nrlp.android.features.forgotPassword.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Qazi Abubakar on 16/07/2020.
 */
class ForgotPasswordRequestModel(
    @SerializedName("nic_nicop")
    val nicNicop: String,
    @SerializedName("user_type")
    val userType: String
)