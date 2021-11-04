package com.onelink.nrlp.android.features.register.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountryCodesRequest (
    @SerializedName("type")
    var type: String = "remitter"
): Parcelable