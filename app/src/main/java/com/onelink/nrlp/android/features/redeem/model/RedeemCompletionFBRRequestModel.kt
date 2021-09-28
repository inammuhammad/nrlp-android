package com.onelink.nrlp.android.features.redeem.model

import com.google.gson.annotations.SerializedName

@Suppress("unused")
class RedeemCompletionFBRRequestModel (
    @SerializedName("transaction_id")
    var transactionId: String?
)