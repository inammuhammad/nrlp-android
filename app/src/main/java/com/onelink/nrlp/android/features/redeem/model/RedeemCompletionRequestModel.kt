package com.onelink.nrlp.android.features.redeem.model

import com.google.gson.annotations.SerializedName

@Suppress("unused")
class RedeemCompletionRequestModel(
    @SerializedName("transaction_id")
    var transactionId: String?,
    @SerializedName("agent_code")
    var agentCode: String?
)