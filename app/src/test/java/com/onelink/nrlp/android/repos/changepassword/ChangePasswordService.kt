package com.onelink.nrlp.android.repos.changepassword

import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.changePassword.models.ChangePasswordRequest
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import io.reactivex.Single
import org.mockito.Mockito
import retrofit2.Response

class ChangePasswordService {

    val serviceGateway: ServiceGateway = Mockito.mock(ServiceGateway::class.java)

    fun mockChangePassword(
        request: ChangePasswordRequest,
        response: GeneralMessageResponseModel
    ) {
        Mockito.`when`(serviceGateway.changePassword(request))
            .thenReturn(Single.just(Response.success(response)))
    }

}