package com.onelink.nrlp.android.features.redeem.viewmodels

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.redeem.model.RedeemCompletionRequestModel
import com.onelink.nrlp.android.features.redeem.repo.RedemptionRepo
import com.onelink.nrlp.android.utils.ValidationUtils
import javax.inject.Inject

class RedeemAgentConfirmationViewModel @Inject constructor(private val redPartnerRepo: RedemptionRepo) :
    BaseViewModel() {
    val agentConfirmationCode = MutableLiveData("")
    val transactionId = MutableLiveData<String>()
    val validationAgentCodePassed = MutableLiveData(true)


    private fun completeRedemption(redeemCompletionRequestModel: RedeemCompletionRequestModel) =
        redPartnerRepo.completeRedemption(redeemCompletionRequestModel)

    fun observeRedeemSuccess() = redPartnerRepo.observeRedeemSuccess()

    fun makeCompleteRedemptionCall() {
        completeRedemption(
            RedeemCompletionRequestModel(
                transactionId = transactionId.value,
                agentCode = agentConfirmationCode.value
            )
        )
    }

    val isValidAgentConfirmationCode = MediatorLiveData<Boolean>().apply {
        addSource(agentConfirmationCode) {
            val valid = ValidationUtils.isValidAgentCodeLength(it)
            //Log.d(it, valid.toString())
            value = valid
        }
    }

    val isAgentCodeValidationPassed = MediatorLiveData<Boolean>().apply {
        addSource(validationAgentCodePassed) {
            value = it
        }
    }

    fun checkAgentCodeValidation(string: String) =
        string.isEmpty() || ValidationUtils.isValidAgentCode(string)

    fun validationsPassed(): Boolean {
        val isAgentCodeValid: Boolean =
            checkAgentCodeValidation(agentConfirmationCode.value.toString())
        isAgentCodeValidationPassed.value = isAgentCodeValid
        return isAgentCodeValid
    }
}