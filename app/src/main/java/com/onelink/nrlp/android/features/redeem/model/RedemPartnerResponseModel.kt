package com.onelink.nrlp.android.features.redeem.model

import com.google.gson.annotations.SerializedName

data class RedeemPartnerResponseModel(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<RedeemPartnerModel>
)

data class RedeemPartnerModel(
    @SerializedName("id") val id: Int,
    @SerializedName("partner_name") var partnerName: String,
    @SerializedName("categories") val categories: List<RedeemCategoryModel>
)

data class RedeemCategoryModel(
    @SerializedName("id") val id: Int,
    @SerializedName("catalog_name") val categoryName: String,
    @SerializedName("points_assigned") val points: Int
)