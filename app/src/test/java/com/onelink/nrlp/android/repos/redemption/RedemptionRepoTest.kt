package com.onelink.nrlp.android.repos.redemption

import com.onelink.nrlp.android.base.BaseRepoTest
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.features.redeem.model.RedeemInitializeResponseModel
import com.onelink.nrlp.android.features.redeem.model.RedeemPartnerResponseModel
import com.onelink.nrlp.android.features.redeem.model.RedeemSuccessResponseModel
import com.onelink.nrlp.android.features.redeem.repo.RedemptionRepo
import com.onelink.nrlp.android.getOrAwaitValue
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock
import java.util.concurrent.TimeUnit

class RedemptionRepoTest : BaseRepoTest() {

    @Mock
    lateinit var redeemPartnersResponse: BaseResponse<RedeemPartnerResponseModel>

    @Mock
    lateinit var initializeRedeemResponse: BaseResponse<RedeemInitializeResponseModel>

    @Mock
    lateinit var verifyRedeemOTPResponse: BaseResponse<GeneralMessageResponseModel>

    @Mock
    lateinit var verifyRedeemResendOTPResponse: BaseResponse<GeneralMessageResponseModel>

    @Mock
    lateinit var redeemSuccessResponse: BaseResponse<RedeemSuccessResponseModel>

    private lateinit var redemptionRepo: RedemptionRepo

    private lateinit var redemptionService: RedemptionService

    override fun setUp() {
        super.setUp()
        redemptionService = RedemptionService()
        redemptionRepo = RedemptionRepo(networkHelper, redemptionService.serviceGateway)
    }


    @Test
    fun getRedeemPartnerTest() {
        val response = getRedeemPartnerResponse()
        redemptionService.mockGetRedemptionPartners(response)
        redemptionRepo.getRedeemPartner()
        redemptionRepo.observeRedeemPartner().getOrAwaitValue(10, TimeUnit.SECONDS).let {
            assertEquals(
                it.data,
                response
            )
        }
    }

    @Test
    fun initializeRedemptionTest() {
        val request = getInitializeRedeemRequest()
        val response = getRedeemInitializeResponse()
        redemptionService.mockInitializeRedemption(request, response)
        redemptionRepo.initializeRedemption(request)
        redemptionRepo.observeInitializeRedemption().getOrAwaitValue(10, TimeUnit.SECONDS).let {
            assertEquals(
                it.data,
                response
            )
        }
    }

    @Test
    fun verifyRedeemOTPTest() {
        val request = getVerifyRedeemOTPRequest()
        val response = getVerifyRedeemOTPResponse()
        redemptionService.mockVerifyRedeemOTP(request, response)
        redemptionRepo.verifyRedeemOTP(request)
        redemptionRepo.observeRedeemOTP().getOrAwaitValue(10, TimeUnit.SECONDS).let {
            assertEquals(
                it.data,
                response
            )
        }
    }

    @Test
    fun verifyRedeemResendOTPTest() {
        val request = getRedeemResendOTPRequest()
        val response = getRedeemResendOTPResponse()
        redemptionService.mockVerifyRedeemResendOTP(request, response)
        redemptionRepo.verifyRedeemResendOTP(request)
        redemptionRepo.observeRedeemResendOTP().getOrAwaitValue(10, TimeUnit.SECONDS).let {
            assertEquals(
                it.data,
                response
            )
        }
    }

    @Test
    fun completeRedemptionTest() {
        val request = getRedeemCompletionRequest()
        val response = getRedeemSuccessResponse()
        redemptionService.mockCompleteRedemption(request, response)
        redemptionRepo.completeRedemption(request)
        redemptionRepo.observeRedeemSuccess().getOrAwaitValue(10, TimeUnit.SECONDS).let {
            assertEquals(
                it.data,
                response
            )
        }
    }

    @Test
    fun redeemPartnersResponseEmitSuccess() {

        assertNotNull(redemptionRepo.redeemPartnersResponse)

        assertNull(redemptionRepo.redeemPartnersResponse.value)

        redemptionRepo.redeemPartnersResponse.value = redeemPartnersResponse

        assertEquals(redemptionRepo.redeemPartnersResponse.value, redeemPartnersResponse)

        assertNotNull(
            redemptionRepo.redeemPartnersResponse.value
        )

    }

    @Test
    fun redeemPartnersResponseEmitFail() {

        assertFalse(redemptionRepo.redeemPartnersResponse.value != null)

        assertNotEquals(redemptionRepo.redeemPartnersResponse.value, redeemPartnersResponse)

    }

    @Test
    fun initializeRedeemResponseEmitSuccess() {

        assertNotNull(redemptionRepo.initializeRedeemResponse)

        assertNull(redemptionRepo.initializeRedeemResponse.value)

        redemptionRepo.initializeRedeemResponse.value = initializeRedeemResponse

        assertEquals(redemptionRepo.initializeRedeemResponse.value, initializeRedeemResponse)

        assertNotNull(
            redemptionRepo.initializeRedeemResponse.value
        )

    }

    @Test
    fun initializeRedeemResponseEmitFail() {

        assertFalse(redemptionRepo.initializeRedeemResponse.value != null)

        assertNotEquals(
            redemptionRepo.initializeRedeemResponse.value,
            initializeRedeemResponse
        )

    }

    @Test
    fun verifyRedeemOTPResponseEmitSuccess() {

        assertNotNull(redemptionRepo.verifyRedeemOTPResponse)

        assertNull(redemptionRepo.verifyRedeemOTPResponse.value)

        redemptionRepo.verifyRedeemOTPResponse.value = verifyRedeemOTPResponse

        assertEquals(
            redemptionRepo.verifyRedeemOTPResponse.value,
            verifyRedeemOTPResponse
        )

        assertNotNull(
            redemptionRepo.verifyRedeemOTPResponse.value
        )

    }

    @Test
    fun verifyRedeemOTPResponseEmitFail() {

        assertFalse(redemptionRepo.verifyRedeemOTPResponse.value != null)

        assertNotEquals(
            redemptionRepo.verifyRedeemOTPResponse.value,
            verifyRedeemOTPResponse
        )

    }

    @Test
    fun verifyRedeemResendOTPResponseEmitSuccess() {

        assertNotNull(redemptionRepo.verifyRedeemResendOTPResponse)

        assertNull(redemptionRepo.verifyRedeemResendOTPResponse.value)

        redemptionRepo.verifyRedeemResendOTPResponse.value = verifyRedeemResendOTPResponse

        assertEquals(
            redemptionRepo.verifyRedeemResendOTPResponse.value,
            verifyRedeemResendOTPResponse
        )

        assertNotNull(
            redemptionRepo.verifyRedeemResendOTPResponse.value
        )

    }

    @Test
    fun verifyRedeemResendOTPResponseEmitFail() {

        assertFalse(redemptionRepo.verifyRedeemResendOTPResponse.value != null)

        assertNotEquals(
            redemptionRepo.verifyRedeemResendOTPResponse.value,
            verifyRedeemResendOTPResponse
        )

    }

    @Test
    fun redeemSuccessResponseEmitSuccess() {

        assertNotNull(redemptionRepo.redeemSuccessResponse)

        assertNull(redemptionRepo.redeemSuccessResponse.value)

        redemptionRepo.redeemSuccessResponse.value = redeemSuccessResponse

        assertEquals(redemptionRepo.redeemSuccessResponse.value, redeemSuccessResponse)

        assertNotNull(
            redemptionRepo.redeemSuccessResponse.value
        )

    }

    @Test
    fun redeemSuccessResponseEmitFail() {

        assertFalse(redemptionRepo.redeemSuccessResponse.value != null)

        assertNotEquals(redemptionRepo.redeemSuccessResponse.value, redeemSuccessResponse)

    }

    @Test
    fun onClearSuccess() {

        assertNull(networkHelper.disposable)

        networkHelper.disposable = disposable

        assertNotNull(networkHelper.disposable)

        assertEquals(disposable, networkHelper.disposable)

        assertTrue(disposable === networkHelper.disposable)

        redemptionRepo.onClear()

        assertNull(networkHelper.disposable)

    }

    @Test
    fun onClearFailed() {
        assertFalse(
            networkHelper.disposable != null
        )

        assertFalse(networkHelper.disposable != null)

        networkHelper.disposable = disposable

        redemptionRepo.onClear()

        assertFalse(networkHelper.disposable != null)

    }

}