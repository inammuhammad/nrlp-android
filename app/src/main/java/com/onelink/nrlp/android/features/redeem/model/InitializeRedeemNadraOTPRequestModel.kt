package com.onelink.nrlp.android.features.redeem.model

import com.google.gson.annotations.SerializedName

@Suppress("unused")
class InitializeRedeemNadraOTPRequestModel(
    @SerializedName("code")
    var code: String?,
    @SerializedName("pse")
    var pse: String?,
    @SerializedName("pse_child")
    var pseChild: String?,
    @SerializedName("tracking_id")
    var trackingId: String?,
    @SerializedName("consumer_no")
    var consumerNo: String?,
    @SerializedName("sotp")
    var sotp: String?,
    @SerializedName("amount")
    var amount: String?,
)