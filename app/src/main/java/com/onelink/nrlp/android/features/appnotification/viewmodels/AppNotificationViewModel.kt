package com.onelink.nrlp.android.features.appnotification.viewmodels

import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.appnotification.models.NotificationReadRequestModel
import com.onelink.nrlp.android.features.appnotification.models.NotificationsListRequestModel
import com.onelink.nrlp.android.features.appnotification.repo.PushNotificationRepo
import javax.inject.Inject

class AppNotificationViewModel @Inject constructor(private val notificationsRepo: PushNotificationRepo)
    : BaseViewModel() {

    fun getNotifications(request: NotificationsListRequestModel) {
        notificationsRepo.getNotifications(request)
    }

    fun deleteNotifications(request: NotificationReadRequestModel) {
        notificationsRepo.deleteNotification(request)
    }

    fun markNotificationRead(request: NotificationReadRequestModel) {
        notificationsRepo.markNotificationRead(request)
    }

    fun observeNotifications() = notificationsRepo.observeNotificationsResponse()

    fun observeNotificationRead() = notificationsRepo.observeNotificationReadResponse()

    fun observeNotificationDelete() = notificationsRepo.observeNotificationDelete()

    override fun onCleared() {
        super.onCleared()
        notificationsRepo.onClear()
    }
}