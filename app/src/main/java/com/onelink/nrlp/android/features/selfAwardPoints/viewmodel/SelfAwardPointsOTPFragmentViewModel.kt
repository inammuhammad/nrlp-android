package com.onelink.nrlp.android.features.selfAwardPoints.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.selfAwardPoints.model.SelfAwardPointsOTPRequestModel
import com.onelink.nrlp.android.features.selfAwardPoints.model.SelfAwardPointsRequest
import com.onelink.nrlp.android.features.selfAwardPoints.repo.SelfAwardPointsFragmentRepo
import javax.inject.Inject

class SelfAwardPointsOTPFragmentViewModel @Inject constructor(private val selfAwardPointsFragmentRepo: SelfAwardPointsFragmentRepo) :
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

    fun selfAwardPointsOTP(selfAwardPointsOTPRequestModel: SelfAwardPointsOTPRequestModel) =
        selfAwardPointsFragmentRepo.selfAwardPointsOTP(selfAwardPointsOTPRequestModel)

    fun observeSelfAwardPointsOTPResponse() = selfAwardPointsFragmentRepo.observeSelfAwardPointsOTPResponse()

    fun getSelfAwardPointsResendOTP(selfAwardPointsRequest: SelfAwardPointsRequest) =
        selfAwardPointsFragmentRepo.selfAwardPointsResendOTP(selfAwardPointsRequest)

    fun observeSelfAwardPointsResendOTP() = selfAwardPointsFragmentRepo.observeSelfAwardPointsResendOTPResponse()
}