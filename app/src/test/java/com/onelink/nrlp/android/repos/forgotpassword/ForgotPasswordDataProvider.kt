package com.onelink.nrlp.android.repos.forgotpassword

import com.onelink.nrlp.android.features.forgotPassword.models.ForgotPasswordOTPRequestModel
import com.onelink.nrlp.android.features.forgotPassword.models.ForgotPasswordRequestModel
import com.onelink.nrlp.android.features.forgotPassword.models.UpdatePasswordRequestModel
import com.onelink.nrlp.android.models.GeneralMessageResponseModel

fun getForgotPasswordRequest() = ForgotPasswordRequestModel(
    "9r09128340123",
    "remitter"
)

fun getForgotPasswordResponse() = GeneralMessageResponseModel(
    "Forgot Password Success"
)

fun getForgotPasswordOTPRequestModel() = ForgotPasswordOTPRequestModel(
    "12384091824038",
    "remitter",
    "123123"
)

fun getForgotPasswordOTPResponse() = GeneralMessageResponseModel(
    "Forgot Password OTP request successful"
)

fun getUpdatePasswordRequestModel() = UpdatePasswordRequestModel(
    "928341238120", "remitter", "Password@123"
)

fun getUpdatePasswordResponse() = GeneralMessageResponseModel(
    "Password updated"
)