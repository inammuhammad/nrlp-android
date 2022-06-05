package com.onelink.nrlp.android.features.rate.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.data.NetworkHelper
import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import javax.inject.Inject

class RatingRepo @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val serviceGateway: ServiceGateway
) {
    val rateRedemptionResponse = MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()
    val rateRegistrationResponse = MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()

    fun rateRedemption(jsonObject: JsonObject) {
        networkHelper.serviceCall(serviceGateway.rateRedemption(jsonObject))
            .observeForever {
                rateRedemptionResponse.value = it
            }
    }

    fun rateRegistration(jsonObject: JsonObject) {
        networkHelper.serviceCall(serviceGateway.rateRegistration(jsonObject))
            .observeForever {
                rateRegistrationResponse.value = it
            }
    }

    fun observeRateRedemptionSuccess() =
        rateRedemptionResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun observeRateRegistrationSuccess() =
        rateRegistrationResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun onClear() {
        networkHelper.dispose()
        networkHelper.disposable = null
    }
}