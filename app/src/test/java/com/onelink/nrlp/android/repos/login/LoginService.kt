package com.onelink.nrlp.android.repos.login

import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.login.model.LoginRequest
import com.onelink.nrlp.android.features.login.model.LoginResponseModel
import com.onelink.nrlp.android.features.uuid.model.UniqueIdentifierResendOTPRequest
import com.onelink.nrlp.android.features.uuid.model.UniqueIdentifierUpdateRequest
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import io.reactivex.Single
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import retrofit2.Response

class LoginService {

    val serviceGateway: ServiceGateway = mock(ServiceGateway::class.java)

    fun mockLogin(request: LoginRequest, response: LoginResponseModel) {
        `when`(serviceGateway.login(request))
            .thenReturn(Single.just(Response.success(response)))
    }

    fun mockUniqueIdentifierUpdate(
        request: UniqueIdentifierUpdateRequest,
        response: GeneralMessageResponseModel
    ) {
        `when`(serviceGateway.uniqueIdentifierUpdate(request))
            .thenReturn(Single.just(Response.success(response)))
    }

    fun mockVerifyUUIDResendOTP(
        request: UniqueIdentifierResendOTPRequest,
        response: GeneralMessageResponseModel
    ) {
        `when`(serviceGateway.uniqueIdentifierResendOTP(request))
            .thenReturn(Single.just(Response.success(response)))
    }


}
