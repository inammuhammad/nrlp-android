package com.onelink.nrlp.android.features.beneficiary.models

import com.google.gson.annotations.SerializedName

class UpdateBeneficiaryRequestModel(
    @SerializedName("beneficiary_id")
    val beneficiaryId:String,
    @SerializedName("nic_nicop")
    val beneficiaryNicNicop: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("mobile_no")
    val beneficiaryMobileNo: String,
    @SerializedName("relationship")
    val beneficiaryRelation: String,
    @SerializedName("country")
    val country: String?
) {
}