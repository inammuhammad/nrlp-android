package com.onelink.nrlp.android.features.selfAwardPoints.model

import com.google.gson.annotations.SerializedName

data class SelfAwardPointsRequest(
    @SerializedName("amount") var amount: String,
    @SerializedName("reference_no") var reference_no: String,
    /*@SerializedName("transaction_date") var transaction_date: String?,*/
)