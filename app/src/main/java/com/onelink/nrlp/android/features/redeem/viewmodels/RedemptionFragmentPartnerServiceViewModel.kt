package com.onelink.nrlp.android.features.redeem.viewmodels

import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.redeem.model.InitializeRedeemRequestModel
import com.onelink.nrlp.android.features.redeem.repo.RedemptionRepo
import java.math.BigInteger
import javax.inject.Inject

class RedemptionFragmentPartnerServiceViewModel @Inject constructor(private val redPartnerRepo: RedemptionRepo) :
    BaseViewModel() {
    val loyaltyPointBalance = MutableLiveData<BigInteger>()
    val partnerId = MutableLiveData<Int>()
    val categoryId = MutableLiveData<Int>()
    val points = MutableLiveData<BigInteger>()
    val categoryName = MutableLiveData<String>("")
    val partnerName = MutableLiveData<String>("")

    private fun initializeRedemption(initializeRedeemRequestModel: InitializeRedeemRequestModel) =
        redPartnerRepo.initializeRedemption(initializeRedeemRequestModel)

    fun observeInitializeRedemption() = redPartnerRepo.observeInitializeRedemption()

    fun makeInitializeRedemptionCall() {
        initializeRedemption(
            InitializeRedeemRequestModel(
                partnerId = partnerId.value,
                categoryId = categoryId.value,
                points = points.value
            )
        )
    }

    override fun onCleared() {
        redPartnerRepo.onClear()
        super.onCleared()
    }

    fun isValidTransaction(selectedPoints: BigInteger, totalPoints: BigInteger): Boolean {
        return selectedPoints <= totalPoints
    }
}