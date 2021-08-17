package com.onelink.nrlp.android.repos.home

import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.home.model.UserProfileResponseModel
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import io.reactivex.Single
import org.mockito.Mockito
import retrofit2.Response

class HomeService {

    val serviceGateway: ServiceGateway = Mockito.mock(ServiceGateway::class.java)

    fun mockGetUserProfile(response: UserProfileResponseModel) {
        Mockito.`when`(serviceGateway.getUserProfile())
            .thenReturn(Single.just(Response.success(response)))
    }

    fun mockPerformLogout(response: GeneralMessageResponseModel) {
        Mockito.`when`(serviceGateway.performLogout())
            .thenReturn(Single.just(Response.success(response)))
    }
}