package com.onelink.nrlp.android.features.register.registerRepo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.data.NetworkHelper
import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.register.models.*
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import javax.inject.Inject

/**
 * Created by Qazi Abubakar on 08/07/2020.
 */
open class RegisterRepo @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val serviceGateway: ServiceGateway
) {
    val verifyReferenceNumResponse =
        MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()
    val verifyRegistrationCodeResponse =
        MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()
    val verifyOTPResponse =
        MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()
    val resendOTPResponse =
        MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()
    val termsAndConditionsResponse =
        MutableLiveData<BaseResponse<TermsAndConditionsResponseModel>>()
    val registerUserResponse =
        MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()

    fun verifyReferenceNumber(verifyReferenceNumberRequest: VerifyReferenceNumberRequest) {
        networkHelper.serviceCall(
            serviceGateway.verifyReferenceNumber(
                verifyReferenceNumberRequest
            )
        ).observeForever {
            verifyReferenceNumResponse.value = it
        }
    }

    fun observeVerifyReferenceNumber() =
        verifyReferenceNumResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun verifyRegistrationCode(verifyRegistrationCodeRequest: VerifyRegistrationCodeRequest) {
        networkHelper.serviceCall(
            serviceGateway.verifyRegistrationCode(
                verifyRegistrationCodeRequest
            )
        ).observeForever {
            verifyRegistrationCodeResponse.value = it
        }
    }

    fun observeVerifyRegistrationCode() =
        verifyRegistrationCodeResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun verifyOTP(verifyOTPRequest: VerifyOTPRequest) {
        networkHelper.serviceCall(serviceGateway.verifyOTP(verifyOTPRequest)).observeForever {
            verifyOTPResponse.value = it
        }
    }

    fun observeVerifyOTP() =
        verifyOTPResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun resendOTP(resendOTPRequest: ResendOTPRequest) {
        networkHelper.serviceCall(serviceGateway.resendOTP(resendOTPRequest)).observeForever {
            resendOTPResponse.value = it
        }
    }

    fun observeResendOTP() =
        resendOTPResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun getTermsAndConditions(lang: String = "en") {
        networkHelper.serviceCall(serviceGateway.getTermsAndConditions(TermsAndConditionsRequest(lang))).observeForever {
            termsAndConditionsResponse.value = it
        }
    }

    fun observeTermsAndConditions() =
        termsAndConditionsResponse as LiveData<BaseResponse<TermsAndConditionsResponseModel>>

    fun registerRemitter(registerRemitterRequest: RegisterRemitterRequest) {
        networkHelper.serviceCall(serviceGateway.registerRemitter(registerRemitterRequest))
            .observeForever {
                registerUserResponse.value = it
            }
    }

    fun registerBeneficiary(registerBeneficiaryRequest: RegisterBeneficiaryRequest) {
        networkHelper.serviceCall(serviceGateway.registerBeneficiary(registerBeneficiaryRequest))
            .observeForever {
                registerUserResponse.value = it
            }
    }

    fun observeRegisterUser() =
        registerUserResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun onClear() {
        networkHelper.dispose()
        networkHelper.disposable = null
    }
}
