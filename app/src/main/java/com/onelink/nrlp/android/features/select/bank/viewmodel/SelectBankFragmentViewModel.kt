package com.onelink.nrlp.android.features.select.bank.viewmodel

import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.receiver.repo.ReceiverRepo
import com.onelink.nrlp.android.features.select.bank.repo.BanksRepo
import com.onelink.nrlp.android.features.select.country.repo.SelectCountryRepo
import java.util.*
import javax.inject.Inject

class SelectBankFragmentViewModel @Inject constructor(private val banksRepo: BanksRepo) :
    BaseViewModel() {

    fun getBanksList() = banksRepo.getBanksList()

    fun observeBankList() = banksRepo.observeBanksListResponse()

    override fun onCleared() {
        banksRepo.onClear()
        super.onCleared()
    }
}