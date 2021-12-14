package com.onelink.nrlp.android.features.select.country.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.data.NetworkHelper
import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.register.models.CountryCodesRequest
import com.onelink.nrlp.android.features.select.country.model.CountryCodeResponseModel
import javax.inject.Inject

class SelectCountryRepo @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val serviceGateway: ServiceGateway
) {


    val countryCodesResponse = MutableLiveData<BaseResponse<CountryCodeResponseModel>>()

    fun getCountryCodes(type: String = "beneficiary") {
        networkHelper.serviceCall(serviceGateway.getCountryCodes(CountryCodesRequest(type)))
            .observeForever { response ->
                countryCodesResponse.value = response
            }
    }

    fun observeCountryCodes() =
        countryCodesResponse as LiveData<BaseResponse<CountryCodeResponseModel>>

    fun onClear() {
        networkHelper.dispose()
        networkHelper.disposable = null
    }
}
