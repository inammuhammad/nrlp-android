package com.onelink.nrlp.android.features.profile.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Qazi Abubakar on 17/07/2020.
 */
data class ProfileResponseModel(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ProfileDataModel
)
