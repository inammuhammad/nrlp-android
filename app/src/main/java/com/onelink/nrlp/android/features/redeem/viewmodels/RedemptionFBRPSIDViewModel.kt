package com.onelink.nrlp.android.features.redeem.viewmodels

import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.redeem.model.InitializeRedeemFBROTPRequestModel
import com.onelink.nrlp.android.features.redeem.model.InitializeRedeemFBRRequestModel
import com.onelink.nrlp.android.features.redeem.model.RedeemCompletionFBRRequestModel
import com.onelink.nrlp.android.features.redeem.model.RedeemCompletionRequestModel
import com.onelink.nrlp.android.features.redeem.repo.RedemptionRepo
import com.onelink.nrlp.android.utils.ValidationUtils
import javax.inject.Inject

class RedemptionFBRPSIDViewModel @Inject constructor(private val redPartnerRepo: RedemptionRepo) : BaseViewModel() {
    val psid = MutableLiveData<String>("")
    val transactionId = MutableLiveData<String>()

    fun getRedeemPartner() = redPartnerRepo.getRedeemPartner()

    fun observerRedeemPartner() = redPartnerRepo.observeRedeemPartner()

    private fun initializeRedemption(initializeRedeemFBRRequestModel: InitializeRedeemFBRRequestModel) =
        redPartnerRepo.initializeRedemptionFBR(initializeRedeemFBRRequestModel)

    private fun initializeRedemptionOTP(initializeRedeemFBROTPRequestModel: InitializeRedeemFBROTPRequestModel) =
        redPartnerRepo.initializeRedemptionFBROTP(initializeRedeemFBROTPRequestModel)

    fun observeInitializeRedemption() = redPartnerRepo.observeInitializeRedemptionFBR()

    fun observeInitializeRedemptionOTP() = redPartnerRepo.observeInitializeRedemptionFBROTP()

    fun checkPSIDValidation(string: String) : Boolean {
        return string.isNotEmpty() && ValidationUtils.isPSIDLengthValid(string)
    }

    fun compareRedeemAmount(redeemablePKR: Double, redeemAmount: Double) : Boolean =
        redeemablePKR > redeemAmount

    fun makeInitializeRedemptionCall(code: String, pse: String, consumerNo: String) {
        initializeRedemption(
            InitializeRedeemFBRRequestModel(
                code = code,
                pse = pse,
                consumerNo = consumerNo
            )
        )
    }

    fun makeInitializeRedemptionOTPCall(code: String, pse: String, consumerNo: String,
                                        amount: String, sotp: String) {
        initializeRedemptionOTP(
            InitializeRedeemFBROTPRequestModel(
                code = code,
                pse = pse,
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
}