package com.onelink.nrlp.android.features.uuid.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.login.model.LoginRequest
import com.onelink.nrlp.android.features.login.repo.LoginRepo
import com.onelink.nrlp.android.features.uuid.model.UniqueIdentifierResendOTPRequest
import com.onelink.nrlp.android.features.uuid.model.UniqueIdentifierUpdateRequest
import com.onelink.nrlp.android.models.LoginCredentials
import com.onelink.nrlp.android.utils.LukaKeRakk
import com.onelink.nrlp.android.utils.cleanNicNumber
import java.util.*
import javax.inject.Inject

class LoginOtpFragmentViewModel @Inject constructor(private val loginRepo: LoginRepo) :
    BaseViewModel() {

    val etOTP1 = MutableLiveData("")
    val etOTP2 = MutableLiveData("")
    val etOTP3 = MutableLiveData("")
    val etOTP4 = MutableLiveData("")

    lateinit var loginCredentials: LoginCredentials

    private fun uniqueIdentifierUpdate(uniqueIdentifierUpdateRequest: UniqueIdentifierUpdateRequest) =
        loginRepo.uniqueIdentifierUpdate(uniqueIdentifierUpdateRequest)

    fun observeUpdateUUIDResponse() = loginRepo.observeUpdateUUIDResponse()

    fun resendOTP() = loginRepo.verifyUUIDResendOTP(
        UniqueIdentifierResendOTPRequest(
            nicNicop = loginCredentials.cnic.cleanNicNumber(),
            password = loginCredentials.password,
            userType = loginCredentials.accountType
        )
    )

    fun observeResendOTP() = loginRepo.observeUUIDResendOTP()

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

    fun updateUUIDCall() {
        uniqueIdentifierUpdate(
            UniqueIdentifierUpdateRequest(
                loginCredentials.cnic.replace("-", ""),
                loginCredentials.password,
                loginCredentials.accountType.toLowerCase(Locale.getDefault()),
                getOTPCode()
            )
        )
    }

    fun getOTPCode(): String {
        return etOTP1.value + etOTP2.value + etOTP3.value + etOTP4.value
    }
}