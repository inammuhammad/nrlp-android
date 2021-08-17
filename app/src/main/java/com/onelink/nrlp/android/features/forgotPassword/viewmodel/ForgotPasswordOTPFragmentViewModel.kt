package com.onelink.nrlp.android.features.forgotPassword.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.forgotPassword.models.ForgotPasswordOTPRequestModel
import com.onelink.nrlp.android.features.forgotPassword.models.ForgotPasswordRequestModel
import com.onelink.nrlp.android.features.forgotPassword.repo.ForgotPasswordRepo
import javax.inject.Inject

class ForgotPasswordOTPFragmentViewModel @Inject constructor(private val forgotPasswordRepo: ForgotPasswordRepo) :
    BaseViewModel() {
    val etOTP1 = MutableLiveData<String>("")
    val etOTP2 = MutableLiveData<String>("")
    val etOTP3 = MutableLiveData<String>("")
    val etOTP4 = MutableLiveData<String>("")

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

    fun forgotPasswordOTP(forgotPasswordOTPRequestModel: ForgotPasswordOTPRequestModel) =
        forgotPasswordRepo.forgotPasswordOTP(forgotPasswordOTPRequestModel)

    fun observeForgotPasswordOTPResponse() = forgotPasswordRepo.observeForgotPasswordOTPResponse()

    fun getResendOTP(forgotPasswordRequestModel: ForgotPasswordRequestModel) =
        forgotPasswordRepo.forgotPasswordResendOTP(forgotPasswordRequestModel)

    fun observeResendOTP() = forgotPasswordRepo.observeForgotPasswordResendOTPResponse()
}