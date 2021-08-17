package com.onelink.nrlp.android.repos.forgotpassword

import com.onelink.nrlp.android.base.BaseRepoTest
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.features.forgotPassword.repo.ForgotPasswordRepo
import com.onelink.nrlp.android.getOrAwaitValue
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock
import java.util.concurrent.TimeUnit

class ForgotPasswordRepoTest : BaseRepoTest() {

    @Mock
    lateinit var forgotPasswordResponse: BaseResponse<GeneralMessageResponseModel>

    @Mock
    lateinit var forgotPasswordOTPResponse: BaseResponse<GeneralMessageResponseModel>

    @Mock
    lateinit var forgotPasswordResendOTPResponse: BaseResponse<GeneralMessageResponseModel>

    @Mock
    lateinit var updatePasswordResponse: BaseResponse<GeneralMessageResponseModel>

    private lateinit var forgotPasswordRepo: ForgotPasswordRepo

    private lateinit var forgotPasswordService: ForgotPasswordService

    override fun setUp() {
        super.setUp()
        forgotPasswordService = ForgotPasswordService()
        forgotPasswordRepo = ForgotPasswordRepo(networkHelper, forgotPasswordService.serviceGateway)
    }

    @Test
    fun forgotPasswordTest() {
        val request = getForgotPasswordRequest()
        val response = getForgotPasswordResponse()
        forgotPasswordService.mockForgotPassword(request, response)
        forgotPasswordRepo.forgotPassword(request)
        forgotPasswordRepo.observeForgotPasswordResponse().getOrAwaitValue(10, TimeUnit.SECONDS)
            .let {
                assertEquals(it.data, response)
            }
    }


    @Test
    fun forgotPasswordOTPTest() {
        val request = getForgotPasswordOTPRequestModel()
        val response = getForgotPasswordOTPResponse()
        forgotPasswordService.mockForgotPasswordOTP(request, response)
        forgotPasswordRepo.forgotPasswordOTP(request)
        forgotPasswordRepo.observeForgotPasswordOTPResponse().getOrAwaitValue(10, TimeUnit.SECONDS)
            .let {
                assertEquals(it.data, response)
            }
    }

    @Test
    fun forgotPasswordResendOTPTest() {
        val request = getForgotPasswordRequest()
        val response = getForgotPasswordResponse()
        forgotPasswordService.mockForgotPasswordResendOTP(request, response)
        forgotPasswordRepo.forgotPasswordResendOTP(request)
        forgotPasswordRepo.observeForgotPasswordResendOTPResponse()
            .getOrAwaitValue(10, TimeUnit.SECONDS)
            .let {
                assertEquals(it.data, response)
            }
    }

    @Test
    fun updatePasswordTest() {
        val request = getUpdatePasswordRequestModel()
        val response = getUpdatePasswordResponse()
        forgotPasswordService.mockUpdatePassword(request, response)
        forgotPasswordRepo.updatePassword(request)
        forgotPasswordRepo.observeUpdateForgotPasswordResponse()
            .getOrAwaitValue(10, TimeUnit.SECONDS)
            .let {
                assertEquals(it.data, response)
            }
    }

    @Test
    fun forgotPasswordResponseEmitSuccess() {

        assertNotNull(forgotPasswordRepo.forgotPasswordResponse)

        assertNull(forgotPasswordRepo.forgotPasswordResponse.value)

        forgotPasswordRepo.forgotPasswordResponse.value = forgotPasswordResponse

        assertEquals(forgotPasswordRepo.forgotPasswordResponse.value, forgotPasswordResponse)

        assertNotNull(
            forgotPasswordRepo.forgotPasswordResponse.value
        )

    }

    @Test
    fun forgotPasswordResponseEmitFail() {

        assertFalse(forgotPasswordRepo.forgotPasswordResponse.value != null)

        assertNotEquals(forgotPasswordRepo.forgotPasswordResponse.value, forgotPasswordResponse)

    }

    @Test
    fun forgotPasswordOTPResponseEmitSuccess() {

        assertNotNull(forgotPasswordRepo.forgotPasswordOTPResponse)

        assertNull(forgotPasswordRepo.forgotPasswordOTPResponse.value)

        forgotPasswordRepo.forgotPasswordOTPResponse.value = forgotPasswordOTPResponse

        assertEquals(forgotPasswordRepo.forgotPasswordOTPResponse.value, forgotPasswordOTPResponse)

        assertNotNull(
            forgotPasswordRepo.forgotPasswordOTPResponse.value
        )

    }

    @Test
    fun forgotPasswordOTPResponseEmitFail() {

        assertFalse(forgotPasswordRepo.forgotPasswordOTPResponse.value != null)

        assertNotEquals(
            forgotPasswordRepo.forgotPasswordOTPResponse.value,
            forgotPasswordOTPResponse
        )

    }

    @Test
    fun forgotPasswordResendOTPResponseEmitSuccess() {

        assertNotNull(forgotPasswordRepo.forgotPasswordResendOTPResponse)

        assertNull(forgotPasswordRepo.forgotPasswordResendOTPResponse.value)

        forgotPasswordRepo.forgotPasswordResendOTPResponse.value = forgotPasswordResendOTPResponse

        assertEquals(
            forgotPasswordRepo.forgotPasswordResendOTPResponse.value,
            forgotPasswordResendOTPResponse
        )

        assertNotNull(
            forgotPasswordRepo.forgotPasswordResendOTPResponse.value
        )

    }

    @Test
    fun forgotPasswordResendOTPResponseEmitFail() {

        assertFalse(forgotPasswordRepo.forgotPasswordResendOTPResponse.value != null)

        assertNotEquals(
            forgotPasswordRepo.forgotPasswordResendOTPResponse.value,
            forgotPasswordResendOTPResponse
        )

    }


    @Test
    fun updatePasswordResponseEmitSuccess() {

        assertNotNull(forgotPasswordRepo.updatePasswordResponse)

        assertNull(forgotPasswordRepo.updatePasswordResponse.value)

        forgotPasswordRepo.updatePasswordResponse.value = updatePasswordResponse

        assertEquals(forgotPasswordRepo.updatePasswordResponse.value, updatePasswordResponse)

        assertNotNull(
            forgotPasswordRepo.updatePasswordResponse.value
        )

    }

    @Test
    fun updatePasswordResponseEmitFail() {

        assertFalse(forgotPasswordRepo.updatePasswordResponse.value != null)

        assertNotEquals(forgotPasswordRepo.updatePasswordResponse.value, updatePasswordResponse)

    }
}