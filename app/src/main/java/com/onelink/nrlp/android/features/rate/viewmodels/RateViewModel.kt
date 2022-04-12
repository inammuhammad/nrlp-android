package com.onelink.nrlp.android.features.rate.viewmodels

import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.rate.model.RateRedemptionRequestModel
import com.onelink.nrlp.android.features.redeem.repo.RedemptionRepo
import javax.inject.Inject

class RateViewModel @Inject constructor(private val redeemRepo: RedemptionRepo) : BaseViewModel(){

    fun rateRedemption(rateRedemptionRequestModel: RateRedemptionRequestModel){
        redeemRepo.rateRedemption(rateRedemptionRequestModel)
    }

    fun observeRateRedemption() = redeemRepo.observeRateRedemptionSuccess()

    override fun onCleared() {
        redeemRepo.onClear()
        super.onCleared()
    }
}