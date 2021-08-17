package com.onelink.nrlp.android.repos.login

import com.onelink.nrlp.android.base.BaseRepoTest
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.data.local.UserModel
import com.onelink.nrlp.android.features.login.model.LoginModel
import com.onelink.nrlp.android.features.login.model.LoginResponseModel
import com.onelink.nrlp.android.features.login.repo.LoginRepo
import com.onelink.nrlp.android.getOrAwaitValue
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock
import java.util.concurrent.TimeUnit

class LoginRepoTest : BaseRepoTest() {

    @Mock
    lateinit var updateUUIDResponse: BaseResponse<GeneralMessageResponseModel>

    @Mock
    lateinit var uuidResendOTPResponse: BaseResponse<GeneralMessageResponseModel>

    private lateinit var loginRepo: LoginRepo

    private lateinit var userModel: UserModel

    private lateinit var anOtherUserModel: UserModel

    private lateinit var loginService: LoginService

    override fun setUp() {
        super.setUp()

        loginService = LoginService()

        loginRepo = LoginRepo(networkHelper, loginService.serviceGateway)

        userModel = getDefaultUserModel()

        anOtherUserModel = getAnotherUserModel()
    }

    @Test
    fun loginTest() {
        //mock default data
        val loginRequest = getDefaultLoginRequest()
        val modelResponse = getDefaultLoginResponseModel(getDefaultLoginModel())
        loginService.mockLogin(loginRequest, modelResponse)
        loginRepo.login(loginRequest)
        val result = loginRepo.observeLogin().getOrAwaitValue(10, timeUnit = TimeUnit.SECONDS)
        assertEquals(result.data?.loginModel?.id, modelResponse.loginModel.id)
    }

    @Test
    fun uniqueIdentifierUpdateTest() {
        val request = getUniqueIdentifierUpdateRequest()
        val response = getUniqueIdentifierUpdateResponse()
        loginService.mockUniqueIdentifierUpdate(request, response)
        loginRepo.uniqueIdentifierUpdate(request)
        val result = loginRepo.observeUpdateUUIDResponse().getOrAwaitValue(10, TimeUnit.SECONDS)
        assertEquals(result.data?.message, response.message)
    }

    @Test
    fun verifyUUIDResendOTPTest() {
        val request = getUniqueIdentifierResendOTPRequest()
        val response = getUuidResendOTPResponse()
        loginService.mockVerifyUUIDResendOTP(request, response)
        loginRepo.verifyUUIDResendOTP(request)
        val result = loginRepo.observeUUIDResendOTP().getOrAwaitValue(10, TimeUnit.SECONDS)
        assertEquals(result.data?.message, response.message)
    }

    @Test
    fun loginResponseToUserModelSuccess() = assertEquals(
        userModel,
        loginRepo.getUserModelFromLoginResponse(getDefaultLoginResponseModel(getDefaultLoginModel()))
    )

    @Test
    fun loginResponseToUserModelFailed() = assertNotEquals(
        userModel,
        loginRepo.getUserModelFromLoginResponse(
            LoginResponseModel(
                "",
                "",
                LoginModel.emptyObj(),
                "0",
                "0",
                ""
            )


        )
    )

    @Test
    fun loginResponseToUserModelNotNull() = assertNotNull(
        loginRepo.getUserModelFromLoginResponse(getDefaultLoginResponseModel(getDefaultLoginModel()))
    )

    @Test
    fun persistUserSuccess() {

        loginRepo.persistUser(getDefaultLoginResponseModel(getDefaultLoginModel()))
        assertNotNull(
            UserData.getUser()
        )
        assertEquals(
            userModel,
            UserData.getUser()
        )

        assertTrue(
            userModel == UserData.getUser()
        )

        assertFalse(
            userModel === UserData.getUser()
        )
    }

    @Test
    fun persisUserFailed() {
        loginRepo.persistUser(getDefaultLoginResponseModel(getDefaultLoginModel()))
        assertNotEquals(
            null,
            UserData.getUser()
        )

        assertNotEquals(
            anOtherUserModel,
            UserData.getUser()
        )

        assertTrue(
            anOtherUserModel !== UserData.getUser()
        )
    }

    @Test
    fun onClearSuccess() {

        assertNull(networkHelper.disposable)

        networkHelper.disposable = disposable

        assertNotNull(networkHelper.disposable)

        assertEquals(disposable, networkHelper.disposable)

        assertTrue(disposable === networkHelper.disposable)

        loginRepo.onClear()

        assertNull(networkHelper.disposable)

    }

    @Test
    fun onClearFailed() {
        assertFalse(
            networkHelper.disposable != null
        )

        assertFalse(networkHelper.disposable != null)

        networkHelper.disposable = disposable

        loginRepo.onClear()

        assertFalse(networkHelper.disposable != null)

    }

    @Test
    fun loginResponseEmitSuccess() {

        assertNotNull(loginRepo.loginResponse)

        assertNull(loginRepo.loginResponse.value)
        val loginResponse =
            getDefaultLoginResponse(getDefaultLoginResponseModel(getDefaultLoginModel()))
        loginRepo.loginResponse.value = loginResponse

        assertEquals(loginRepo.loginResponse.value, loginResponse)

        assertNotNull(
            loginRepo.loginResponse.value
        )

    }

    @Test
    fun loginResponseEmitFail() {

        assertFalse(loginRepo.loginResponse.value != null)

        assertNotEquals(
            loginRepo.loginResponse.value,
            getDefaultLoginResponse(getDefaultLoginResponseModel(getDefaultLoginModel()))
        )

    }

    @Test
    fun updateUUIDResponseEmitSuccess() {

        assertNotNull(loginRepo.updateUUIDResponse)

        assertNull(loginRepo.updateUUIDResponse.value)

        loginRepo.updateUUIDResponse.value = updateUUIDResponse

        assertEquals(loginRepo.updateUUIDResponse.value, updateUUIDResponse)

        assertNotNull(
            loginRepo.updateUUIDResponse.value
        )

    }

    @Test
    fun updateUUIDResponseEmitFail() {

        assertFalse(loginRepo.updateUUIDResponse.value != null)

        assertNotEquals(loginRepo.updateUUIDResponse.value, updateUUIDResponse)

    }

    @Test
    fun uuidResendOTPResponseEmitSuccess() {

        assertNotNull(loginRepo.uuidResendOTPResponse)

        assertNull(loginRepo.uuidResendOTPResponse.value)

        loginRepo.uuidResendOTPResponse.value = uuidResendOTPResponse

        assertEquals(loginRepo.uuidResendOTPResponse.value, uuidResendOTPResponse)

        assertNotNull(
            loginRepo.uuidResendOTPResponse.value
        )

    }

    @Test
    fun uuidResendOTPResponseEmitFail() {

        assertFalse(loginRepo.updateUUIDResponse.value != null)

        assertNotEquals(loginRepo.updateUUIDResponse.value, updateUUIDResponse)

    }
}