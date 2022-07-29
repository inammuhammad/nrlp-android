package com.onelink.nrlp.android.features.receiver.viewmodel

import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.home.repo.HomeRepo
import javax.inject.Inject

class ReceiverViewModel @Inject constructor(private val homeRepo: HomeRepo) : BaseViewModel() {
    fun performLogout() = homeRepo.performLogout()
    fun observeLogout() = homeRepo.observeLogoutResponse()
}