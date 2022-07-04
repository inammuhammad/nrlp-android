package com.onelink.nrlp.android.features.select.generic.model

import com.google.gson.annotations.SerializedName

data class BranchCenterResponseModel(
    @SerializedName("data") val branchCenterList: ArrayList<BranchCenterModel>
)

data class BranchCenterModel(
    @SerializedName("country_name") val branchCenterName: String
)