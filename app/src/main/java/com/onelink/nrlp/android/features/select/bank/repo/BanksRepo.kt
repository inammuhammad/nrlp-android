package com.onelink.nrlp.android.features.select.bank.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.data.NetworkHelper
import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.select.bank.model.BanksListResponse
import javax.inject.Inject

open class BanksRepo @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val serviceGateway: ServiceGateway
) {

    val banksListResponse =
        MutableLiveData<BaseResponse<BanksListResponse>>()

    fun getBanksList() {
        networkHelper.serviceCall(serviceGateway.getBanks()).observeForever {
            banksListResponse.value = it
        }
    }

    fun observeBanksListResponse() =
        banksListResponse as LiveData<BaseResponse<BanksListResponse>>

    fun onClear() {
        networkHelper.dispose()
        networkHelper.disposable = null
    }
}