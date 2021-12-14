package com.onelink.nrlp.android.features.redeem.model

import com.google.gson.annotations.SerializedName

@Suppress("unused")
class InitializeRedeemPassportOTPRequestModel (
    @SerializedName("code")
    var code: String?,
    @SerializedName("pse")
    var pse: String?,
    @SerializedName("pse_child")
    var pseChild: String?,
    @SerializedName("consumer_no")
    var consumerNo: String?,
    @SerializedName("mobile_no")
    var mobileNo: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("sotp")
    var sotp: String?,
    @SerializedName("amount")
    var amount: String?,
)