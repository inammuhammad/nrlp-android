package com.onelink.nrlp.android.features.managePoints.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.data.NetworkHelper
import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.managePoints.model.TransferPointsRequest
import com.onelink.nrlp.android.features.managePoints.model.TransferPointsResponseModel
import com.onelink.nrlp.android.models.BeneficiariesResponseModel
import javax.inject.Inject

open class ManagePointsFragmentRepo @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val serviceGateway: ServiceGateway
) {

    val beneficiariesResponse = MutableLiveData<BaseResponse<BeneficiariesResponseModel>>()

    val transferPointsResponse = MutableLiveData<BaseResponse<TransferPointsResponseModel>>()

    fun getAllBeneficiaries() {
        networkHelper.serviceCall(serviceGateway.getBeneficiaries()).observeForever {
            beneficiariesResponse.value = it
        }
    }

    fun transferPoints(transferPointsRequest: TransferPointsRequest) {
        networkHelper.serviceCall(serviceGateway.transferPoints(transferPointsRequest))
            .observeForever {
                transferPointsResponse.value = it
            }
    }

    fun observeTransferPoints() =
        transferPointsResponse as LiveData<BaseResponse<TransferPointsResponseModel>>

    fun observeBeneficiaryResponse() =
        beneficiariesResponse as LiveData<BaseResponse<BeneficiariesResponseModel>>
}
