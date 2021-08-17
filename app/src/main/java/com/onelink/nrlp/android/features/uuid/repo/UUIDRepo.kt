package com.onelink.nrlp.android.features.uuid.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.data.NetworkHelper
import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.uuid.model.UniqueIdentifierUpdateRequest
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import com.onelink.nrlp.android.utils.mocks.MockedAPIResponseModels
import javax.inject.Inject

@Suppress("unused")
class UUIDRepo @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val serviceGateway: ServiceGateway
) {

    val updateUUIDResponse = MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()
    val uuidResendOTPResponse = MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()

    @Suppress("UNUSED_PARAMETER")
    fun uniqueIdentifierUpdate(uniqueIdentifierUpdateRequest: UniqueIdentifierUpdateRequest) {
        updateUUIDResponse.value = MockedAPIResponseModels.getMockedVerifyOTPAPIResponse()
        return
        /*networkHelper.serviceCall(
            serviceGateway.uniqueIdentifierUpdate(
                uniqueIdentifierUpdateRequest
            )
        ).observeForever {
            updateUUIDResponse.value = it
        }*/
    }

    fun getObserverUpdateUUIDResponse() =
        updateUUIDResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

/*    fun verifyRedeemResendOTP() {
        uuidResendOTPResponse.value = MockedAPIResponseModels.getMockedVerifyOTPAPIResponse()
        return
        networkHelper.serviceCall(
            serviceGateway.redeemResendOTP(
                ResendOTPRequest(
                    "",
                    "",
                    1
                )
            )
        ).observeForever {
            uuidResendOTPResponse.value = it
        }
    }*/

    fun onClear() {
        networkHelper.dispose()
        networkHelper.disposable = null
    }
}
