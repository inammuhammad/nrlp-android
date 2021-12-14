package com.onelink.nrlp.android.features.redeem.viewmodels

import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.redeem.fragments.RedemptionUSCPSIDFragment
import com.onelink.nrlp.android.features.redeem.repo.RedemptionRepo
import javax.inject.Inject

class RedemptionUSCDescriptionViewModel @Inject constructor(private val redPartnerRepo: RedemptionRepo): BaseViewModel() {
    fun getRedeemPartner() = redPartnerRepo.getRedeemPartner()

    fun observerRedeemPartner() = redPartnerRepo.observeRedeemPartner()

    override fun onCleared() {
        redPartnerRepo.onClear()
        super.onCleared()
    }

    fun addNextFragment(fragmentHelper: BaseFragment.FragmentNavigationHelper) {
        fragmentHelper.addFragment(
            RedemptionUSCPSIDFragment.newInstance(),
            clearBackStack = false,
            addToBackStack = true
        )

    }
}