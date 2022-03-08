package com.onelink.nrlp.android.features.receiver.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fasterxml.jackson.databind.ser.Serializers
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.data.NetworkHelper
import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.receiver.models.AddReceiverRequestModel
import com.onelink.nrlp.android.features.receiver.models.BanksListResponse
import com.onelink.nrlp.android.features.receiver.models.DeleteReceiverRequestModel
import com.onelink.nrlp.android.features.receiver.models.ReceiversResponseModel
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import javax.inject.Inject

open class ReceiverRepo @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val serviceGateway: ServiceGateway
) {
    val receiverAddResponse =
        MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()
    val receiversListResponse =
        MutableLiveData<BaseResponse<ReceiversResponseModel>>()
    val receiverDeleteResponse =
        MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()
    val banksListResponse =
        MutableLiveData<BaseResponse<BanksListResponse>>()

    fun addReceiver(addReceiverRequestModel: AddReceiverRequestModel) {
        networkHelper.serviceCall(serviceGateway.addReceiver(addReceiverRequestModel))
            .observeForever {
                receiverAddResponse.value = it
            }
    }

    fun getAllReceivers() {
        networkHelper.serviceCall(serviceGateway.getReceiversList())
            .observeForever {
            receiversListResponse.value = it
        }
    }

    fun deleteReceiver(deleteReceiverRequestModel: DeleteReceiverRequestModel){
        networkHelper.serviceCall(serviceGateway.deleteReceiver(deleteReceiverRequestModel))
            .observeForever {
            receiverDeleteResponse.value = it
        }
    }

    fun getBanksList() {

    }

    fun observeReceiverAddResponse() =
        receiverAddResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun observeReceiversListResponse() =
        receiversListResponse as LiveData<BaseResponse<ReceiversResponseModel>>

    fun observeReceiverDeleteResponse() =
        receiverDeleteResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun observeBanksListResponse() =
        banksListResponse as LiveData<BaseResponse<BanksListResponse>>

    fun onClear() {
        networkHelper.dispose()
        networkHelper.disposable = null
    }
}