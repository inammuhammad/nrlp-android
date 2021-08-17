package com.onelink.nrlp.android.repos.beneficiary

import com.onelink.nrlp.android.features.beneficiary.models.AddBeneficiaryRequestModel
import com.onelink.nrlp.android.features.beneficiary.models.DeleteBeneficiaryRequestModel
import com.onelink.nrlp.android.models.BeneficiariesResponseModel
import com.onelink.nrlp.android.models.BeneficiaryDetailsModel
import com.onelink.nrlp.android.models.GeneralMessageResponseModel

fun getBeneficiariesResponseModel() = BeneficiariesResponseModel(
    arrayListOf(
        BeneficiaryDetailsModel(
            1,
            1.toBigInteger(),
            "12093812093",
            1,
            "yo"
        )
    )
)

fun getDeleteBeneficiaryRequest() = DeleteBeneficiaryRequestModel(
    1
)

fun getDeleteBeneficiaryResponse() = GeneralMessageResponseModel(
    "Beneficiary Deleted"
)

fun getAddBeneficiaryRequest() = AddBeneficiaryRequestModel(
    "Name",
    "abc",
    "10293482139018"
)

fun getAddBeneficiaryResponse() = GeneralMessageResponseModel(
    "Beneficiary Added Success"
)