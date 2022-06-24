package com.onelink.nrlp.android.features.select.generic.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.data.NetworkHelper
import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.select.city.model.CitiesRequest
import com.onelink.nrlp.android.features.select.city.model.CitiesResponseModel
import com.onelink.nrlp.android.features.select.generic.model.BranchCenterRequestModel
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import javax.inject.Inject

class SelectItemRepo @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val serviceGateway: ServiceGateway
) {

    val branchCentersResponse = MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()

    fun getBranchCenter(branchCenterRequestModel: BranchCenterRequestModel) {
        networkHelper.serviceCall((serviceGateway.getBranchCenter(branchCenterRequestModel)))
            .observeForever { response ->
                branchCentersResponse.value = response
            }
    }

    fun observeBranchCenter() =
        branchCentersResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun onClear() {
        networkHelper.dispose()
        networkHelper.disposable = null
    }
}