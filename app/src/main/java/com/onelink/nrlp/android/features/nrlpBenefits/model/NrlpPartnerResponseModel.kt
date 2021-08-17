package com.onelink.nrlp.android.features.nrlpBenefits.model

import com.google.gson.annotations.SerializedName

data class NrlpPartnerResponseModel(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: PartnerDataModel
)

data class PartnerDataModel(
    @SerializedName("redemption_partners") val redemptionPartners: List<RedemptionPartnerModel>
)

data class RedemptionPartnerModel(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("img_src") val imgSrc: String
)
