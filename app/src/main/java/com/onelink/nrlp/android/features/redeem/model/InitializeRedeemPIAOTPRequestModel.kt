package com.onelink.nrlp.android.features.redeem.model

import com.google.gson.annotations.SerializedName

@Suppress("unused")
class InitializeRedeemPIAOTPRequestModel (
    @SerializedName("code")
    var code: String?,
    @SerializedName("pse")
    var pse: String?,
    @SerializedName("pse_child")
    var pseChild: String?,
    @SerializedName("consumer_no")
    var consumerNo: String?,
    @SerializedName("amount")
    var amount: String?,
    @SerializedName("sotp")
    var sotp: String?
)