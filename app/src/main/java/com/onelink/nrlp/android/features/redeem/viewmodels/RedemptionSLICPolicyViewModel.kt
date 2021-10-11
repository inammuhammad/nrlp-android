package com.onelink.nrlp.android.features.redeem.viewmodels

import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.redeem.model.InitializeRedeemOPFOTPRequestModel
import com.onelink.nrlp.android.features.redeem.model.InitializeRedeemOPFRequestModel
import com.onelink.nrlp.android.features.redeem.repo.RedemptionRepo
import com.onelink.nrlp.android.utils.ValidationUtils
import javax.inject.Inject

class RedemptionSLICPolicyViewModel  @Inject constructor(private val redPartnerRepo: RedemptionRepo) : BaseViewModel() {
    val policyNo = MutableLiveData<String>("")
    fun getRedeemPartner() = redPartnerRepo.getRedeemPartner()

    fun observerRedeemPartner() = redPartnerRepo.observeRedeemPartner()

    private fun initializeRedemption(initializeRedeemOPFRequestModel: InitializeRedeemOPFRequestModel) =
        redPartnerRepo.initializeRedemptionOPF(initializeRedeemOPFRequestModel)

    private fun initializeRedemptionOTP(initializeRedeemOPFOTPRequestModel: InitializeRedeemOPFOTPRequestModel) =
        redPartnerRepo.initializeRedemptionOPFOTP(initializeRedeemOPFOTPRequestModel)

    fun observeInitializeRedemption() = redPartnerRepo.observeInitializeRedemptionFBR()

    fun observeInitializeRedemptionOTP() = redPartnerRepo.observeInitializeRedemptionFBROTP()

    fun checkVoucherValidation(string: String) : Boolean {
        return string.isNotEmpty() && ValidationUtils.isPSIDLengthValid(string)
    }

    fun compareRedeemAmount(redeemablePKR: Double, redeemAmount: Double) : Boolean =
        redeemablePKR > redeemAmount

    fun makeInitializeRedemptionCall(code: String, pse: String,
                                     pseChild:String,
                                     consumerNo: String) {
        initializeRedemption(
            InitializeRedeemOPFRequestModel(
                code = code,
                pse = pse,
                pseChild = pseChild,
                consumerNo = consumerNo
            )
        )
    }

    fun makeInitializeRedemptionOTPCall(code: String, pse: String,
                                        pseChild: String,
                                        consumerNo: String,
                                        amount: String,
                                        sotp: String) {
        initializeRedemptionOTP(
            InitializeRedeemOPFOTPRequestModel(
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
        amountEntered != null && amountEntered <= actualAmount //&& amountEntered > 0
}