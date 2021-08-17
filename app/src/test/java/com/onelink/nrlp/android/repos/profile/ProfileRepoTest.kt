package com.onelink.nrlp.android.repos.profile

import com.google.gson.JsonObject
import com.onelink.nrlp.android.base.BaseRepoTest
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.features.profile.repo.ProfileRepo
import com.onelink.nrlp.android.features.profile.repo.UpdateProfileConstants
import com.onelink.nrlp.android.features.select.country.model.CountryCodeResponseModel
import com.onelink.nrlp.android.getOrAwaitValue
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock
import java.util.concurrent.TimeUnit

class ProfileRepoTest : BaseRepoTest() {

    @Mock
    lateinit var verifyResendOTPResponse: BaseResponse<GeneralMessageResponseModel>

    @Mock
    lateinit var countryCodesResponse: BaseResponse<CountryCodeResponseModel>

    @Mock
    lateinit var updateProfileResponse: BaseResponse<GeneralMessageResponseModel>

    @Mock
    lateinit var updateProfileOtpResponse: BaseResponse<GeneralMessageResponseModel>

    private lateinit var updateProfileRequestModel: JsonObject

    private lateinit var profileRepo: ProfileRepo

    private lateinit var profileService: ProfileService

    override fun setUp() {
        super.setUp()
        profileService = ProfileService()
        profileRepo = ProfileRepo(networkHelper, profileService.serviceGateway)

        updateProfileRequestModel = JsonObject()
        updateProfileRequestModel.addProperty(UpdateProfileConstants.EMAIL, "test@gmail.com")
        updateProfileRequestModel.addProperty(UpdateProfileConstants.MOBILE_NO, "+921231234567")
    }

    @Test
    fun getCountryCodesTest() {
        val response = getCountryCodeResponse()
        profileService.mockGetCountryCodes(response)
        profileRepo.getCountryCodes()
        profileRepo.observeCountryCodes().getOrAwaitValue(10, TimeUnit.SECONDS).let {
            assertEquals(it.data, response)
        }
    }

    @Test
    fun updateProfileTest() {
        val request = getJsonObject()
        val response = updateProfileResponse()
        profileService.mockUpdateProfile(request, response)
        profileRepo.updateProfile(request)
        profileRepo.observeUpdateProfile().getOrAwaitValue(10, TimeUnit.SECONDS).let {
            assertEquals(it.data, response)
        }
    }


    @Test
    fun updateProfileVerifyOTPTest() {
        val request = getJsonObject()
        val response = verifyOTPResponse()
        profileService.mockVerifyOtp(request, response)
        profileRepo.updateProfileVerifyOtp(request)
        profileRepo.observeUpdateProfileOtp().getOrAwaitValue(10, TimeUnit.SECONDS).let {
            assertEquals(it.data, response)
        }
    }

    @Test
    fun verifyResendOtp() {
        val request = getJsonObject()
        val response = verifyResendOTPResponse()
        profileService.mockVerifyResendOtp(request, response)
        profileRepo.verifyResendOTP(request)
        profileRepo.observeResendOTP().getOrAwaitValue(10, TimeUnit.SECONDS).let {
            assertEquals(it.data, response)
        }
    }

    @Test
    fun verifyResendOTPResponseEmitSuccess() {

        assertNotNull(profileRepo.verifyResendOTPResponse)

        assertNull(profileRepo.verifyResendOTPResponse.value)

        profileRepo.verifyResendOTPResponse.value = verifyResendOTPResponse

        assertEquals(profileRepo.verifyResendOTPResponse.value, verifyResendOTPResponse)

        assertNotNull(
            profileRepo.verifyResendOTPResponse.value
        )

    }

    @Test
    fun verifyResendOTPResponseEmitFail() {

        assertFalse(profileRepo.verifyResendOTPResponse.value != null)

        assertNotEquals(
            profileRepo.verifyResendOTPResponse.value,
            verifyResendOTPResponse
        )

    }

    @Test
    fun countryCodesResponseEmitSuccess() {

        assertNotNull(profileRepo.countryCodesResponse)

        assertNull(profileRepo.countryCodesResponse.value)

        profileRepo.countryCodesResponse.value = countryCodesResponse

        assertEquals(
            profileRepo.countryCodesResponse.value,
            countryCodesResponse
        )

        assertNotNull(
            profileRepo.countryCodesResponse.value
        )

    }

    @Test
    fun countryCodesResponseEmitFail() {

        assertFalse(profileRepo.countryCodesResponse.value != null)

        assertNotEquals(
            profileRepo.countryCodesResponse.value,
            countryCodesResponse
        )

    }

    @Test
    fun updateProfileResponseEmitSuccess() {

        assertNotNull(profileRepo.updateProfileResponse)

        assertNull(profileRepo.updateProfileResponse.value)

        profileRepo.updateProfileResponse.value = updateProfileResponse

        assertEquals(profileRepo.updateProfileResponse.value, updateProfileResponse)

        assertNotNull(
            profileRepo.updateProfileResponse.value
        )

    }

    @Test
    fun updateProfileResponseEmitFail() {

        assertFalse(profileRepo.updateProfileResponse.value != null)

        assertNotEquals(profileRepo.updateProfileResponse.value, updateProfileResponse)

    }

    @Test
    fun updateProfileOtpResponseEmitSuccess() {

        assertNotNull(profileRepo.updateProfileOtpResponse)

        assertNull(profileRepo.updateProfileOtpResponse.value)

        profileRepo.updateProfileOtpResponse.value = updateProfileOtpResponse

        assertEquals(profileRepo.updateProfileOtpResponse.value, updateProfileOtpResponse)

        assertNotNull(
            profileRepo.updateProfileOtpResponse.value
        )

    }

    @Test
    fun updateProfileOtpResponseEmitFail() {

        assertFalse(profileRepo.updateProfileOtpResponse.value != null)

        assertNotEquals(profileRepo.updateProfileOtpResponse.value, updateProfileOtpResponse)

    }

    @Test
    fun onClearSuccess() {

        assertNull(networkHelper.disposable)

        networkHelper.disposable = disposable

        assertNotNull(networkHelper.disposable)

        assertEquals(disposable, networkHelper.disposable)

        assertTrue(disposable === networkHelper.disposable)

        profileRepo.onClear()

        assertNull(networkHelper.disposable)

    }

    @Test
    fun onClearFailed() {
        assertFalse(
            networkHelper.disposable != null
        )

        assertFalse(networkHelper.disposable != null)

        networkHelper.disposable = disposable

        profileRepo.onClear()

        assertFalse(networkHelper.disposable != null)

    }

}