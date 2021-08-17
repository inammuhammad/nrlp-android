package com.onelink.nrlp.android.features.forgotPassword.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.data.NetworkHelper
import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.forgotPassword.models.ForgotPasswordOTPRequestModel
import com.onelink.nrlp.android.features.forgotPassword.models.ForgotPasswordRequestModel
import com.onelink.nrlp.android.features.forgotPassword.models.UpdatePasswordRequestModel
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import com.onelink.nrlp.android.utils.mocks.MockedAPIResponseModels
import javax.inject.Inject


open class ForgotPasswordRepo @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val serviceGateway: ServiceGateway
) {
    val forgotPasswordResponse =
        MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()
    val forgotPasswordOTPResponse =
        MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()
    val forgotPasswordResendOTPResponse =
        MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()
    val updatePasswordResponse =
        MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()

    fun forgotPassword(forgotPasswordRequestModel: ForgotPasswordRequestModel) {
        networkHelper.serviceCall(serviceGateway.forgotPassword(forgotPasswordRequestModel))
            .observeForever {
                forgotPasswordResponse.value = it
            }
    }

    fun observeForgotPasswordResponse() =
        forgotPasswordResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun forgotPasswordOTP(forgotPasswordOTPRequestModel: ForgotPasswordOTPRequestModel) {
        networkHelper.serviceCall(serviceGateway.forgotPasswordOTP(forgotPasswordOTPRequestModel))
            .observeForever {
                forgotPasswordOTPResponse.value = it
            }
    }

    fun observeForgotPasswordOTPResponse() =
        forgotPasswordOTPResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun forgotPasswordResendOTP(forgotPasswordRequestModel: ForgotPasswordRequestModel) {
        networkHelper.serviceCall(serviceGateway.forgotPasswordResendOTP(forgotPasswordRequestModel))
            .observeForever {
                forgotPasswordResendOTPResponse.value = it
            }
    }

    fun observeForgotPasswordResendOTPResponse() =
        forgotPasswordResendOTPResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun updatePassword(updatePasswordRequestModel: UpdatePasswordRequestModel) {
        networkHelper.serviceCall(serviceGateway.updatePassword(updatePasswordRequestModel))
            .observeForever {
                updatePasswordResponse.value = it
            }
    }

    fun observeUpdateForgotPasswordResponse() =
        updatePasswordResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

}