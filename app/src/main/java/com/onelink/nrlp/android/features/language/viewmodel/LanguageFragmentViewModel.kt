package com.onelink.nrlp.android.features.language.viewmodel

import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.language.repo.LanguageRepo
import javax.inject.Inject

class LanguageFragmentViewModel @Inject constructor(private val languageRepo: LanguageRepo) :
    BaseViewModel() {

    @Suppress("unused")
    fun getLanguageName() = languageRepo.getLanguage()

    @Suppress("unused")
    fun observerLanguage() = languageRepo.observeLanguage()

    override fun onCleared() {
        languageRepo.onClear()
        super.onCleared()
    }
}