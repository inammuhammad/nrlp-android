package com.onelink.nrlp.android.features.redeem.viewmodels

import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.redeem.fragments.*
import com.onelink.nrlp.android.features.redeem.repo.RedemptionRepo
import javax.inject.Inject

class RedemtionFragmentPartnerViewModel   @Inject constructor(private val redPartnerRepo: RedemptionRepo) :
    BaseViewModel() {

    fun getRedeemPartner() = redPartnerRepo.getRedeemPartner()

    fun observerRedeemPartner() = redPartnerRepo.observeRedeemPartner()

    override fun onCleared() {
        redPartnerRepo.onClear()
        super.onCleared()
    }

    fun addNextFragment(fragmentHelper: BaseFragment.FragmentNavigationHelper) {
             fragmentHelper.addFragment(
                RedemptionPartnerServiceFragment.newInstance(),
                clearBackStack = false,
                addToBackStack = true
            )
    }
    fun addFbrFragment(fragmentHelper: BaseFragment.FragmentNavigationHelper) {
        fragmentHelper.addFragment(
            RedemptionFBRDescriptionFragment.newInstance(),
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

    fun addPIADescriptionFragment(fragmentHelper: BaseFragment.FragmentNavigationHelper) {
        fragmentHelper.addFragment(
            RedemptionPIADescriptionFragment.newInstance(),
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

    fun addUSCDescriptionFragment(fragmentHelper: BaseFragment.FragmentNavigationHelper) {
        fragmentHelper.addFragment(
            RedemptionUSCDescriptionFragment.newInstance(),
            clearBackStack = false,
            addToBackStack = true
        )
    }
}