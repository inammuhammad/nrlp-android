package com.onelink.nrlp.android.features.home.viewmodel

import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.home.repo.HomeRepo
import javax.inject.Inject

class HomeActivityViewModel @Inject constructor(private val homeRepo: HomeRepo) : BaseViewModel(){

    fun performLogout() = homeRepo.performLogout()

    fun observeLogoutResponse() = homeRepo.observeLogoutResponse()
}