package com.onelink.nrlp.android.repos.selectcountry

import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.select.country.model.CountryCodeResponseModel
import io.reactivex.Single
import org.mockito.Mockito
import retrofit2.Response

class SelectCountryCodeService {

    val serviceGateway: ServiceGateway = Mockito.mock(ServiceGateway::class.java)


    fun mockGetCountryCodes(
        response: CountryCodeResponseModel
    ) {
        Mockito.`when`(serviceGateway.getCountryCodes("",""))
            .thenReturn(Single.just(Response.success(response)))
    }


}