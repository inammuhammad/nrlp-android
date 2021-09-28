package com.onelink.nrlp.android.features.redeem.model

import com.google.gson.annotations.SerializedName

class RedeemFBRSuccessResponseModel (
    @SerializedName("message") val message: String,
    @SerializedName("transaction_id") val transactionId: String
)