package com.onelink.nrlp.android.features.register.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.register.models.VerifyRegistrationCodeRequest
import com.onelink.nrlp.android.features.register.registerRepo.RegisterRepo
import javax.inject.Inject

class VerifyBeneficiaryFragmentViewModel @Inject constructor(private val registerRepo: RegisterRepo) :
    BaseViewModel() {
    val etOTP1 = MutableLiveData<String>("")
    val etOTP2 = MutableLiveData<String>("")
    val etOTP3 = MutableLiveData<String>("")
    val etOTP4 = MutableLiveData<String>("")

    fun verifyRegistrationCode(verifyRegistrationCodeRequest: VerifyRegistrationCodeRequest) =
        registerRepo.verifyRegistrationCode(verifyRegistrationCodeRequest)

    fun observeVerifyRegistrationCode() = registerRepo.observeVerifyRegistrationCode()

    override fun onCleared() {
        registerRepo.onClear()
        super.onCleared()
    }

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

    fun getRegistrationCode(): String {
        return etOTP1.value + etOTP2.value + etOTP3.value + etOTP4.value
    }
}