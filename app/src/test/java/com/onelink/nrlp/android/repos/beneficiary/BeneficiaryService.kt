package com.onelink.nrlp.android.repos.beneficiary

import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.beneficiary.models.AddBeneficiaryRequestModel
import com.onelink.nrlp.android.features.beneficiary.models.DeleteBeneficiaryRequestModel
import com.onelink.nrlp.android.models.BeneficiariesResponseModel
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import io.reactivex.Single
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import retrofit2.Response

class BeneficiaryService {

    val serviceGateway: ServiceGateway = mock(ServiceGateway::class.java)

    fun mockGetBeneficiaries(response: BeneficiariesResponseModel) {
        `when`(serviceGateway.getBeneficiaries())
            .thenReturn(Single.just(Response.success(response)))
    }

    fun mockDeleteBeneficiary(
        request: DeleteBeneficiaryRequestModel,
        response: GeneralMessageResponseModel
    ) {
        `when`(serviceGateway.deleteBeneficiary(request))
            .thenReturn(Single.just(Response.success(response)))
    }

    fun mockAddBeneficiary(
        request: AddBeneficiaryRequestModel,
        response: GeneralMessageResponseModel
    ) {
        `when`(serviceGateway.addBeneficiary(request))
            .thenReturn(Single.just(Response.success(response)))
    }


}
