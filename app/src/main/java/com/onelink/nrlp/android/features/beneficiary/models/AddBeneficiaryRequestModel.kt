package com.onelink.nrlp.android.features.beneficiary.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Qazi Abubakar on 11/07/2020.
 */

@Suppress("unused")
class AddBeneficiaryRequestModel(
    @SerializedName("beneficiary_nic_nicop")
    val beneficiaryNicNicop: String,
    @SerializedName("beneficiary_alias")
    val beneficiaryAlias: String,
    @SerializedName("beneficiary_mobile_no")
    val beneficiaryMobileNo: String,
    @SerializedName("beneficiary_relationship")
    val beneficiaryRelation: String
)
