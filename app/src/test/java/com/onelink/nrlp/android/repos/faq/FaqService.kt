package com.onelink.nrlp.android.repos.faq

import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.faqs.model.FaqsResponseModel
import io.reactivex.Single
import org.mockito.Mockito
import retrofit2.Response

class FaqService {

    val serviceGateway: ServiceGateway = Mockito.mock(ServiceGateway::class.java)

    fun mockGetFaqs(response: FaqsResponseModel) {
        Mockito.`when`(serviceGateway.getFaqs())
            .thenReturn(Single.just(Response.success(response)))
    }
}