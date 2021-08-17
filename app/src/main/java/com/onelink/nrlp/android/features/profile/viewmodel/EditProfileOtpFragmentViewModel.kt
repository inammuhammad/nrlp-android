package com.onelink.nrlp.android.features.profile.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.profile.models.ProfileResendOtpRequestModel
import com.onelink.nrlp.android.features.profile.models.UpdateProfileOtpRequestModel
import com.onelink.nrlp.android.features.profile.repo.ProfileRepo
import com.onelink.nrlp.android.features.profile.repo.UpdateProfileConstants
import javax.inject.Inject

class EditProfileOtpFragmentViewModel @Inject constructor(private val profileRepo: ProfileRepo) :
    BaseViewModel() {
    val etOTP1 = MutableLiveData<String>("")
    val etOTP2 = MutableLiveData<String>("")
    val etOTP3 = MutableLiveData<String>("")
    val etOTP4 = MutableLiveData<String>("")
    var updateProfileRequestModel: JsonObject = JsonObject()

    fun updateProfileVerifyOtp() {
        updateProfileRequestModel.addProperty(UpdateProfileConstants.OTP, getOTPCode())
        profileRepo.updateProfileVerifyOtp(updateProfileRequestModel)
    }

    fun observeUpdateProfileOtp() = profileRepo.observeUpdateProfileOtp()

    fun getResendOTP() = profileRepo.verifyResendOTP(updateProfileRequestModel)

    fun observeResendOTP() = profileRepo.observeResendOTP()

    val validEditText1 = MediatorLiveData<Boolean>().apply {
        addSource(etOTP1) {
            value = it.isNotEmpty()
        }
    }

    val validEditText2 = MediatorLiveData<Boolean>().apply {
        addSource(etOTP2) {
            value = it.isNotEmpty()
        }
    }

    val validEditText3 = MediatorLiveData<Boolean>().apply {
        addSource(etOTP3) {
            value = it.isNotEmpty()
        }
    }

    val validEditText4 = MediatorLiveData<Boolean>().apply {
        addSource(etOTP4) {
            value = it.isNotEmpty()
        }
    }

    fun getOTPCode(): String {
        return etOTP1.value + etOTP2.value + etOTP3.value + etOTP4.value
    }
}