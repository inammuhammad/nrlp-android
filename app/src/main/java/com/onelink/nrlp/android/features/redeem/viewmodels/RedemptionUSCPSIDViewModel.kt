package com.onelink.nrlp.android.features.redeem.viewmodels

import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.redeem.model.InitializeRedeemPIAOTPRequestModel
import com.onelink.nrlp.android.features.redeem.model.InitializeRedeemPIARequestModel
import com.onelink.nrlp.android.features.redeem.repo.RedemptionRepo
import com.onelink.nrlp.android.utils.ValidationUtils
import javax.inject.Inject

class RedemptionUSCPSIDViewModel @Inject constructor(private val redPartnerRepo: RedemptionRepo) : BaseViewModel() {
    val psid = MutableLiveData<String>("")
    fun getRedeemPartner() = redPartnerRepo.getRedeemPartner()

    fun observerRedeemPartner() = redPartnerRepo.observeRedeemPartner()

    fun checkPSIDValidation(string: String) : Boolean {
        return string.isNotEmpty() && ValidationUtils.isPSIDLengthValid(string)
    }

    fun compareRedeemAmount(redeemablePKR: Double, redeemAmount: Double) : Boolean =
        redeemablePKR > redeemAmount

    private fun initializeRedemption(initializeRedeemPIARequestModel: InitializeRedeemPIARequestModel) =
        redPartnerRepo.initializeRedemptionPIA(initializeRedeemPIARequestModel)

    private fun initializeRedemptionOTP(initializeRedeemPIAOTPRequestModel: InitializeRedeemPIAOTPRequestModel) =
        redPartnerRepo.initializeRedemptionPIAOTP(initializeRedeemPIAOTPRequestModel)

    fun observeInitializeRedemption() = redPartnerRepo.observeInitializeRedemptionFBR()

    fun observeInitializeRedemptionOTP() = redPartnerRepo.observeInitializeRedemptionFBROTP()

    fun makeInitializeRedemptionCall(code: String, pse: String, pseChild: String, consumerNo: String) {
        initializeRedemption(
            InitializeRedeemPIARequestModel(
                code = code,
                pse = pse,
                pseChild = pseChild,
                consumerNo = consumerNo
            )
        )
    }

    fun makeInitializeRedemptionOTPCall(code: String, pse: String, pseChild: String,
                                        consumerNo: String,
                                        amount: String,
                                        sotp: String) {
        initializeRedemptionOTP(
            InitializeRedeemPIAOTPRequestModel(
                code = code,
                pse = pse,
                pseChild = pseChild,
                consumerNo = consumerNo,
                amount = amount,
                sotp = sotp
            )
        )
    }

    override fun onCleared() {
        redPartnerRepo.onClear()
        super.onCleared()
    }

    fun checkAmountValidation(actualAmount: Int, amountEntered: Int) : Boolean =
        amountEntered!= null && amountEntered <= actualAmount
}