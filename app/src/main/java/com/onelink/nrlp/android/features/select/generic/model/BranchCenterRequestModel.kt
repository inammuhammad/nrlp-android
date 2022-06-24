package com.onelink.nrlp.android.features.select.generic.model

import com.google.gson.annotations.SerializedName

data class BranchCenterRequestModel(
    @SerializedName("pse_name") val pseName: String?
)