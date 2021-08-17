package com.onelink.nrlp.android.repos.register

import com.onelink.nrlp.android.features.register.models.*
import com.onelink.nrlp.android.models.GeneralMessageResponseModel

fun getVerifyReferenceNumberRequest() = VerifyReferenceNumberRequest(
    "1298",
    "2301320d83",
    "remitter",
    "209381209381",
    "23423",
    "8728123tdahsbdmasnb732t81t327812"
)

fun getVerifyReferenceNumberResponse() = GeneralMessageResponseModel(
    "Verified Successfully"
)

fun getVerifyRegistrationCodeRequest() = VerifyRegistrationCodeRequest(
    "9823904829384239",
    "remitter",
    "904233",
    "8728123tdahsbdmasnb732t81t327812"
)

fun getVerifyRegistrationCodeResponse() = GeneralMessageResponseModel(
    "Registration Code Verified Successfully"
)

fun getVerifyOTPRequest() = VerifyOTPRequest(
    "23123123129381",
    "remitter",
    "231232",
    "8728123tdahsbdmasnb732t81t327812"
)

fun getVerifyOTPResponse() = GeneralMessageResponseModel(
    "Verified Successfully"
)

fun getResendOTPRequest() = ResendOTPRequest(
    "123123123",
    "remitter",
    1,
    "8728123tdahsbdmasnb732t81t327812"
)

fun getResendOTPResponse() = GeneralMessageResponseModel(
    "Resend Successfully"
)

fun getTermsAndConditionsResponseModel() = TermsAndConditionsResponseModel(
    TermsAndConditions(
        "ABC"
    )
)

fun getRegisterRemitterRequest() = RegisterRemitterRequest(
    "941023481284091",
    "2098312093812",
    "Password@123",
    "Full Test Name",
    "remitter",
    "92034801298",
    "12312",
    "test@gmail.com",
    "8728123tdahsbdmasnb732t81t327812"
)

fun getRegisterRemitterResponse() = GeneralMessageResponseModel(
    "Register Successfully"
)

fun getRegisterBeneficiaryRequest() = RegisterBeneficiaryRequest(
    "941023481284091",
    "2098312093812",
    "Password@123",
    "Full Test Name",
    "beneficiary",
    "92034801298",
    "test@gmail.com",
    "8728123tdahsbdmasnb732t81t327812"
)

fun getRegisterBeneficiaryResponse() = GeneralMessageResponseModel(
    "Register Successfully"
)