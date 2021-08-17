package com.onelink.nrlp.android.features.redeem.model

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

@Suppress("unused")
class InitializeRedeemRequestModel(
    @SerializedName("partner_id")
    var partnerId: Int?,
    @SerializedName("category_id")
    var categoryId: Int?,
    @SerializedName("points")
    var points: BigInteger?
)