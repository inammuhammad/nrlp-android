package com.onelink.nrlp.android.repos.selectcountry

import com.onelink.nrlp.android.features.select.country.model.CountryCodeModel
import com.onelink.nrlp.android.features.select.country.model.CountryCodeResponseModel

fun getCountryCodeResponse() = CountryCodeResponseModel(
    listOf(
        CountryCodeModel(
            "Pak",
            "+92",
            3
        )
    )
)