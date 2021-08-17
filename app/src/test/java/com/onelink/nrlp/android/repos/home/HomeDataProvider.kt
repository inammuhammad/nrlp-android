package com.onelink.nrlp.android.repos.home

import com.onelink.nrlp.android.features.home.model.UserProfileModel
import com.onelink.nrlp.android.features.home.model.UserProfileResponseModel
import com.onelink.nrlp.android.models.GeneralMessageResponseModel


fun getUserProfileResponse() = UserProfileResponseModel(
    UserProfileModel(
        1,
        "Muhammad Hamza",
        123.toBigInteger(),
        "12391231",
        "test@gmail.com",
        "remitter",
        "bronze",
        2321.toBigDecimal()
    )
)

fun getPerformLogoutResponse() = GeneralMessageResponseModel(
    "Logout Successfully"
)

