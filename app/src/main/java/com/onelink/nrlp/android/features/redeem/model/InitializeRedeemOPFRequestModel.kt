package com.onelink.nrlp.android.features.redeem.model

import com.google.gson.annotations.SerializedName

@Suppress("unused")
class InitializeRedeemOPFRequestModel (
    @SerializedName("code")
    var code: String?,
    @SerializedName("pse")
    var pse: String?,
    @SerializedName("pse_child")
    var pseChild: String?,
    @SerializedName("consumer_no")
    var consumerNo: String?
)