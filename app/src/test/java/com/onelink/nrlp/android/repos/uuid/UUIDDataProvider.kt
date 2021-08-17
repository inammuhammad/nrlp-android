package com.onelink.nrlp.android.repos.uuid

import com.onelink.nrlp.android.features.uuid.model.UniqueIdentifierUpdateRequest
import com.onelink.nrlp.android.utils.mocks.MockedAPIResponseModels

fun getUniqueIdentifierUpdateRequest() = UniqueIdentifierUpdateRequest(
    "2348230480238",
    "Password@123",
    "remitter",
    "123123",
    "asdasdasdasdasd343456787342342342"
)

fun getUniqueIdentifierUpdateResponse() = MockedAPIResponseModels.getMockedVerifyOTPAPIResponse()