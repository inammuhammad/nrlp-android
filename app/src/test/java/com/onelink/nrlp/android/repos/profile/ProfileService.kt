package com.onelink.nrlp.android.repos.profile

import com.google.gson.JsonObject
import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.select.country.model.CountryCodeResponseModel
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import io.reactivex.Single
import org.mockito.Mockito
import retrofit2.Response

class ProfileService {

    val serviceGateway: ServiceGateway = Mockito.mock(ServiceGateway::class.java)


    fun mockGetCountryCodes(
        response: CountryCodeResponseModel
    ) {
        Mockito.`when`(serviceGateway.getCountryCodes("",""))
            .thenReturn(Single.just(Response.success(response)))
    }

    fun mockUpdateProfile(
        request: JsonObject,
        response: GeneralMessageResponseModel
    ) {
        Mockito.`when`(serviceGateway.updateProfile(request))
            .thenReturn(Single.just(Response.success(response)))
    }

    fun mockVerifyOtp(
        request: JsonObject,
        response: GeneralMessageResponseModel
    ) {
        Mockito.`when`(serviceGateway.updateProfileVerifyOtp(request))
            .thenReturn(Single.just(Response.success(response)))
    }

    fun mockVerifyResendOtp(
        request: JsonObject,
        response: GeneralMessageResponseModel
    ) {
        Mockito.`when`(serviceGateway.updateProfileResendOtp(request))
            .thenReturn(Single.just(Response.success(response)))
    }

}