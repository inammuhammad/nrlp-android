package com.onelink.nrlp.android.features.language.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.data.NetworkHelper
import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.language.model.LanguageResponseModel
import com.onelink.nrlp.android.utils.mocks.MockedAPIResponseModels
import javax.inject.Inject

class LanguageRepo @Inject constructor(
    private val networkHelper: NetworkHelper,
    @Suppress("unused") private val serviceGateway: ServiceGateway
) {
    private val languageResponse = MutableLiveData<BaseResponse<LanguageResponseModel>>()
    fun getLanguage() {
        languageResponse.value = MockedAPIResponseModels.getLanguageApiResposne()
        return
    }

    fun observeLanguage() =
        languageResponse as LiveData<BaseResponse<LanguageResponseModel>>

    fun onClear() {
        networkHelper.dispose()
    }
}