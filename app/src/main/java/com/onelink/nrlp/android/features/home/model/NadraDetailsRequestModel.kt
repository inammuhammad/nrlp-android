package com.onelink.nrlp.android.features.home.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NadraDetailsRequestModel(
    @SerializedName("mother_maiden_name") val motherMaidenName: String?,
    @SerializedName("place_of_birth") val placeOfBirth: String?,
    @SerializedName("cnic_nicop_issuance_date") val cnicNicopIssuanceDate: String?,
    @SerializedName("full_name") val fullName: String?
): Parcelable