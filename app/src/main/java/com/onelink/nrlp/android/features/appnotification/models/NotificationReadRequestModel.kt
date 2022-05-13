package com.onelink.nrlp.android.features.appnotification.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NotificationReadRequestModel (
    @SerializedName("id")
    var id: String?,
    @SerializedName("is_read_flag")
    var isReadFlag: Int? = 1
): Parcelable