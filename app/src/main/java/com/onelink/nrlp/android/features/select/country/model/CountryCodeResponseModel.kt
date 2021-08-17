package com.onelink.nrlp.android.features.select.country.model

import com.google.gson.annotations.SerializedName

data class CountryCodeResponseModel(@SerializedName("data") val countryCodesList: List<CountryCodeModel>)

data class CountryCodeModel(val country : String, val code : String, @SerializedName("number_length") val _length : Int){
    val length: String
        get() = _length.toString()
}

