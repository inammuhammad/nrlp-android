package com.onelink.nrlp.android.features.select.city.viewmodel

import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.select.country.repo.SelectCountryRepo
import java.util.*
import javax.inject.Inject

class SelectCityFragmentViewModel @Inject constructor(private val selectCountryRepo: SelectCountryRepo) :
    BaseViewModel() {

    fun getCountryCodes(type: String = "beneficiary") = selectCountryRepo.getCountryCodes(type)

    fun observerCountryCodes() = selectCountryRepo.observeCountryCodes()

    override fun onCleared() {
        selectCountryRepo.onClear()
        super.onCleared()
    }

    fun getCities(searchText: String = "", pageNumber: Int = 0) =
        selectCountryRepo.getCities(searchText.toLowerCase(Locale.ROOT), pageNumber)

    fun observeCities() = selectCountryRepo.observeCities()
}