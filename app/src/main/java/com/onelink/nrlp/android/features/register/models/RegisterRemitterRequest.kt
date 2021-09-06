package com.onelink.nrlp.android.features.register.models

import com.google.gson.annotations.SerializedName
import com.onelink.nrlp.android.utils.LukaKeRakk

/**
 * Created by Qazi Abubakar on 09/07/2020.
 */

class RegisterRemitterRequest(

    @SerializedName("nic_nicop")
    val nicNicop: String,

    @SerializedName("mobile_no")
    val mobileNo: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("full_name")
    val fullName: String,

    @SerializedName("user_type")
    val userType: String,

    @Suppress("unused")
    @SerializedName("reference_no")
    val referenceNo: String,

    @Suppress("unused")
    @SerializedName("amount")
    val amount: String,

    @SerializedName("email")
    val email: String,
    @SerializedName("encryption_key")
    val encryptionKey: String = LukaKeRakk.kcth(),

    @SerializedName("resident_id")
    val residentId: String?,
    @SerializedName("passport_type")
    val passportType: String?,
    @SerializedName("passport_id")
    val passportId: String?
)