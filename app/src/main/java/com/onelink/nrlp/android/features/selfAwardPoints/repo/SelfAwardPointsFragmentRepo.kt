package com.onelink.nrlp.android.features.selfAwardPoints.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.data.NetworkHelper
import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.selfAwardPoints.model.SelfAwardPointsOTPRequestModel
import com.onelink.nrlp.android.features.selfAwardPoints.model.SelfAwardPointsRequest
import com.onelink.nrlp.android.features.selfAwardPoints.model.SelfAwardPointsResponseModel
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import javax.inject.Inject

open class SelfAwardPointsFragmentRepo @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val serviceGateway: ServiceGateway
) {
    val verifyReferenceNumResponse =
            MutableLiveData<BaseResponse<SelfAwardPointsResponseModel>>()

    fun observeSelfAwardValidTransaction() =
            verifyReferenceNumResponse as LiveData<BaseResponse<SelfAwardPointsResponseModel>>

    fun verifySelfAwardValidTransaction(selfAwardPointsRequest: SelfAwardPointsRequest) {
        networkHelper.serviceCall(serviceGateway.verifySelfAwardPoints(selfAwardPointsRequest))
                .observeForever {
            verifyReferenceNumResponse.value = it
        }
    }

    val selfAwardPointsOTPResponse =
        MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()
    val selfAwardPointsResendOTPResponse =
        MutableLiveData<BaseResponse<SelfAwardPointsResponseModel>>()

    fun observeSelfAwardPointsOTPResponse() =
        selfAwardPointsOTPResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun selfAwardPointsOTP(selfAwardPointsOTPRequestModel: SelfAwardPointsOTPRequestModel) {
        networkHelper.serviceCall(serviceGateway.selfAwardPointsOTP(selfAwardPointsOTPRequestModel))
            .observeForever {
                selfAwardPointsOTPResponse.value = it
        }
    }

    fun observeSelfAwardPointsResendOTPResponse() =
        selfAwardPointsResendOTPResponse as LiveData<BaseResponse<SelfAwardPointsResponseModel>>

    fun selfAwardPointsResendOTP(selfAwardPointsRequest: SelfAwardPointsRequest) {
        networkHelper.serviceCall(serviceGateway.verifySelfAwardPoints(selfAwardPointsRequest))
            .observeForever {
                selfAwardPointsResendOTPResponse.value = it
        }
    }


    fun onClear() {
        networkHelper.dispose()
        networkHelper.disposable = null
    }
}
