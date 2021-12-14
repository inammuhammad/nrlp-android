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
    val initializeRedeemFBRResponse = MutableLiveData<BaseResponse<RedeemInitializeFBRResponseModel>>()
    val initializeRedeemFBROTPResponse = MutableLiveData<BaseResponse<RedeemInitializeFBROTPResponseModel>>()
    val verifyRedeemOTPResponse = MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()
    val verifyRedeemResendOTPResponse = MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()
    val redeemSuccessResponse = MutableLiveData<BaseResponse<RedeemSuccessResponseModel>>()
    val redeemFBRSuccessResponse = MutableLiveData<BaseResponse<RedeemFBRSuccessResponseModel>>()

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

    fun initializeRedemptionFBR(initializeRedeemFBRRequestModel: InitializeRedeemFBRRequestModel) {
        networkHelper.serviceCall(serviceGateway.initializeRedemptionFBR(initializeRedeemFBRRequestModel))
            .observeForever {
                initializeRedeemFBRResponse.value = it
            }
    }

    fun initializeRedemptionNadra(initializeRedeemNadraRequestModel: InitializeRedeemNadraRequestModel) {
        networkHelper.serviceCall(serviceGateway.initializeRedemptionNADRA(initializeRedeemNadraRequestModel))
            .observeForever {
                initializeRedeemFBRResponse.value = it
            }
    }

    fun initializeRedemptionPIA(initializeRedeemPIARequestModel: InitializeRedeemPIARequestModel) {
        networkHelper.serviceCall(serviceGateway.initializeRedemptionPIA(initializeRedeemPIARequestModel))
            .observeForever {
                initializeRedeemFBRResponse.value = it
            }
    }

    fun initializeRedemptionOPF(initializeRedeemOPFRequestModel: InitializeRedeemOPFRequestModel) {
        networkHelper.serviceCall(serviceGateway.initializeRedemptionOPF(initializeRedeemOPFRequestModel))
            .observeForever {
                initializeRedeemFBRResponse.value = it
            }
    }

    fun initializeRedemptionFBROTP(initializeRedeemFBROTPRequestModel: InitializeRedeemFBROTPRequestModel) {
        networkHelper.serviceCall(serviceGateway.initializeRedemptionFBROTP(initializeRedeemFBROTPRequestModel))
            .observeForever {
                initializeRedeemFBROTPResponse.value = it
            }
    }
    fun initializeRedemptionNadraOTP(initializeRedeemNadraOTPRequestModel: InitializeRedeemNadraOTPRequestModel) {
        networkHelper.serviceCall(serviceGateway.initializeRedemptionNadraOTP(initializeRedeemNadraOTPRequestModel))
            .observeForever {
                initializeRedeemFBROTPResponse.value = it
            }
    }

    fun initializeRedemptionPIAOTP(initializeRedeemPIAOTPRequestModel: InitializeRedeemPIAOTPRequestModel) {
        networkHelper.serviceCall(serviceGateway.initializeRedemptionPIAOTP(initializeRedeemPIAOTPRequestModel))
            .observeForever {
                initializeRedeemFBROTPResponse.value = it
            }
    }

    fun initializeRedemptionPassportOTP(initializeRedeemPassportOTPRequestModel: InitializeRedeemPassportOTPRequestModel) {
        networkHelper.serviceCall(serviceGateway.initializeRedemptionPassportOTP(initializeRedeemPassportOTPRequestModel))
            .observeForever {
                initializeRedeemFBROTPResponse.value = it
            }
    }

    fun initializeRedemptionOPFOTP(initializeRedeemOPFOTPRequestModel: InitializeRedeemOPFOTPRequestModel) {
        networkHelper.serviceCall(serviceGateway.initializeRedemptionOPFOTP(initializeRedeemOPFOTPRequestModel))
            .observeForever {
                initializeRedeemFBROTPResponse.value = it
            }
    }

    fun observeInitializeRedemption() =
        initializeRedeemResponse as LiveData<BaseResponse<RedeemInitializeResponseModel>>

    fun observeInitializeRedemptionFBR() =
        initializeRedeemFBRResponse as LiveData<BaseResponse<RedeemInitializeFBRResponseModel>>

    fun observeInitializeRedemptionFBROTP() =
        initializeRedeemFBROTPResponse as LiveData<BaseResponse<RedeemInitializeFBROTPResponseModel>>

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

    fun completeRedemptionFBR(redeemCompletionFBRRequestModel: RedeemCompletionFBRRequestModel) {
        networkHelper.serviceCall(serviceGateway.completeRedemptionFBR(redeemCompletionFBRRequestModel))
            .observeForever {
                redeemFBRSuccessResponse.value = it
            }
    }

    fun observeRedeemSuccess() =
        redeemSuccessResponse as LiveData<BaseResponse<RedeemSuccessResponseModel>>

    fun observeRedeemFBRSuccess() =
        redeemFBRSuccessResponse as LiveData<BaseResponse<RedeemFBRSuccessResponseModel>>

    fun onClear() {
        networkHelper.dispose()
        networkHelper.disposable = null
    }

}