package com.onelink.nrlp.android.features.beneficiary.viewmodel

import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.beneficiary.repo.BeneficiaryRepo
import javax.inject.Inject


class ManageBeneficiaryViewModel @Inject constructor(private val beneficiaryRepo: BeneficiaryRepo) :
    BaseViewModel() {

    fun getAllBeneficiaries() = beneficiaryRepo.getAllBeneficiaries()

    fun observeAllBeneficiaries() = beneficiaryRepo.observeAllBeneficiaries()

    override fun onCleared() {
        beneficiaryRepo.onClear()
        super.onCleared()
    }
}