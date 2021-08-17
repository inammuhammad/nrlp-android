package com.onelink.nrlp.android.repos.viewstatement

import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.viewStatement.models.DetailedStatementRequestModel
import com.onelink.nrlp.android.features.viewStatement.models.LoyaltyStatementRequestModel
import com.onelink.nrlp.android.features.viewStatement.models.StatementsResponseModel
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import io.reactivex.Single
import org.mockito.Mockito
import retrofit2.Response

class ViewStatementService {

    val serviceGateway: ServiceGateway = Mockito.mock(ServiceGateway::class.java)

    fun mockGetStatements(
        request: LoyaltyStatementRequestModel,
        response: StatementsResponseModel
    ) {
        Mockito.`when`(serviceGateway.getStatements(request))
            .thenReturn(Single.just(Response.success(response)))
    }

    fun mockGetDetailedStatement(
        request: DetailedStatementRequestModel,
        response: GeneralMessageResponseModel
    ) {
        Mockito.`when`(serviceGateway.getDetailedStatement(request))
            .thenReturn(Single.just(Response.success(response)))
    }

}