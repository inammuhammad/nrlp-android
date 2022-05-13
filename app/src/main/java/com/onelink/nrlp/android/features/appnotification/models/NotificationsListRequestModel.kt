package com.onelink.nrlp.android.features.appnotification.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NotificationsListRequestModel (
        @SerializedName("page")
        var page: String? = "",
        @SerializedName("per_page")
        var perPage: String? = "",
        @SerializedName("nic_nicop")
        var cnic: String? = "",
        @SerializedName("notification_type")
        var notificationType: String? = ""
) : Parcelable