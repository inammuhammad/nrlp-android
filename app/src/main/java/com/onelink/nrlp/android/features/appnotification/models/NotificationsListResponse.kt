package com.onelink.nrlp.android.features.appnotification.models

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class NotificationsListResponse (
    @SerializedName("message")
    val message: String?,
    @SerializedName("data") val data: AppNotificationModel,
)

data class AppNotificationModel (
    @SerializedName("records")
    var records: ArrayList<NotificationListItemModel>
)

data class NotificationListItemModel (
    @SerializedName("id")
    var id: Int?,
    @SerializedName("cnic")
    var cnic: BigInteger?,
    @SerializedName("cust_type")
    var customerType: String? = "",
    @SerializedName("notification_datetime")
    var notificationDateTime: String? = "",
    @SerializedName("notification_type")
    var notificationType: String? = "",
    @SerializedName("notification_message")
    var notificationMessage: String? = "",
    @SerializedName("is_read_flag")
    var isReadFlag: Int? = 0
)