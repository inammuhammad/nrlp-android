package com.onelink.nrlp.android.repos.faq

import com.onelink.nrlp.android.features.faqs.model.FAQsModel
import com.onelink.nrlp.android.features.faqs.model.FaqsResponseModel
import com.onelink.nrlp.android.features.faqs.model.Questions

fun getFaqResponseModel() = FaqsResponseModel(
    Questions(
        arrayListOf(
            FAQsModel(
                "Question",
                "Answer"
            )
        )
    )
)