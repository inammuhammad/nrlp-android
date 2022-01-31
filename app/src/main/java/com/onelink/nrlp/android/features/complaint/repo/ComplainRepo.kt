package com.onelink.nrlp.android.features.complaint.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.data.NetworkHelper
import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.complaint.models.AddComplaintRequestModel
import com.onelink.nrlp.android.features.complaint.models.AddComplaintResponseModel
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import org.json.JSONObject
import javax.inject.Inject

open class ComplainRepo @Inject constructor(
private val networkHelper: NetworkHelper,
private val serviceGateway: ServiceGateway
) {
    private val complainAddResponse=
        MutableLiveData<BaseResponse<AddComplaintResponseModel>>()

    fun addComplainRequest(addComplaintRequestModel: JsonObject){
        networkHelper.serviceCall(serviceGateway.addComplaint(addComplaintRequestModel))
            .observeForever {
                complainAddResponse.value=it
            }
    }

    fun observeAddComplainResponse()=
        complainAddResponse as LiveData<BaseResponse<AddComplaintResponseModel>>

}