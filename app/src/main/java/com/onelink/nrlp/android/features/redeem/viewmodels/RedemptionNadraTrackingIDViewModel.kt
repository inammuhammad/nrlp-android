package com.onelink.nrlp.android.features.redeem.viewmodels

import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.redeem.model.InitializeRedeemFBROTPRequestModel
import com.onelink.nrlp.android.features.redeem.model.InitializeRedeemFBRRequestModel
import com.onelink.nrlp.android.features.redeem.model.InitializeRedeemNadraOTPRequestModel
import com.onelink.nrlp.android.features.redeem.model.InitializeRedeemNadraRequestModel
import com.onelink.nrlp.android.features.redeem.repo.RedemptionRepo
import com.onelink.nrlp.android.utils.ValidationUtils
import javax.inject.Inject

class RedemptionNadraTrackingIDViewModel@Inject constructor(private val redPartnerRepo: RedemptionRepo) : BaseViewModel() {
    val psid = MutableLiveData<String>("")
    fun getRedeemPartner() = redPartnerRepo.getRedeemPartner()

    fun observerRedeemPartner() = redPartnerRepo.observeRedeemPartner()

    private fun initializeRedemption(initializeRedeemNadraRequestModel: InitializeRedeemNadraRequestModel) =
        redPartnerRepo.initializeRedemptionNadra(initializeRedeemNadraRequestModel)

    private fun initializeRedemptionOTP(initializeRedeemNadraOTPRequestModel: InitializeRedeemNadraOTPRequestModel) =
        redPartnerRepo.initializeRedemptionNadraOTP(initializeRedeemNadraOTPRequestModel)

    fun observeInitializeRedemption() = redPartnerRepo.observeInitializeRedemptionFBR()

    fun observeInitializeRedemptionOTP() = redPartnerRepo.observeInitializeRedemptionFBROTP()

    fun checkTrackingNoValidation(string: String) : Boolean {
        return string.isNotEmpty() && ValidationUtils.isTrackingNoLengthValid(string)
    }

    fun checkCNICNoValidation(string: String) : Boolean {
        return string.isNotEmpty() || string.length == 15
    }

    fun compareRedeemAmount(redeemablePKR: Double, redeemAmount: Double) : Boolean =
        redeemablePKR > redeemAmount

    fun makeInitializeRedemptionCall(code: String, pse: String, pseChild: String, trackingNo: String, consumerNo: String ) {
        initializeRedemption(
            InitializeRedeemNadraRequestModel(
                code = code,
                pse = pse,
                pseChild = pseChild,
                trackingNo = trackingNo,
                consumerNo = consumerNo
            )
        )
    }

    fun makeInitializeRedemptionOTPCall(code: String, pse: String, pseChild: String,
                                        trackingId: String,
                                        consumerNo: String,
                                        amount: String,
                                        sotp: String) {
        initializeRedemptionOTP(
            InitializeRedeemNadraOTPRequestModel(
                code = code,
                pse = pse,
                pseChild = pseChild,
                trackingId = trackingId,
                amount = amount,
                consumerNo = consumerNo,
                sotp = sotp
            )
        )
    }

    override fun onCleared() {
        redPartnerRepo.onClear()
        super.onCleared()
    }
}