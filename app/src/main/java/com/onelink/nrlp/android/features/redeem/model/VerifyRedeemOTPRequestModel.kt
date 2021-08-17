package com.onelink.nrlp.android.features.redeem.model

import com.google.gson.annotations.SerializedName

class VerifyRedeemOTPRequestModel(
    @SerializedName("transaction_id")
    var transactionId: String?,
    @SerializedName("otp")
    var otp: String?)