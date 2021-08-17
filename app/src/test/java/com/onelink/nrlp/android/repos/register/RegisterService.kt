package com.onelink.nrlp.android.repos.register

import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.register.models.*
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import io.reactivex.Single
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import retrofit2.Response

class RegisterService {

    val serviceGateway: ServiceGateway = mock(ServiceGateway::class.java)

    fun mockVerifyReferenceNumber(
        request: VerifyReferenceNumberRequest,
        response: GeneralMessageResponseModel
    ) {
        `when`(serviceGateway.verifyReferenceNumber(request))
            .thenReturn(Single.just(Response.success(response)))

    }

    fun mockVerifyRegistrationCode(
        request: VerifyRegistrationCodeRequest,
        response: GeneralMessageResponseModel

    ) {
        `when`(serviceGateway.verifyRegistrationCode(request))
            .thenReturn(Single.just(Response.success(response)))
    }


    fun mockVerifyOTP(
        request: VerifyOTPRequest,
        response: GeneralMessageResponseModel
    ) {
        `when`(serviceGateway.verifyOTP(request))
            .thenReturn(Single.just(Response.success(response)))
    }

    fun mockResendOTP(
        request: ResendOTPRequest,
        response: GeneralMessageResponseModel
    ) {
        `when`(serviceGateway.resendOTP(request))
            .thenReturn(Single.just(Response.success(response)))
    }

    fun mockGetTermsAndConditions(response: TermsAndConditionsResponseModel) {
        `when`(serviceGateway.getTermsAndConditions())
            .thenReturn(Single.just(Response.success(response)))
    }

    fun mockRegisterRemitter(
        request: RegisterRemitterRequest,
        response: GeneralMessageResponseModel
    ) {
        `when`(serviceGateway.registerRemitter(request))
            .thenReturn(Single.just(Response.success(response)))
    }

    fun mockRegisterBeneficiary(
        request: RegisterBeneficiaryRequest,
        response: GeneralMessageResponseModel
    ) {
        `when`(serviceGateway.registerBeneficiary(request))
            .thenReturn(Single.just(Response.success(response)))
    }


}
