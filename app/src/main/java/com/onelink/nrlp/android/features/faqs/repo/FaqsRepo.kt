package com.onelink.nrlp.android.features.faqs.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.data.NetworkHelper
import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.faqs.model.FaqsResponseModel
import javax.inject.Inject

open class FaqsRepo @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val serviceGateway: ServiceGateway
) {

    val faqsResponse = MutableLiveData<BaseResponse<FaqsResponseModel>>()

    fun getFaqsQuestion() {
        networkHelper.serviceCall(serviceGateway.getFaqs()).observeForever {
            faqsResponse.value = it
        }
    }

    fun observeFAQs() = faqsResponse as LiveData<BaseResponse<FaqsResponseModel>>

    fun onClear() {
        networkHelper.dispose()
        networkHelper.disposable = null
    }
}