package com.onelink.nrlp.android.features.receiver.models

import com.google.gson.annotations.SerializedName

data class ReceiversResponseModel(@SerializedName("data") val data: ArrayList<ReceiverDetailsModel>)