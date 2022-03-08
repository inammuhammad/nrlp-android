package com.onelink.nrlp.android.features.select.bank.model

import com.google.gson.annotations.SerializedName

data class BankDetailsModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)