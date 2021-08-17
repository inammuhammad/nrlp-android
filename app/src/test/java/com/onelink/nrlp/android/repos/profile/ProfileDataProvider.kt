package com.onelink.nrlp.android.repos.profile

import com.google.gson.JsonObject
import com.onelink.nrlp.android.features.select.country.model.CountryCodeModel
import com.onelink.nrlp.android.features.select.country.model.CountryCodeResponseModel
import com.onelink.nrlp.android.models.GeneralMessageResponseModel

fun getCountryCodeResponse() = CountryCodeResponseModel(
    listOf(
        CountryCodeModel(
            "Pak",
            "+92",
            3
        )
    )
)

fun getJsonObject() = JsonObject()


fun updateProfileResponse() = GeneralMessageResponseModel(
    "Profile updated"
)

fun verifyOTPResponse() = GeneralMessageResponseModel(
    "OTP verified"
)

fun verifyResendOTPResponse() = GeneralMessageResponseModel(
    "Resend OTP verified"
)
