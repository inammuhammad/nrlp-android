package com.onelink.nrlp.android.repos.managepoints

import com.onelink.nrlp.android.features.managePoints.model.TransferPointsRequest
import com.onelink.nrlp.android.features.managePoints.model.TransferPointsResponseModel
import com.onelink.nrlp.android.models.BeneficiariesResponseModel
import com.onelink.nrlp.android.models.BeneficiaryDetailsModel

fun getBeneficiaryResponse() = BeneficiariesResponseModel(
    arrayListOf(
        BeneficiaryDetailsModel(
            1,
            123.toBigInteger(),
            "!2312312",
            1,
            "123"
        )
    )
)

fun getTransferPointsRequest() = TransferPointsRequest(
    "12",
    "1233"
)

fun getTransferPointsResponse() = TransferPointsResponseModel(
    "Points Transferred Successfully"
)