package com.onelink.nrlp.android.repos.changepassword

import com.onelink.nrlp.android.base.BaseRepoTest
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.features.changePassword.repo.ChangePasswordRepo
import com.onelink.nrlp.android.getOrAwaitValue
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock
import java.util.concurrent.TimeUnit

class ChangePasswordRepoTest : BaseRepoTest() {

    @Mock
    lateinit var changePasswordResponse: BaseResponse<GeneralMessageResponseModel>

    private lateinit var changePasswordRepo: ChangePasswordRepo

    private lateinit var changePasswordService: ChangePasswordService

    override fun setUp() {
        super.setUp()
        changePasswordService = ChangePasswordService()
        changePasswordRepo = ChangePasswordRepo(networkHelper, changePasswordService.serviceGateway)
    }

    @Test
    fun changePasswordTest() {
        val request = getChanegPasswordRequest()
        val response = getChangePasswordResponse()
        changePasswordService.mockChangePassword(request, response)
        changePasswordRepo.changePassword(request)
        changePasswordRepo.observeChangePassword().getOrAwaitValue(10, TimeUnit.SECONDS).let {
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

        changePasswordRepo.onClear()

        assertNull(networkHelper.disposable)

    }


    @Test
    fun onClearFailed() {
        assertFalse(
            networkHelper.disposable != null
        )

        assertFalse(networkHelper.disposable != null)

        networkHelper.disposable = disposable

        changePasswordRepo.onClear()

        assertFalse(networkHelper.disposable != null)

    }

    @Test
    fun changePasswordResponseEmitSuccess() {

        assertNotNull(changePasswordRepo.changePasswordResponse)

        assertNull(changePasswordRepo.changePasswordResponse.value)

        changePasswordRepo.changePasswordResponse.value = changePasswordResponse

        assertEquals(changePasswordRepo.changePasswordResponse.value, changePasswordResponse)

        assertNotNull(
            changePasswordRepo.changePasswordResponse.value
        )

    }

    @Test
    fun changePasswordResponseEmitFail() {

        assertFalse(changePasswordRepo.changePasswordResponse.value != null)

        assertNotEquals(changePasswordRepo.changePasswordResponse.value, changePasswordResponse)

    }

}