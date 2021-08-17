package com.onelink.nrlp.android.repos.uuid

import com.onelink.nrlp.android.base.BaseRepoTest
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.features.uuid.repo.UUIDRepo
import com.onelink.nrlp.android.getOrAwaitValue
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock
import java.util.concurrent.TimeUnit

class UUIDRepoTest : BaseRepoTest() {

    @Mock
    lateinit var updateUUIDResponse: BaseResponse<GeneralMessageResponseModel>

    @Mock
    lateinit var uuidResendOTPResponse: BaseResponse<GeneralMessageResponseModel>

    private lateinit var uuidRepo: UUIDRepo

    override fun setUp() {
        super.setUp()
        uuidRepo = UUIDRepo(networkHelper, serviceGateway)
    }

    @Test
    fun uniqueIdentifierUpdateTest() {
        val request = getUniqueIdentifierUpdateRequest()
        uuidRepo.uniqueIdentifierUpdate(request)
        uuidRepo.getObserverUpdateUUIDResponse().getOrAwaitValue(10, TimeUnit.SECONDS).let {
            assertEquals(it.data, getUniqueIdentifierUpdateResponse().data)
        }
    }

    @Test
    fun updateUUIDResponseEmitSuccess() {

        assertNotNull(uuidRepo.updateUUIDResponse)

        assertNull(uuidRepo.updateUUIDResponse.value)

        uuidRepo.updateUUIDResponse.value = updateUUIDResponse

        assertEquals(uuidRepo.updateUUIDResponse.value, updateUUIDResponse)

        assertNotNull(
            uuidRepo.updateUUIDResponse.value
        )

    }

    @Test
    fun updateUUIDResponseEmitFail() {

        assertFalse(uuidRepo.updateUUIDResponse.value != null)

        assertNotEquals(uuidRepo.updateUUIDResponse.value, updateUUIDResponse)

    }

    @Test
    fun uuidResendOTPResponseEmitSuccess() {

        assertNotNull(uuidRepo.uuidResendOTPResponse)

        assertNull(uuidRepo.uuidResendOTPResponse.value)

        uuidRepo.uuidResendOTPResponse.value = uuidResendOTPResponse

        assertEquals(uuidRepo.uuidResendOTPResponse.value, uuidResendOTPResponse)

        assertNotNull(
            uuidRepo.uuidResendOTPResponse.value
        )

    }

    @Test
    fun uuidResendOTPResponseEmitFail() {

        assertFalse(uuidRepo.uuidResendOTPResponse.value != null)

        assertNotEquals(
            uuidRepo.uuidResendOTPResponse.value,
            uuidResendOTPResponse
        )

    }

    @Test
    fun onClearSuccess() {

        assertNull(networkHelper.disposable)

        networkHelper.disposable = disposable

        assertNotNull(networkHelper.disposable)

        assertEquals(disposable, networkHelper.disposable)

        assertTrue(disposable === networkHelper.disposable)

        uuidRepo.onClear()

        assertNull(networkHelper.disposable)

    }

    @Test
    fun onClearFailed() {
        assertFalse(
            networkHelper.disposable != null
        )

        assertFalse(networkHelper.disposable != null)

        networkHelper.disposable = disposable

        uuidRepo.onClear()

        assertFalse(networkHelper.disposable != null)

    }

}