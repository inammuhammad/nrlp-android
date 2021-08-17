package com.onelink.nrlp.android.features.profile.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.data.NetworkHelper
import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.profile.models.ProfileResendOtpRequestModel
import com.onelink.nrlp.android.features.profile.models.UpdateProfileOtpRequestModel
import com.onelink.nrlp.android.features.profile.models.UpdateProfileRequestModel
import com.onelink.nrlp.android.features.select.country.model.CountryCodeResponseModel
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import com.onelink.nrlp.android.utils.mocks.MockedAPIResponseModels
import javax.inject.Inject

/**
 * Created by Qazi Abubakar on 11/07/2020.
 */
open class ProfileRepo @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val serviceGateway: ServiceGateway
) {
    val verifyResendOTPResponse = MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()
    val countryCodesResponse = MutableLiveData<BaseResponse<CountryCodeResponseModel>>()
    val updateProfileResponse = MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()
    val updateProfileOtpResponse = MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()

    fun getCountryCodes() {
        networkHelper.serviceCall(serviceGateway.getCountryCodes("", "")).observeForever {
            countryCodesResponse.value = it
        }
    }

    fun observeCountryCodes() =
        countryCodesResponse as LiveData<BaseResponse<CountryCodeResponseModel>>

    fun updateProfile(jsonObject: JsonObject) {
        networkHelper.serviceCall(serviceGateway.updateProfile(jsonObject))
            .observeForever {
                updateProfileResponse.value = it
            }
    }

    fun observeUpdateProfile() =
        updateProfileResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun updateProfileVerifyOtp(jsonObject: JsonObject) {
        networkHelper.serviceCall(serviceGateway.updateProfileVerifyOtp(jsonObject))
            .observeForever {
                updateProfileOtpResponse.value = it
            }
    }

    fun observeUpdateProfileOtp() =
        updateProfileOtpResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun verifyResendOTP(jsonObject: JsonObject) {
        networkHelper.serviceCall(serviceGateway.updateProfileResendOtp(jsonObject))
            .observeForever {
                verifyResendOTPResponse.value = it
            }
    }

    fun observeResendOTP() =
        verifyResendOTPResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun onClear() {
        networkHelper.dispose()
        networkHelper.disposable = null
    }
}

object UpdateProfileConstants {
    const val MOBILE_NO = "mobile_no"
    const val EMAIL = "email"
    const val OTP = "otp"
}