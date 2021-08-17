package com.onelink.nrlp.android.features.nrlpBenefits.viewmodel

import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.nrlpBenefits.repo.NrlpBenefitsRepo
import javax.inject.Inject

class NrlpPartnersFragmentViewModel @Inject constructor(private val nrlpBenefitsRepo: NrlpBenefitsRepo) : BaseViewModel() {

    fun getNrlpBenefits() = nrlpBenefitsRepo.getNrlpBenefits()

    fun observeNrlpBenefits() = nrlpBenefitsRepo.observeNrlpBenefits()

    override fun onCleared() {
        nrlpBenefitsRepo.onClear()
        super.onCleared()
    }
}
