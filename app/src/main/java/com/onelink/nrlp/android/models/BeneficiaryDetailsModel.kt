package com.onelink.nrlp.android.models

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

/**
 * Created by Qazi Abubakar on 11/07/2020.
 */

data class BeneficiaryDetailsModel(
    @SerializedName("id") val id: Int,
    @SerializedName("nic_nicop") val nicNicop: BigInteger,
    @SerializedName("mobile_no") val mobileNo: String,
    @SerializedName("is_active") val _isActive: Int,
    @SerializedName("alias") val alias: String,
    @SerializedName("relationship") val relationship: String,
    @SerializedName("country") val country: String
) {
    val isActive: Boolean
        get() = _isActive.toBoolean()
}

fun Int.toBoolean() = this == 1
