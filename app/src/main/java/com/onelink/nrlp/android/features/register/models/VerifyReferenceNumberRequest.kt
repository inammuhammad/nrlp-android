package com.onelink.nrlp.android.features.register.models

import com.google.gson.annotations.SerializedName
import com.onelink.nrlp.android.utils.LukaKeRakk

/**
 * Created by Qazi Abubakar on 08/07/2020.
 */

class VerifyReferenceNumberRequest(
    @Suppress("unused")
    @SerializedName("reference_no")
    val referenceNo: String,
    @SerializedName("nic_nicop")
    val nicNicop: String,
    @SerializedName("resident_id")
    val residentId: String?,
    @SerializedName("passport_type")
    val passportType: String?,
    @SerializedName("passport_id")
    val passportId: String?,
    @SerializedName("user_type")
    val userType: String,
    @SerializedName("mobile_no")
    val mobileNo: String,
    @Suppress("unused")
    @SerializedName("amount")
    val amount: String,
    @SerializedName("encryption_key")
    val encryptionKey :String = LukaKeRakk.kcth()
)
