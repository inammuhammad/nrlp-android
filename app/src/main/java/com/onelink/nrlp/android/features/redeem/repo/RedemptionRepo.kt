package com.onelink.nrlp.android.features.redeem.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.data.NetworkHelper
import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.redeem.model.*
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import javax.inject.Inject

open class RedemptionRepo @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val serviceGateway: ServiceGateway
) {
    val redeemPartnersResponse = MutableLiveData<BaseResponse<RedeemPartnerResponseModel>>()
    val initializeRedeemResponse = MutableLiveData<BaseResponse<RedeemInitializeResponseModel>>()
    val verifyRedeemOTPResponse = MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()
    val verifyRedeemResendOTPResponse = MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()
    val redeemSuccessResponse = MutableLiveData<BaseResponse<RedeemSuccessResponseModel>>()

    fun getRedeemPartner() {
        networkHelper.serviceCall(serviceGateway.getRedemptionPartners()).observeForever {
            redeemPartnersResponse.value = it
        }
    }

    fun observeRedeemPartner() =
        redeemPartnersResponse as LiveData<BaseResponse<RedeemPartnerResponseModel>>


    fun initializeRedemption(initializeRedeemRequestModel: InitializeRedeemRequestModel) {
        networkHelper.serviceCall(serviceGateway.initializeRedemption(initializeRedeemRequestModel))
            .observeForever {
                initializeRedeemResponse.value = it
            }
    }

    fun observeInitializeRedemption() =
        initializeRedeemResponse as LiveData<BaseResponse<RedeemInitializeResponseModel>>

    fun verifyRedeemOTP(verifyRedeemOTPRequestModel: VerifyRedeemOTPRequestModel) {
        networkHelper.serviceCall(serviceGateway.verifyRedeemOTP(verifyRedeemOTPRequestModel))
            .observeForever {
                verifyRedeemOTPResponse.value = it
            }
    }

    fun observeRedeemOTP() =
        verifyRedeemOTPResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun verifyRedeemResendOTP(redeemResendOTPRequestModel: RedeemResendOTPRequestModel) {
        networkHelper.serviceCall(serviceGateway.redeemResendOTP(redeemResendOTPRequestModel))
            .observeForever {
                verifyRedeemResendOTPResponse.value = it
            }
    }

    fun observeRedeemResendOTP() =
        verifyRedeemResendOTPResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun completeRedemption(redeemCompletionRequestModel: RedeemCompletionRequestModel) {
        networkHelper.serviceCall(serviceGateway.completeRedemption(redeemCompletionRequestModel))
            .observeForever {
                redeemSuccessResponse.value = it
            }
    }

    fun observeRedeemSuccess() =
        redeemSuccessResponse as LiveData<BaseResponse<RedeemSuccessResponseModel>>

    fun onClear() {
        networkHelper.dispose()
        networkHelper.disposable = null
    }

}