package com.onelink.nrlp.android.repos.redemption

import com.onelink.nrlp.android.features.redeem.model.*
import com.onelink.nrlp.android.models.GeneralMessageResponseModel

fun getRedeemPartnerResponse() = RedeemPartnerResponseModel(
    "Hello",
    listOf(
        RedeemPartnerModel(
            2,
            "ASKdjas",
            listOf(
                RedeemCategoryModel(
                    1,
                    "Cate1",
                    1
                )
            )
        )
    )
)


fun getInitializeRedeemRequest() = InitializeRedeemRequestModel(
    1,
    1,
    1.toBigInteger()
)

fun getRedeemInitializeResponse() = RedeemInitializeResponseModel(
    "Hello",
    "Yo"
)

fun getVerifyRedeemOTPRequest() = VerifyRedeemOTPRequestModel(
    "1weq08q-09-09",
    "122112"
)

fun getVerifyRedeemOTPResponse() = GeneralMessageResponseModel(
    "Redeemed Successfully"
)

fun getRedeemResendOTPRequest() = RedeemResendOTPRequestModel(
    "12309812081xn813"
)

fun getRedeemResendOTPResponse() = GeneralMessageResponseModel(
    "Redeem Resend OTP response"
)

fun getRedeemCompletionRequest() = RedeemCompletionRequestModel(
    "20981023nx10",
    "12212"
)

fun getRedeemSuccessResponse() = RedeemSuccessResponseModel(
    "Redeem Success",
    RedeemSuccessDataModel(
        "asdjalskdja92112-0120-w"
    )
)