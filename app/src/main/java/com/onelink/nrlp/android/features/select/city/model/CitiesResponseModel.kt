package com.onelink.nrlp.android.features.select.city.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class CitiesResponseModel(@SerializedName("data") val citiesList: ArrayList<CitiesModel>)

@Parcelize
data class CitiesModel(
    @SerializedName("id") val id : String,
    @SerializedName("city") val city : String
): Parcelable