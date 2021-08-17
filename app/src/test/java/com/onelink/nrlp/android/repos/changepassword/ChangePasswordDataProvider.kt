package com.onelink.nrlp.android.repos.changepassword

import com.onelink.nrlp.android.features.changePassword.models.ChangePasswordRequest
import com.onelink.nrlp.android.models.GeneralMessageResponseModel

fun getChanegPasswordRequest() = ChangePasswordRequest(
    "028021821n",
    "skfj23482"
)

fun getChangePasswordResponse() = GeneralMessageResponseModel(
    "Password has been changed successfully"
)