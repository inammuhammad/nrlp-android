package com.onelink.nrlp.android.features.profile.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Qazi Abubakar on 17/07/2020.
 */
data class ProfileDataModel(
    @SerializedName("full_name") val fullName: String,
    @SerializedName("nic_nicop") val nicNicop: String,
    @SerializedName("mobile_no") val mobileNo: String
)
