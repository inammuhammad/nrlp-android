package com.onelink.nrlp.android.features.selfAwardPoints.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SelfAwardPointsRequest(
    @SerializedName("amount") var amount: String,
    @SerializedName("reference_no") var reference_no: String,
    @SerializedName("beneficiary_nic_nicop") var beneficiary_nic_nicop: String,
    @SerializedName("transaction_date") var transaction_date: String?
): Parcelable