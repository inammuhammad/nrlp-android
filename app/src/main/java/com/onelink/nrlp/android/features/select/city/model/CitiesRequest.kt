package com.onelink.nrlp.android.features.select.city.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CitiesRequest(
    @SerializedName("city")
    var city: String,
    @SerializedName("page_number")
    var page_number: Int
): Parcelable