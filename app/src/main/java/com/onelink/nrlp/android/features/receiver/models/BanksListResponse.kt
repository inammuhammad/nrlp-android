package com.onelink.nrlp.android.features.receiver.models

import com.google.gson.annotations.SerializedName

data class BanksListResponse(@SerializedName("data") val data: ArrayList<BankDetailsModel>)