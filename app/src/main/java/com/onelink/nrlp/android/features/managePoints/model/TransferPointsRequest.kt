package com.onelink.nrlp.android.features.managePoints.model

import com.google.gson.annotations.SerializedName

data class TransferPointsRequest(
    @SerializedName("beneficiary_id") var beneficiaryId: String,
    @SerializedName("points") var points: String
)