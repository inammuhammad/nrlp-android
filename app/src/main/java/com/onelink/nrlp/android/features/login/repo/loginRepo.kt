package com.onelink.nrlp.android.features.login.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.data.NetworkHelper
import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.data.local.UserModel
import com.onelink.nrlp.android.features.login.model.LoginRequest
import com.onelink.nrlp.android.features.login.model.LoginResponseModel
import com.onelink.nrlp.android.features.uuid.model.UniqueIdentifierResendOTPRequest
import com.onelink.nrlp.android.features.uuid.model.UniqueIdentifierUpdateRequest
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import javax.inject.Inject

open class LoginRepo @Inject constructor(

    // already mocked and passed into loginRepo object in test let me show u
    private val networkHelper: NetworkHelper,
    private val serviceGateway: ServiceGateway
) {
    val loginResponse = MutableLiveData<BaseResponse<LoginResponseModel>>()
    val updateUUIDResponse = MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()
    val uuidResendOTPResponse = MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()

    fun login(loginRequest: LoginRequest) {
        networkHelper.serviceCall(serviceGateway.login(loginRequest)).observeForever {
            it.data?.let { loginResponseModel ->
                persistUser(loginResponseModel)
            }
            loginResponse.value = it
        }
    }

    fun persistUser(loginResponseModel: LoginResponseModel) {
        UserData.setUser(getUserModelFromLoginResponse(loginResponseModel))
    }

    fun observeLogin() = loginResponse as LiveData<BaseResponse<LoginResponseModel>>

    fun getUserModelFromLoginResponse(loginResponseModel: LoginResponseModel): UserModel {
        return UserModel(
            token = loginResponseModel.token,
            id = loginResponseModel.loginModel.id,
            fullName = loginResponseModel.loginModel.fullName,
            cnicNicop = loginResponseModel.loginModel.cnicNicop,
            mobileNo = loginResponseModel.loginModel.mobileNo,
            email = loginResponseModel.loginModel.email,
            accountType = loginResponseModel.loginModel.userType,
            loyaltyLevel = loginResponseModel.loginModel.loyaltyLevel,
            loyaltyPoints = loginResponseModel.loginModel.loyaltyPoints,
            sessionKey =  loginResponseModel.sessionKey,
            inActiveTime = loginResponseModel.inActivityTime.toLong(),
            expiresIn = loginResponseModel.expiresIn.toLong()
        )
    }


    fun uniqueIdentifierUpdate(uniqueIdentifierUpdateRequest: UniqueIdentifierUpdateRequest) {
        networkHelper.serviceCall(
            serviceGateway.uniqueIdentifierUpdate(
                uniqueIdentifierUpdateRequest
            )
        ).observeForever {
            updateUUIDResponse.value = it
        }
    }

    fun observeUpdateUUIDResponse() =
        updateUUIDResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun verifyUUIDResendOTP(uniqueIdentifierResendOTPRequest: UniqueIdentifierResendOTPRequest) {
        networkHelper.serviceCall(
            serviceGateway.uniqueIdentifierResendOTP(uniqueIdentifierResendOTPRequest)
        ).observeForever {
            uuidResendOTPResponse.value = it
        }
    }

    fun observeUUIDResendOTP() = uuidResendOTPResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun onClear() {
        networkHelper.dispose()
        networkHelper.disposable = null
    }
}
