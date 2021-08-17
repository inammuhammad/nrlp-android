package com.onelink.nrlp.android.repos.managepoints

import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.managePoints.model.TransferPointsRequest
import com.onelink.nrlp.android.features.managePoints.model.TransferPointsResponseModel
import com.onelink.nrlp.android.models.BeneficiariesResponseModel
import io.reactivex.Single
import org.mockito.Mockito
import retrofit2.Response

class ManagePointsService {

    val serviceGateway: ServiceGateway = Mockito.mock(ServiceGateway::class.java)


    fun mockGetAllBeneficiaries(
        response: BeneficiariesResponseModel
    ) {
        Mockito.`when`(serviceGateway.getBeneficiaries())
            .thenReturn(Single.just(Response.success(response)))
    }

    fun mockTransferPoints(
        request: TransferPointsRequest,
        response: TransferPointsResponseModel
    ) {
        Mockito.`when`(serviceGateway.transferPoints(request))
            .thenReturn(Single.just(Response.success(response)))
    }
}