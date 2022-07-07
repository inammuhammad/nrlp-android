package com.onelink.nrlp.android.features.select.banksandexchange.viewmodel

import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.select.generic.model.BranchCenterRequestModel
import com.onelink.nrlp.android.features.select.generic.repo.SelectItemRepo
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class SelectBanksAndExchangeViewModel @Inject constructor(private val selectItemRepo: SelectItemRepo) :
    BaseViewModel() {

    var bcList: ArrayList<String> = ArrayList<String>(
        listOf(
            "Allied Bank Limited",
            "Askari Bank Limited",
            "Al Baraka Bank Pakistan",
            "BankIslami Pakistan Limited",
            "Bank of Khyber",
            "JS Bank Limited",
            "Khushhali Microfinance Bank Limited",
            "MCB Islamic Bank",
            "Mobilink Microfinance Bank Pvt. Ltd",
            "National Bank of Pakistan",
            "Silkbank Limited",
            "Sindh Bank Limited",
            "Soneri Bank Limited",
            "Summit Bank",
            "Bank Alfalah Limited",
            "Bank Al Habib Limited",
            "Bank of Punjab",
            "Dubai Islamic Bank",
            "Faysal Bank Limited",
            "Habib Metropolitan Bank",
            "Habib Bank Limited",
            "MCB Bank Limited",
            "Meezan Bank",
            "Samba Bank Limited",
            "Standard Chartered Pakistan",
            "United Bank Limited",
            "Telenor Microfinance Bank",
            "Zarai Taraqiati Bank Limited",
            "U Microfinance Bank Limited",
            "AA Exchange Company (Pvt.) Ltd",
            "D.D Exchange Company (Pvt.) Ltd",
            "Dollar East Exchange Company",
            "Fairdeal Exchange Company (Pvt) Limited",
            "H&H Exchange Co (Pvt.) Ltd",
            "Habib Qatar International Exchange Pakistan (Pvt.) Ltd",
            "Link International Exchange Co Pvt Ltd",
            "Muhammadi Exchange Company (Pvt) Limited",
            "NBP Exchange Company Limited",
            "Pakistan Currency Exchange (Pvt) Ltd",
            "Paracha International Exchange Pvt Ltd",
            "Paragon Exchange Private Limited",
            "Ravi Exchange Company pvt ltd",
            "Royal International Exchange Co, (Pvt) Ltd",
            "Sadiq Exchange Company (Pvt) Ltd",
            "Sky Exchange Company - Pvt Ltd",
            "Wall Street Exchange Company (PVT) Limited",
            "ZeeQue Exchange Company (Pvt) Ltd"
        )
    )

    fun getBranchCenter(pseName: String) =
        selectItemRepo.getBranchCenter(BranchCenterRequestModel(pseName))

    fun observeBranchCenter() = selectItemRepo.observeBranchCenter()

    override fun onCleared() {
        selectItemRepo.onClear()
        super.onCleared()
    }
}