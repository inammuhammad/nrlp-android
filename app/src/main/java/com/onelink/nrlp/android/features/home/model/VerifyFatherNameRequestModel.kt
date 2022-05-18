package com.onelink.nrlp.android.features.home.model

import com.google.gson.annotations.SerializedName

data class VerifyFatherNameRequestModel(
    @SerializedName("father_name")
    var fatherName: String?
)