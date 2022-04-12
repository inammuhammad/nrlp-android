package com.onelink.nrlp.android.features.redeem.model

import com.google.gson.annotations.SerializedName

data class RedeemSuccessResponseModel(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: RedeemSuccessDataModel
)

data class RedeemSuccessDataModel(
    @SerializedName("transaction_id") val transactionId: String,
    @SerializedName("auth_id") val authId: String
)