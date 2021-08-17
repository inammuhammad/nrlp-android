package com.onelink.nrlp.android.repos.home

import com.onelink.nrlp.android.base.BaseRepoTest
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.features.home.model.UserProfileResponseModel
import com.onelink.nrlp.android.features.home.repo.HomeRepo
import com.onelink.nrlp.android.getOrAwaitValue
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock
import java.util.concurrent.TimeUnit

class HomeRepoTest : BaseRepoTest() {

    @Mock
    lateinit var profileResponse: BaseResponse<UserProfileResponseModel>

    @Mock
    lateinit var logoutResponse: BaseResponse<GeneralMessageResponseModel>

    private lateinit var homeRepo: HomeRepo

    private lateinit var homeService: HomeService

    override fun setUp() {
        super.setUp()
        homeService = HomeService()
        homeRepo = HomeRepo(networkHelper, homeService.serviceGateway)
    }

    @Test
    fun getUserProfileTest() {
        val response = getUserProfileResponse()
        homeService.mockGetUserProfile(response)
        homeRepo.getUserProfile()
        homeRepo.observeUserProfile().getOrAwaitValue(10, TimeUnit.SECONDS).let {
            assertEquals(
                it.data,
                response
            )
        }
    }

    @Test
    fun performLogoutTest() {
        val response = getPerformLogoutResponse()
        homeService.mockPerformLogout(response)
        homeRepo.performLogout()
        homeRepo.observeLogoutResponse().getOrAwaitValue(10, TimeUnit.SECONDS).let {
            assertEquals(
                it.data,
                response
            )
        }
    }

    @Test
    fun profileResponseEmitSuccess() {

        assertNotNull(homeRepo.profileResponse)

        assertNull(homeRepo.profileResponse.value)

        homeRepo.profileResponse.value = profileResponse

        assertEquals(homeRepo.profileResponse.value, profileResponse)

        assertNotNull(
            homeRepo.profileResponse.value
        )

    }

    @Test
    fun profileResponseEmitFail() {

        assertFalse(homeRepo.profileResponse.value != null)

        assertNotEquals(homeRepo.profileResponse.value, profileResponse)

    }

    @Test
    fun logoutResponseEmitSuccess() {

        assertNotNull(homeRepo.logoutResponse)

        assertNull(homeRepo.logoutResponse.value)

        homeRepo.logoutResponse.value = logoutResponse

        assertEquals(homeRepo.logoutResponse.value, logoutResponse)

        assertNotNull(
            homeRepo.logoutResponse.value
        )

    }

    @Test
    fun logoutResponseEmitFail() {

        assertFalse(homeRepo.logoutResponse.value != null)

        assertNotEquals(
            homeRepo.logoutResponse.value,
            logoutResponse
        )

    }

    @Test
    fun onClearSuccess() {

        assertNull(networkHelper.disposable)

        networkHelper.disposable = disposable

        assertNotNull(networkHelper.disposable)

        assertEquals(disposable, networkHelper.disposable)

        assertTrue(disposable === networkHelper.disposable)

        homeRepo.onClear()

        assertNull(networkHelper.disposable)

    }

    @Test
    fun onClearFailed() {
        assertFalse(
            networkHelper.disposable != null
        )

        assertFalse(networkHelper.disposable != null)

        networkHelper.disposable = disposable

        homeRepo.onClear()

        assertFalse(networkHelper.disposable != null)

    }

}