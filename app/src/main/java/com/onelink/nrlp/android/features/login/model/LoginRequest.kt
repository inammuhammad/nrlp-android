package com.onelink.nrlp.android.features.login.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.onelink.nrlp.android.utils.LukaKeRakk
import com.onelink.nrlp.android.utils.UniqueDeviceID
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginRequest(
    @SerializedName("nic_nicop")
    var nicNicop: String,

    @SerializedName("password")
    var password: String,

    @SerializedName("user_type")
    var userType: String,

    @SerializedName("encryption_key")
    val encryptionKey: String = UniqueDeviceID.getUniqueId()?:""
): Parcelable