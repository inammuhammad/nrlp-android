package com.onelink.nrlp.android.features.appnotification.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.data.NetworkHelper
import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.appnotification.models.NotificationReadRequestModel
import com.onelink.nrlp.android.features.appnotification.models.NotificationsListRequestModel
import com.onelink.nrlp.android.features.appnotification.models.NotificationsListResponse
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import javax.inject.Inject

open class PushNotificationRepo @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val serviceGateway: ServiceGateway
) {
    val userNotificationsResponse = MutableLiveData<BaseResponse<NotificationsListResponse>>()
    val userNotificationReadResponse = MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()
    val deleteNotificationResponse = MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()

    fun getNotifications(request: NotificationsListRequestModel){
        networkHelper.serviceCall(serviceGateway.getUserNotifications(request))
            .observeForever { response ->
                userNotificationsResponse.value = response
            }
    }

    fun markNotificationRead(request: NotificationReadRequestModel) {
        networkHelper.serviceCall(serviceGateway.markNotificationRead(request))
            .observeForever { response ->
                userNotificationReadResponse.value = response
            }
    }

    fun deleteNotification(request: NotificationReadRequestModel) {
        networkHelper.serviceCall(serviceGateway.deleteNotification(request))
            .observeForever { response ->
                userNotificationReadResponse.value = response
            }
    }

    fun observeNotificationsResponse() =
        userNotificationsResponse as LiveData<BaseResponse<NotificationsListResponse>>

    fun observeNotificationReadResponse() =
        userNotificationReadResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun observeNotificationDelete() =
        deleteNotificationResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun onClear() {
        networkHelper.dispose()
        networkHelper.disposable = null
    }
}