package com.onelink.nrlp.android.base

import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.features.nrlpBenefits.model.NrlpBenefitsResponseModel
import com.onelink.nrlp.android.features.nrlpBenefits.model.NrlpPartnerResponseModel
import com.onelink.nrlp.android.features.nrlpBenefits.repo.NrlpBenefitsRepo
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock

class NrlpRepoTest : BaseRepoTest() {

    @Mock
    lateinit var redeemPartnersResponse: BaseResponse<NrlpPartnerResponseModel>
    @Mock
    lateinit var nrlpPartnerBenefitsResponse: BaseResponse<NrlpBenefitsResponseModel>

    private lateinit var nrlpBenefitsRepo: NrlpBenefitsRepo

    override fun setUp() {
        super.setUp()
        nrlpBenefitsRepo = NrlpBenefitsRepo(networkHelper, serviceGateway)
    }

    @Test
    fun onClearSuccess() {

        assertNull(networkHelper.disposable)

        networkHelper.disposable = disposable

        assertNotNull(networkHelper.disposable)

        assertEquals(disposable, networkHelper.disposable)

        assertTrue(disposable === networkHelper.disposable)

        nrlpBenefitsRepo.onClear()

        assertNull(networkHelper.disposable)

    }


    @Test
    fun onClearFailed() {
        assertFalse(
            networkHelper.disposable != null
        )

        assertFalse(networkHelper.disposable != null)

        networkHelper.disposable = disposable

        nrlpBenefitsRepo.onClear()

        assertFalse(networkHelper.disposable != null)

    }

    @Test
    fun redeemPartnersResponseEmitSuccess() {

        assertNotNull(nrlpBenefitsRepo.redeemPartnersResponse)

        assertNull(nrlpBenefitsRepo.redeemPartnersResponse.value)

        nrlpBenefitsRepo.redeemPartnersResponse.value = redeemPartnersResponse

        assertEquals(nrlpBenefitsRepo.redeemPartnersResponse.value, redeemPartnersResponse)

        assertNotNull(
            nrlpBenefitsRepo.redeemPartnersResponse.value
        )

    }

    @Test
    fun redeemPartnersResponseEmitFail() {

        assertFalse(nrlpBenefitsRepo.redeemPartnersResponse.value != null)

        assertNotEquals(nrlpBenefitsRepo.redeemPartnersResponse.value, redeemPartnersResponse)

    }

    @Test
    fun nrlpPartnerBenefitsResponseEmitSuccess() {

        assertNotNull(nrlpBenefitsRepo.nrlpPartnerBenefitsResponse)

        assertNull(nrlpBenefitsRepo.nrlpPartnerBenefitsResponse.value)

        nrlpBenefitsRepo.nrlpPartnerBenefitsResponse.value = nrlpPartnerBenefitsResponse

        assertEquals(nrlpBenefitsRepo.nrlpPartnerBenefitsResponse.value, nrlpPartnerBenefitsResponse)

        assertNotNull(
            nrlpBenefitsRepo.nrlpPartnerBenefitsResponse.value
        )

    }

    @Test
    fun nrlpPartnerBenefitsResponseEmitFail() {

        assertFalse(nrlpBenefitsRepo.nrlpPartnerBenefitsResponse.value != null)

        assertNotEquals(nrlpBenefitsRepo.nrlpPartnerBenefitsResponse.value, nrlpPartnerBenefitsResponse)

    }

}