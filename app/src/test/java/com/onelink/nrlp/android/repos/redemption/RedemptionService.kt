package com.onelink.nrlp.android.repos.redemption

import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.redeem.model.*
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import io.reactivex.Single
import org.mockito.Mockito
import retrofit2.Response

class RedemptionService {

    val serviceGateway: ServiceGateway = Mockito.mock(ServiceGateway::class.java)

    fun mockGetRedemptionPartners(
        response: RedeemPartnerResponseModel
    ) {
        Mockito.`when`(serviceGateway.getRedemptionPartners())
            .thenReturn(Single.just(Response.success(response)))
    }

    fun mockInitializeRedemption(
        request: InitializeRedeemRequestModel,
        response: RedeemInitializeResponseModel
    ) {
        Mockito.`when`(serviceGateway.initializeRedemption(request))
            .thenReturn(Single.just(Response.success(response)))
    }


    fun mockVerifyRedeemOTP(
        request: VerifyRedeemOTPRequestModel,
        response: GeneralMessageResponseModel
    ) {
        Mockito.`when`(serviceGateway.verifyRedeemOTP(request))
            .thenReturn(Single.just(Response.success(response)))
    }


    fun mockVerifyRedeemResendOTP(
        request: RedeemResendOTPRequestModel,
        response: GeneralMessageResponseModel
    ) {
        Mockito.`when`(serviceGateway.redeemResendOTP(request))
            .thenReturn(Single.just(Response.success(response)))
    }


    fun mockCompleteRedemption(
        request: RedeemCompletionRequestModel,
        response: RedeemSuccessResponseModel
    ) {
        Mockito.`when`(serviceGateway.completeRedemption(request))
            .thenReturn(Single.just(Response.success(response)))
    }

}