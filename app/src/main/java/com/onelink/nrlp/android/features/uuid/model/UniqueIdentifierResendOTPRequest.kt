package com.onelink.nrlp.android.features.uuid.model

import com.google.gson.annotations.SerializedName
import com.onelink.nrlp.android.utils.LukaKeRakk

data class UniqueIdentifierResendOTPRequest(
    @SerializedName("nic_nicop")
    val nicNicop: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("user_type")
    val userType: String,
    @SerializedName("encryption_key")
    val encryptionKey: String = LukaKeRakk.kctw()
)