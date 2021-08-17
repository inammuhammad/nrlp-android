package com.onelink.nrlp.android.features.nrlpBenefits.model

import com.google.gson.annotations.SerializedName

data class NrlpBenefitsResponseModel(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: BenefitDataModel
)

data class BenefitDataModel(
    @SerializedName("partner_catalogs") val partnerCatalogs: List<NrlpBenefitModel>
)

data class NrlpBenefitModel(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("img_src") val imgSrc: String?
)
