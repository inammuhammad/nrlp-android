package com.onelink.nrlp.android.repos.beneficiary

import com.onelink.nrlp.android.base.BaseRepoTest
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.features.beneficiary.repo.BeneficiaryRepo
import com.onelink.nrlp.android.getOrAwaitValue
import com.onelink.nrlp.android.models.BeneficiariesResponseModel
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock
import java.util.concurrent.TimeUnit

class BeneficiaryRepoTest : BaseRepoTest() {

    @Mock
    lateinit var beneficiaryResponse: BaseResponse<BeneficiariesResponseModel>

    @Mock
    lateinit var beneficiaryDeleteResponse: BaseResponse<GeneralMessageResponseModel>

    @Mock
    lateinit var beneficiaryAddResponse: BaseResponse<GeneralMessageResponseModel>

    private lateinit var beneficiaryRepo: BeneficiaryRepo

    private lateinit var beneficiaryService: BeneficiaryService

    override fun setUp() {
        super.setUp()
        beneficiaryService = BeneficiaryService()
        beneficiaryRepo = BeneficiaryRepo(networkHelper, beneficiaryService.serviceGateway)
    }

    @Test
    fun getAllBeneficiariesTest() {
        val response = getBeneficiariesResponseModel()
        beneficiaryService.mockGetBeneficiaries(response)
        beneficiaryRepo.getAllBeneficiaries()
        beneficiaryRepo.observeAllBeneficiaries().getOrAwaitValue(10, TimeUnit.SECONDS).let {
            assertEquals(it.data, response)
        }
    }

    @Test
    fun deleteBeneficiaryTest() {

        val request = getDeleteBeneficiaryRequest()
        val response = getDeleteBeneficiaryResponse()
        beneficiaryService.mockDeleteBeneficiary(request, response)
        beneficiaryRepo.deleteBeneficiary(request)
        beneficiaryRepo.observeBeneficiaryDeleteResponse().getOrAwaitValue(10, TimeUnit.SECONDS)
            .let {
                assertEquals(it.data, response)
            }
    }

    @Test
    fun addBeneficiaryTest() {
        val request = getAddBeneficiaryRequest()
        val response = getAddBeneficiaryResponse()
        beneficiaryService.mockAddBeneficiary(request, response)
        beneficiaryRepo.addBeneficiary(request)
        beneficiaryRepo.observeBeneficiaryAddResponse().getOrAwaitValue(10, TimeUnit.SECONDS).let {
            assertEquals(it.data, response)
        }
    }

    @Test
    fun onClearSuccess() {

        assertNull(networkHelper.disposable)

        networkHelper.disposable = disposable

        assertNotNull(networkHelper.disposable)

        assertEquals(disposable, networkHelper.disposable)

        assertTrue(disposable === networkHelper.disposable)

        beneficiaryRepo.onClear()

        assertNull(networkHelper.disposable)

    }


    @Test
    fun onClearFailed() {
        assertFalse(
            networkHelper.disposable != null
        )

        assertFalse(networkHelper.disposable != null)

        networkHelper.disposable = disposable

        beneficiaryRepo.onClear()

        assertFalse(networkHelper.disposable != null)

    }

    @Test
    fun beneficiaryResponseEmitSuccess() {

        assertNotNull(beneficiaryRepo.beneficiariesResponse)

        assertNull(beneficiaryRepo.beneficiariesResponse.value)

        beneficiaryRepo.beneficiariesResponse.value = beneficiaryResponse

        assertEquals(beneficiaryRepo.beneficiariesResponse.value, beneficiaryResponse)

        assertNotNull(
            beneficiaryRepo.beneficiariesResponse.value
        )

    }

    @Test
    fun beneficiaryResponseEmitFail() {

        assertFalse(beneficiaryRepo.beneficiariesResponse.value != null)

        assertNotEquals(beneficiaryRepo.beneficiariesResponse.value, beneficiaryResponse)

    }


    @Test
    fun beneficiaryDeleteResponseEmitSuccess() {

        assertNotNull(beneficiaryRepo.beneficiaryDeleteResponse)

        assertNull(beneficiaryRepo.beneficiaryDeleteResponse.value)

        beneficiaryRepo.beneficiaryDeleteResponse.value = beneficiaryDeleteResponse

        assertEquals(beneficiaryRepo.beneficiaryDeleteResponse.value, beneficiaryDeleteResponse)

        assertNotNull(
            beneficiaryRepo.beneficiaryDeleteResponse.value
        )

    }

    @Test
    fun beneficiaryDeleteResponseEmitFail() {

        assertFalse(beneficiaryRepo.beneficiaryDeleteResponse.value != null)

        assertNotEquals(beneficiaryRepo.beneficiaryDeleteResponse.value, beneficiaryDeleteResponse)

    }

    @Test
    fun beneficiaryAddResponseEmitSuccess() {

        assertNotNull(beneficiaryRepo.beneficiaryAddResponse)

        assertNull(beneficiaryRepo.beneficiaryAddResponse.value)

        beneficiaryRepo.beneficiaryAddResponse.value = beneficiaryAddResponse

        assertEquals(beneficiaryRepo.beneficiaryAddResponse.value, beneficiaryAddResponse)

        assertNotNull(
            beneficiaryRepo.beneficiaryAddResponse.value
        )

    }

    @Test
    fun beneficiaryAddResponseEmitFail() {

        assertFalse(beneficiaryRepo.beneficiaryAddResponse.value != null)

        assertNotEquals(beneficiaryRepo.beneficiaryAddResponse.value, beneficiaryAddResponse)

    }
}