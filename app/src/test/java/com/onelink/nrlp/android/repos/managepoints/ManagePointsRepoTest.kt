package com.onelink.nrlp.android.repos.managepoints

import com.onelink.nrlp.android.base.BaseRepoTest
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.features.managePoints.model.TransferPointsResponseModel
import com.onelink.nrlp.android.features.managePoints.repo.ManagePointsFragmentRepo
import com.onelink.nrlp.android.getOrAwaitValue
import com.onelink.nrlp.android.models.BeneficiariesResponseModel
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock
import java.util.concurrent.TimeUnit

class ManagePointsRepoTest : BaseRepoTest() {

    @Mock
    lateinit var beneficiariesResponse: BaseResponse<BeneficiariesResponseModel>

    @Mock
    lateinit var transferPointsResponse: BaseResponse<TransferPointsResponseModel>

    private lateinit var managePointsRepo: ManagePointsFragmentRepo

    private lateinit var managePointsService: ManagePointsService

    override fun setUp() {
        super.setUp()
        managePointsService = ManagePointsService()
        managePointsRepo =
            ManagePointsFragmentRepo(networkHelper, managePointsService.serviceGateway)
    }


    @Test
    fun getAllBeneficiariesTest() {
        val response = getBeneficiaryResponse()
        managePointsService.mockGetAllBeneficiaries(response)
        managePointsRepo.getAllBeneficiaries()
        managePointsRepo.observeBeneficiaryResponse().getOrAwaitValue(10, TimeUnit.SECONDS).let {
            assertEquals(
                it.data,
                response
            )
        }
    }

    @Test
    fun transferPointsTest() {
        val request = getTransferPointsRequest()
        val response = getTransferPointsResponse()
        managePointsService.mockTransferPoints(request, response)
        managePointsRepo.transferPoints(request)
        managePointsRepo.observeTransferPoints().getOrAwaitValue(10, TimeUnit.SECONDS).let {
            assertEquals(
                it.data,
                response
            )
        }
    }

    @Test
    fun beneficiariesResponseEmitSuccess() {

        assertNotNull(managePointsRepo.beneficiariesResponse)

        assertNull(managePointsRepo.beneficiariesResponse.value)

        managePointsRepo.beneficiariesResponse.value = beneficiariesResponse

        assertEquals(managePointsRepo.beneficiariesResponse.value, beneficiariesResponse)

        assertNotNull(
            managePointsRepo.beneficiariesResponse.value
        )

    }

    @Test
    fun beneficiariesResponseEmitFail() {

        assertFalse(managePointsRepo.beneficiariesResponse.value != null)

        assertNotEquals(managePointsRepo.beneficiariesResponse.value, beneficiariesResponse)

    }

    @Test
    fun transferPointsResponseEmitSuccess() {

        assertNotNull(managePointsRepo.transferPointsResponse)

        assertNull(managePointsRepo.transferPointsResponse.value)

        managePointsRepo.transferPointsResponse.value = transferPointsResponse

        assertEquals(managePointsRepo.transferPointsResponse.value, transferPointsResponse)

        assertNotNull(
            managePointsRepo.transferPointsResponse.value
        )

    }

    @Test
    fun transferPointsResponseEmitFail() {

        assertFalse(managePointsRepo.transferPointsResponse.value != null)

        assertNotEquals(
            managePointsRepo.transferPointsResponse.value,
            transferPointsResponse
        )

    }

}