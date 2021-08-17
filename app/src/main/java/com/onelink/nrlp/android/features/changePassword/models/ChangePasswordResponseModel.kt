package com.onelink.nrlp.android.features.changePassword.models

import com.google.gson.annotations.SerializedName


@Suppress("unused")
data class ChangePasswordResponseModel(@SerializedName("data") val loginModel: ChangePasswordModel)

data class ChangePasswordModel(val oldPassword: String, val newPassword: String)