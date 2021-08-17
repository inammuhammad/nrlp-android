package com.onelink.nrlp.android.features.nrlpBenefits.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.data.NetworkHelper
import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.nrlpBenefits.model.NrlpBenefitsResponseModel
import com.onelink.nrlp.android.features.nrlpBenefits.model.NrlpPartnerResponseModel
import javax.inject.Inject

class NrlpBenefitsRepo @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val serviceGateway: ServiceGateway
) {
    val redeemPartnersResponse = MutableLiveData<BaseResponse<NrlpPartnerResponseModel>>()
    val nrlpPartnerBenefitsResponse = MutableLiveData<BaseResponse<NrlpBenefitsResponseModel>>()

    fun getNrlpBenefits() {
        networkHelper.serviceCall(serviceGateway.getNrlpBenefits()).observeForever {
            redeemPartnersResponse.value = it
        }
    }

    fun observeNrlpBenefits() =
        redeemPartnersResponse as LiveData<BaseResponse<NrlpPartnerResponseModel>>

    fun getNrlpPartnerBenefits(id: Int) {
        networkHelper.serviceCall(serviceGateway.getNrlpPartnerBenefits(id)).observeForever {
            nrlpPartnerBenefitsResponse.value = it
        }
    }

    fun observeNrlpPartnerBenefits() =
        nrlpPartnerBenefitsResponse as LiveData<BaseResponse<NrlpBenefitsResponseModel>>

    fun onClear() {
        networkHelper.dispose()
        networkHelper.disposable = null
    }

}