package com.onelink.nrlp.android.features.redeem.viewmodels

import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.redeem.fragments.RedemptionFBRPSIDFragment
import com.onelink.nrlp.android.features.redeem.fragments.RedemptionPIAPSIDFragment
import com.onelink.nrlp.android.features.redeem.repo.RedemptionRepo
import javax.inject.Inject

class RedemptionPIADescriptionViewModel @Inject constructor(private val redPartnerRepo: RedemptionRepo): BaseViewModel() {
    fun getRedeemPartner() = redPartnerRepo.getRedeemPartner()

    fun observerRedeemPartner() = redPartnerRepo.observeRedeemPartner()

    override fun onCleared() {
        redPartnerRepo.onClear()
        super.onCleared()
    }

    fun addNextFragment(fragmentHelper: BaseFragment.FragmentNavigationHelper) {
        fragmentHelper.addFragment(
            RedemptionPIAPSIDFragment.newInstance(),
            clearBackStack = false,
            addToBackStack = true
        )

    }
}