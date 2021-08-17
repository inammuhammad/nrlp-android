package com.onelink.nrlp.android.features.redeem.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.redeem.model.RedeemResendOTPRequestModel
import com.onelink.nrlp.android.features.redeem.model.VerifyRedeemOTPRequestModel
import com.onelink.nrlp.android.features.redeem.repo.RedemptionRepo
import javax.inject.Inject

class RedeemOtpFragmentViewModel @Inject constructor(private val redPartnerRepo: RedemptionRepo) :
    BaseViewModel() {
    val etOTP1 = MutableLiveData<String>("")
    val etOTP2 = MutableLiveData<String>("")
    val etOTP3 = MutableLiveData<String>("")
    val etOTP4 = MutableLiveData<String>("")
    val transactionId = MutableLiveData<String>()

    private fun verifyRedeemOTP(verifyRedeemOTPRequestModel: VerifyRedeemOTPRequestModel) =
        redPartnerRepo.verifyRedeemOTP(verifyRedeemOTPRequestModel)

    fun observeRedeemOTP() = redPartnerRepo.observeRedeemOTP()

    fun verifyRedeemResendOTP() = redPartnerRepo.verifyRedeemResendOTP(
        RedeemResendOTPRequestModel(
            transactionId = transactionId.value
        )
    )

    fun observeRedeemResendOTP() = redPartnerRepo.observeRedeemResendOTP()

    fun makeVerifyOtpCall() {
        verifyRedeemOTP(
            VerifyRedeemOTPRequestModel(
                transactionId = transactionId.value,
                otp = getOTPCode()
            )
        )
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

    fun getOTPCode(): String {
        return etOTP1.value + etOTP2.value + etOTP3.value + etOTP4.value
    }
}