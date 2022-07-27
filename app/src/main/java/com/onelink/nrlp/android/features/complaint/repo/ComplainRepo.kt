package com.onelink.nrlp.android.features.complaint.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.data.NetworkHelper
import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.complaint.models.AddComplaintResponseModel
import com.onelink.nrlp.android.features.redeem.model.RedeemPartnerResponseModel
import com.onelink.nrlp.android.models.BeneficiariesResponseModel
import javax.inject.Inject

open class ComplainRepo @Inject constructor(
private val networkHelper: NetworkHelper,
private val serviceGateway: ServiceGateway
) {
    val beneficiariesResponse = MutableLiveData<BaseResponse<BeneficiariesResponseModel>>()
    private val complainAddResponse =
        MutableLiveData<BaseResponse<AddComplaintResponseModel>>()
    private val redeemPartnersResponse = MutableLiveData<BaseResponse<RedeemPartnerResponseModel>>()

    fun addComplainRequest(addComplaintRequestModel: JsonObject) {
        networkHelper.serviceCall(serviceGateway.addComplaint(addComplaintRequestModel))
            .observeForever {
                complainAddResponse.value = it
            }
    }

    fun getAllBeneficiaries() {
        networkHelper.serviceCall(serviceGateway.getBeneficiaries()).observeForever {
            beneficiariesResponse.value = it
        }
    }

    fun observeAddComplainResponse() =
        complainAddResponse as LiveData<BaseResponse<AddComplaintResponseModel>>


    fun getRedeemPartner() {
        networkHelper.serviceCall(serviceGateway.getRedemptionPartners()).observeForever {
            redeemPartnersResponse.value = it
        }
    }

    fun observeRedeemPartner() =
        redeemPartnersResponse as LiveData<BaseResponse<RedeemPartnerResponseModel>>

    fun observeBeneficiaryResponse() =
        beneficiariesResponse as LiveData<BaseResponse<BeneficiariesResponseModel>>

}