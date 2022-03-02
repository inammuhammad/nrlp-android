package com.onelink.nrlp.android.features.receiver.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.data.NetworkHelper
import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.receiver.models.AddReceiverRequestModel
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import javax.inject.Inject

open class ReceiverRepo @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val serviceGateway: ServiceGateway
) {
    val receiverAddResponse =
        MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()

    fun addReceiver(addReceiverRequestModel: AddReceiverRequestModel) {
        networkHelper.serviceCall(serviceGateway.addReceiver(addReceiverRequestModel))
            .observeForever {
                receiverAddResponse.value = it
            }
    }

    fun observeReceiverAddResponse() =
        receiverAddResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun onClear() {
        networkHelper.dispose()
        networkHelper.disposable = null
    }
}