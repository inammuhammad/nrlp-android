package com.onelink.nrlp.android.features.faqs.viewmodel

import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.faqs.repo.FaqsRepo
 import javax.inject.Inject

open class FAQsFragmentViewModel  @Inject constructor(private val faqsRepo: FaqsRepo) :
    BaseViewModel() {

    fun getFaqs() = faqsRepo.getFaqsQuestion()

    fun observerFaqs() = faqsRepo.observeFAQs()

    override fun onCleared() {
        faqsRepo.onClear()
        super.onCleared()
    }
}