package com.onelink.nrlp.android.features.selfAwardPoints.model

import com.google.gson.annotations.SerializedName

data class SelfAwardPointsResponseModel (
    @SerializedName("message") val message: String,
    @SerializedName("sa_row_id") val rowID:String
)