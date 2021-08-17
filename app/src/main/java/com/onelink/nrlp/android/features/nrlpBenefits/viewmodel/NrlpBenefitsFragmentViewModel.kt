package com.onelink.nrlp.android.features.nrlpBenefits.viewmodel

import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.nrlpBenefits.repo.NrlpBenefitsRepo
import javax.inject.Inject

class NrlpBenefitsFragmentViewModel @Inject constructor(private val nrlpBenefitsRepo: NrlpBenefitsRepo) : BaseViewModel() {

    fun getNrlpPartnerBenefits(id: Int) = nrlpBenefitsRepo.getNrlpPartnerBenefits(id)

    fun observeNrlpPartnerBenefits() = nrlpBenefitsRepo.observeNrlpPartnerBenefits()

    override fun onCleared() {
        nrlpBenefitsRepo.onClear()
        super.onCleared()
    }
}
