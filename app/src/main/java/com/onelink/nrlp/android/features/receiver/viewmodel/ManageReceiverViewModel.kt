package com.onelink.nrlp.android.features.receiver.viewmodel

import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.receiver.repo.ReceiverRepo
import javax.inject.Inject


class ManageReceiverViewModel @Inject constructor(private val receiverRepo: ReceiverRepo) :
    BaseViewModel() {

    fun getAllReceivers() = receiverRepo.getAllReceivers()

    fun observeAllReceivers() = receiverRepo.observeReceiversListResponse()

    override fun onCleared() {
        receiverRepo.onClear()
        super.onCleared()
    }
}