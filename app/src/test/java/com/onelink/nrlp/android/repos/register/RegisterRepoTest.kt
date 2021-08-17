package com.onelink.nrlp.android.repos.register

import com.onelink.nrlp.android.base.BaseRepoTest
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.features.register.models.TermsAndConditionsResponseModel
import com.onelink.nrlp.android.features.register.registerRepo.RegisterRepo
import com.onelink.nrlp.android.getOrAwaitValue
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock
import java.util.concurrent.TimeUnit

class RegisterRepoTest : BaseRepoTest() {

    @Mock
    lateinit var verifyReferenceNumResponse: BaseResponse<GeneralMessageResponseModel>

    @Mock
    lateinit var verifyRegistrationCodeResponse: BaseResponse<GeneralMessageResponseModel>

    @Mock
    lateinit var verifyOTPResponse: BaseResponse<GeneralMessageResponseModel>

    @Mock
    lateinit var resendOTPResponse: BaseResponse<GeneralMessageResponseModel>

    @Mock
    lateinit var termsAndConditionsResponse: BaseResponse<TermsAndConditionsResponseModel>

    @Mock
    lateinit var registerUserResponse: BaseResponse<GeneralMessageResponseModel>

    private lateinit var registerRepo: RegisterRepo

    private lateinit var registerService: RegisterService

    override fun setUp() {
        super.setUp()
        registerService = RegisterService()
        registerRepo = RegisterRepo(networkHelper, registerService.serviceGateway)
    }

    @Test
    fun verifyReferenceNumberTest() {
        val request = getVerifyReferenceNumberRequest()
        val response = getVerifyReferenceNumberResponse()
        registerService.mockVerifyReferenceNumber(request, response)
        registerRepo.verifyReferenceNumber(request)
        registerRepo.observeVerifyReferenceNumber().getOrAwaitValue(10, TimeUnit.SECONDS).let {
            assertEquals(it.data, response)
        }
    }

    @Test
    fun verifyRegistrationCodeTest() {
        val request = getVerifyRegistrationCodeRequest()
        val response = getVerifyRegistrationCodeResponse()
        registerService.mockVerifyRegistrationCode(request, response)
        registerRepo.verifyRegistrationCode(request)
        registerRepo.observeVerifyRegistrationCode().getOrAwaitValue(10, TimeUnit.SECONDS).let {
            assertEquals(it.data, response)
        }
    }

    @Test
    fun verifyOTPTest() {
        val request = getVerifyOTPRequest()
        val response = getVerifyOTPResponse()
        registerService.mockVerifyOTP(request, response)
        registerRepo.verifyOTP(request)
        registerRepo.observeVerifyOTP().getOrAwaitValue(10, TimeUnit.SECONDS).let {
            assertEquals(it.data, response)
        }
    }

    @Test
    fun resendOTPTest() {
        val request = getResendOTPRequest()
        val response = getResendOTPResponse()
        registerService.mockResendOTP(request, response)
        registerRepo.resendOTP(request)
        registerRepo.observeResendOTP().getOrAwaitValue(10, TimeUnit.SECONDS).let {
            assertEquals(it.data, response)
        }
    }

    @Test
    fun getTermsAndConditionsTest() {
        val response = getTermsAndConditionsResponseModel()
        registerService.mockGetTermsAndConditions(response)
        registerRepo.getTermsAndConditions()
        registerRepo.observeTermsAndConditions().getOrAwaitValue(10, TimeUnit.SECONDS).let {
            assertEquals(it.data, response)
        }
    }


    @Test
    fun registerRemitterTest() {
        val request = getRegisterRemitterRequest()
        val response = getRegisterRemitterResponse()
        registerService.mockRegisterRemitter(request, response)
        registerRepo.registerRemitter(request)
        registerRepo.observeRegisterUser().getOrAwaitValue(10, TimeUnit.SECONDS).let {
            assertEquals(it.data, response)
        }
    }

    @Test
    fun registerBeneficiaryTest() {
        val request = getRegisterBeneficiaryRequest()
        val response = getRegisterBeneficiaryResponse()
        registerService.mockRegisterBeneficiary(request, response)
        registerRepo.registerBeneficiary(request)
        registerRepo.observeRegisterUser().getOrAwaitValue(10, TimeUnit.SECONDS).let {
            assertEquals(it.data, response)
        }
    }

    @Test
    fun verifyReferenceNumResponseEmitSuccess() {

        assertNotNull(registerRepo.verifyReferenceNumResponse)

        assertNull(registerRepo.verifyReferenceNumResponse.value)

        registerRepo.verifyReferenceNumResponse.value = verifyReferenceNumResponse

        assertEquals(registerRepo.verifyReferenceNumResponse.value, verifyReferenceNumResponse)

        assertNotNull(
            registerRepo.verifyReferenceNumResponse.value
        )

    }

    @Test
    fun verifyReferenceNumResponseEmitFail() {

        assertFalse(registerRepo.verifyReferenceNumResponse.value != null)

        assertNotEquals(registerRepo.verifyReferenceNumResponse.value, verifyReferenceNumResponse)

    }

    @Test
    fun verifyRegistrationCodeResponseEmitSuccess() {

        assertNotNull(registerRepo.verifyRegistrationCodeResponse)

        assertNull(registerRepo.verifyRegistrationCodeResponse.value)

        registerRepo.verifyRegistrationCodeResponse.value = verifyRegistrationCodeResponse

        assertEquals(
            registerRepo.verifyRegistrationCodeResponse.value,
            verifyRegistrationCodeResponse
        )

        assertNotNull(
            registerRepo.verifyRegistrationCodeResponse.value
        )

    }

    @Test
    fun verifyRegistrationCodeResponseEmitFail() {

        assertFalse(registerRepo.verifyRegistrationCodeResponse.value != null)

        assertNotEquals(
            registerRepo.verifyRegistrationCodeResponse.value,
            verifyRegistrationCodeResponse
        )

    }

    @Test
    fun verifyOTPResponseEmitSuccess() {

        assertNotNull(registerRepo.verifyOTPResponse)

        assertNull(registerRepo.verifyOTPResponse.value)

        registerRepo.verifyOTPResponse.value = verifyOTPResponse

        assertEquals(
            registerRepo.verifyOTPResponse.value,
            verifyOTPResponse
        )

        assertNotNull(
            registerRepo.verifyOTPResponse.value
        )

    }

    @Test
    fun verifyOTPResponseEmitFail() {

        assertFalse(registerRepo.verifyOTPResponse.value != null)

        assertNotEquals(
            registerRepo.verifyOTPResponse.value,
            verifyOTPResponse
        )

    }

    @Test
    fun resendOTPResponseEmitSuccess() {

        assertNotNull(registerRepo.resendOTPResponse)

        assertNull(registerRepo.resendOTPResponse.value)

        registerRepo.resendOTPResponse.value = resendOTPResponse

        assertEquals(
            registerRepo.resendOTPResponse.value,
            resendOTPResponse
        )

        assertNotNull(
            registerRepo.resendOTPResponse.value
        )

    }

    @Test
    fun resendOTPResponseEmitFail() {

        assertFalse(registerRepo.resendOTPResponse.value != null)

        assertNotEquals(
            registerRepo.resendOTPResponse.value,
            resendOTPResponse
        )

    }

    @Test
    fun termsAndConditionsResponseEmitSuccess() {

        assertNotNull(registerRepo.termsAndConditionsResponse)

        assertNull(registerRepo.termsAndConditionsResponse.value)

        registerRepo.termsAndConditionsResponse.value = termsAndConditionsResponse

        assertEquals(registerRepo.termsAndConditionsResponse.value, termsAndConditionsResponse)

        assertNotNull(
            registerRepo.termsAndConditionsResponse.value
        )

    }

    @Test
    fun termsAndConditionsResponseEmitFail() {

        assertFalse(registerRepo.termsAndConditionsResponse.value != null)

        assertNotEquals(registerRepo.termsAndConditionsResponse.value, termsAndConditionsResponse)

    }

    @Test
    fun registerUserResponseEmitSuccess() {

        assertNotNull(registerRepo.registerUserResponse)

        assertNull(registerRepo.registerUserResponse.value)

        registerRepo.registerUserResponse.value = registerUserResponse

        assertEquals(registerRepo.registerUserResponse.value, registerUserResponse)

        assertNotNull(
            registerRepo.registerUserResponse.value
        )

    }

    @Test
    fun registerUserResponseEmitFail() {

        assertFalse(registerRepo.registerUserResponse.value != null)

        assertNotEquals(registerRepo.registerUserResponse.value, registerUserResponse)

    }

    @Test
    fun onClearSuccess() {

        assertNull(networkHelper.disposable)

        networkHelper.disposable = disposable

        assertNotNull(networkHelper.disposable)

        assertEquals(disposable, networkHelper.disposable)

        assertTrue(disposable === networkHelper.disposable)

        registerRepo.onClear()

        assertNull(networkHelper.disposable)

    }

    @Test
    fun onClearFailed() {
        assertFalse(
            networkHelper.disposable != null
        )

        assertFalse(networkHelper.disposable != null)

        networkHelper.disposable = disposable

        registerRepo.onClear()

        assertFalse(networkHelper.disposable != null)

    }
}