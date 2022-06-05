package com.onelink.nrlp.android.features.rate.viewmodels

import com.google.gson.JsonObject
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.rate.repo.RatingRepo
import javax.inject.Inject

class RateViewModel @Inject constructor(private val ratingRepo: RatingRepo) : BaseViewModel() {

    fun rateRedemption(jsonObject: JsonObject) {
        ratingRepo.rateRedemption(jsonObject)
    }

    fun rateRegistration(jsonObject: JsonObject) {
        ratingRepo.rateRegistration(jsonObject)
    }

    fun observeRateRedemption() = ratingRepo.observeRateRedemptionSuccess()

    fun observeRateRegistration() = ratingRepo.observeRateRegistrationSuccess()

    override fun onCleared() {
        ratingRepo.onClear()
        super.onCleared()
    }
}