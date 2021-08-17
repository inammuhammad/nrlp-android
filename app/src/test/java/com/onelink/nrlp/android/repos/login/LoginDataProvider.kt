package com.onelink.nrlp.android.repos.login

import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.data.local.UserModel
import com.onelink.nrlp.android.features.login.model.LoginModel
import com.onelink.nrlp.android.features.login.model.LoginRequest
import com.onelink.nrlp.android.features.login.model.LoginResponseModel
import com.onelink.nrlp.android.features.uuid.model.UniqueIdentifierResendOTPRequest
import com.onelink.nrlp.android.features.uuid.model.UniqueIdentifierUpdateRequest
import com.onelink.nrlp.android.models.GeneralMessageResponseModel

fun getDefaultLoginModel() = LoginModel(
    2,
    "Test Name",
    "203049823049823".toBigInteger(),
    "2309482304823",
    "test@gmail.com",
    "remitter",
    "bronze",
    "1.0".toBigDecimal()
)

fun getDefaultLoginResponseModel(model: LoginModel) = LoginResponseModel(
    "Test Message",
    "kasddjfaaasdd0n9fuapsd8f0cn8as0f8n098aw0c9d8fa0n98c4r8a0w98330ac830rca8308ca309nc8",
    model,
    "100000",
    "10000",
    "n8as0f8n9d8fa0n98c4r8a0w98330ac830"
)

fun getDefaultLoginResponse(loginResponseModel: LoginResponseModel) =
    BaseResponse(Status.SUCCESS, loginResponseModel, null, "success", 200)

fun getDefaultLoginRequest() = LoginRequest(
    "47777-77777-8",
    "Fariha@123",
    "remitter"
)

fun getDefaultUserModel() = UserModel(
    "kasddjfaaasdd0n9fuapsd8f0cn8as0f8n098aw0c9d8fa0n98c4r8a0w98330ac830rca8308ca309nc8",
    2,
    "Test Name",
    "203049823049823".toBigInteger(),
    "2309482304823",
    "test@gmail.com",
    "remitter",
    "bronze",
    "1.0".toBigDecimal(),
    "n8as0f8n9d8fa0n98c4r8a0w98330ac830",
    10000,
    100000
)

fun getAnotherUserModel() = UserModel(
    "kasddjfaaasdd0n9fuapsd8f0cn8as0f8n098aw0c9d8fa0n98c4r8a0w98330ac830rca8308ca309nc8",
    2,
    "Another Name",
    "2345234345".toBigInteger(),
    "123514",
    "another@gmail.com",
    "remitter",
    "bronze",
    "1.0".toBigDecimal(),
    "n8as0f8n9d8fa0n98c4r8a0w98330ac830",
    10000,
    100000
)

fun getUniqueIdentifierUpdateRequest() = UniqueIdentifierUpdateRequest(
    "1hl4kj1h24khl12kh4l1k23h4",
    "Password@123",
    "remitter",
    "123321",
    "8728123tdahsbdmasnb732t81t327812"
)

fun getUniqueIdentifierUpdateResponse() = GeneralMessageResponseModel(
    "Updated Successfully"
)

fun getUniqueIdentifierResendOTPRequest() = UniqueIdentifierResendOTPRequest(
    "2391290231xn10i312830",
    "Password@123",
    "remitter",
    "8728123tdahsbdmasnb732t81t327812"
)

fun getUuidResendOTPResponse() = GeneralMessageResponseModel(
    "Resend Successfully"
)