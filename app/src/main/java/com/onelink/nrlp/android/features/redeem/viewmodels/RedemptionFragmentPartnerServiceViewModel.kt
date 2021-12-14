package com.onelink.nrlp.android.features.redeem.viewmodels

import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.redeem.fragments.*
import com.onelink.nrlp.android.features.redeem.model.InitializeRedeemNadraRequestModel
import com.onelink.nrlp.android.features.redeem.model.InitializeRedeemPIAOTPRequestModel
import com.onelink.nrlp.android.features.redeem.model.InitializeRedeemPassportOTPRequestModel
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

    private fun initializeRedemptionOTP(initializeRedeemPassportOTPRequestModel: InitializeRedeemPassportOTPRequestModel) =
        redPartnerRepo.initializeRedemptionPassportOTP(initializeRedeemPassportOTPRequestModel)

    fun observeInitializeRedemption() = redPartnerRepo.observeInitializeRedemption()

    fun observeInitializeRedemptionOTP() = redPartnerRepo.observeInitializeRedemptionFBROTP()

    fun compareRedeemAmount(redeemablePKR: Double, redeemAmount: Double) : Boolean =
        redeemablePKR > redeemAmount

    fun makeInitializeRedemptionCall() {
        initializeRedemption(
            InitializeRedeemRequestModel(
                partnerId = partnerId.value,
                categoryId = categoryId.value,
                points = points.value
            )
        )
    }

    fun makeInitializeRedemptionOTPCall(code: String,
                                        pse: String,
                                        pseChild: String,
                                        consumerNo: String,
                                        mobileNo: String,
                                        email:String,
                                        sotp: String,
                                        points: String
    ) {
        initializeRedemptionOTP(
            InitializeRedeemPassportOTPRequestModel(
                code = code,
                pse = pse,
                pseChild = pseChild,
                consumerNo = consumerNo,
                mobileNo = mobileNo,
                email = email,
                sotp = sotp,
                amount = points,
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

    fun addPIADescriptionFragment(fragmentHelper: BaseFragment.FragmentNavigationHelper) {
        fragmentHelper.addFragment(
            RedemptionPIADescriptionFragment.newInstance(),
            clearBackStack = false,
            addToBackStack = true
        )
    }

    fun addNADRADescriptionFragment(fragmentHelper: BaseFragment.FragmentNavigationHelper) {
        fragmentHelper.addFragment(
            RedemptionNadraDescriptionFragment.newInstance(),
            clearBackStack = false,
            addToBackStack = true
        )
    }

    fun addUSCDescriptionFragment(fragmentHelper: BaseFragment.FragmentNavigationHelper) {
        fragmentHelper.addFragment(
            RedemptionUSCDescriptionFragment.newInstance(),
            clearBackStack = false,
            addToBackStack = true
        )
    }

    fun addOPFVoucherFragment(fragmentHelper: BaseFragment.FragmentNavigationHelper) {
        fragmentHelper.addFragment(
            RedemptionOPFVoucherFragment.newInstance(),
            clearBackStack = false,
            addToBackStack = true
        )
    }

    fun addSLICPolicyFragment(fragmentHelper: BaseFragment.FragmentNavigationHelper) {
        fragmentHelper.addFragment(
            RedemptionSLICPolicyFragment.newInstance(),
            clearBackStack = false,
            addToBackStack = true
        )
    }

    fun addBEOECNICFragment(fragmentHelper: BaseFragment.FragmentNavigationHelper) {
        fragmentHelper.addFragment(
            RedemptionBEOECNICFragment.newInstance(),
            clearBackStack = false,
            addToBackStack = true
        )
    }
}