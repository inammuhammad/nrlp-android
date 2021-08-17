package com.onelink.nrlp.android.features.changePassword.models

import com.google.gson.annotations.SerializedName


@Suppress("unused")
data class ChangePasswordRequest(
    @SerializedName("old_password") val oldPassword: String,
    @SerializedName("new_password") val newPassword: String
)