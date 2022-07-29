package com.onelink.nrlp.android.features.forgotPassword.models

import com.google.gson.annotations.SerializedName
import com.onelink.nrlp.android.utils.LukaKeRakk

/**
 * Created by Qazi Abubakar on 16/07/2020.
 */
class ForgotPasswordRequestModel(
    @SerializedName("nic_nicop")
    val nicNicop: String,
    @SerializedName("user_type")
    val userType: String,
    /*@SerializedName("mother_maiden_name")
    val motherMaidenName: String,*/
    @SerializedName("encryption_key")
    val encryptionKey: String = LukaKeRakk.kcth(),
)