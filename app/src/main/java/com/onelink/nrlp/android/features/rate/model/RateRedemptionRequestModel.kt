package com.onelink.nrlp.android.features.rate.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RateRedemptionRequestModel (
    @SerializedName("transaction_id") var transactionId: String,
    @SerializedName("comments") var comments: String,
): Parcelable
