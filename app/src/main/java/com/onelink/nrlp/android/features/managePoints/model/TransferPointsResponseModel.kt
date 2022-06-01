package com.onelink.nrlp.android.features.managePoints.model

import com.google.gson.annotations.SerializedName

data class TransferPointsResponseModel (
    @SerializedName("message") val message: String,
    @SerializedName("customer_rating") val customerRating: Boolean
)