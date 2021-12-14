package com.onelink.nrlp.android.features.redeem.model

import com.google.gson.annotations.SerializedName

class RedeemInitializeFBROTPResponseModel (
    @SerializedName("message") val message: String,
    @SerializedName("transaction_id") val transactionId: String,
    @SerializedName("inquiryMessage") val inquiryMessage: String,
)