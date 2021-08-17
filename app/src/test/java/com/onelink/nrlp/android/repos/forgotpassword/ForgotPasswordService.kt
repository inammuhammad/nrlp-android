package com.onelink.nrlp.android.repos.forgotpassword

import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.forgotPassword.models.ForgotPasswordOTPRequestModel
import com.onelink.nrlp.android.features.forgotPassword.models.ForgotPasswordRequestModel
import com.onelink.nrlp.android.features.forgotPassword.models.UpdatePasswordRequestModel
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import io.reactivex.Single
import org.mockito.Mockito
import retrofit2.Response

class ForgotPasswordService {

    val serviceGateway: ServiceGateway = Mockito.mock(ServiceGateway::class.java)

    fun mockForgotPassword(
        request: ForgotPasswordRequestModel,
        response: GeneralMessageResponseModel
    ) {
        Mockito.`when`(serviceGateway.forgotPassword(request))
            .thenReturn(Single.just(Response.success(response)))
    }

    fun mockForgotPasswordOTP(
        request: ForgotPasswordOTPRequestModel,
        response: GeneralMessageResponseModel
    ) {
        Mockito.`when`(serviceGateway.forgotPasswordOTP(request))
            .thenReturn(Single.just(Response.success(response)))
    }

    fun mockForgotPasswordResendOTP(
        request: ForgotPasswordRequestModel,
        response: GeneralMessageResponseModel
    ) {
        Mockito.`when`(serviceGateway.forgotPasswordResendOTP(request))
            .thenReturn(Single.just(Response.success(response)))
    }

    fun mockUpdatePassword(
        request: UpdatePasswordRequestModel,
        response: GeneralMessageResponseModel
    ) {
        Mockito.`when`(serviceGateway.updatePassword(request))
            .thenReturn(Single.just(Response.success(response)))
    }
}