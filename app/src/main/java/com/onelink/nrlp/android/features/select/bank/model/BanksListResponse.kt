package com.onelink.nrlp.android.features.select.bank.model

import com.google.gson.annotations.SerializedName

data class BanksListResponse(@SerializedName("data") val data: ArrayList<BankDetailsModel>)