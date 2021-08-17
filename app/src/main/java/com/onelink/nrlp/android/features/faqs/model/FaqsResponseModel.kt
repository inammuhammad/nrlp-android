package com.onelink.nrlp.android.features.faqs.model

import com.google.gson.annotations.SerializedName

data class FaqsResponseModel(@SerializedName("data") val questions: Questions)

data class Questions(@SerializedName("questions") val faqsList: ArrayList<FAQsModel>)

data class FAQsModel(var question: String, var answer: String)

data class FAQAdapterModel(var faqsModel: FAQsModel, var isExpanded: Boolean)