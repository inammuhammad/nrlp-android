package com.onelink.nrlp.android.features.home.viewmodel

import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.home.repo.HomeRepo
import javax.inject.Inject

class HomeActivityViewModel @Inject constructor(private val homeRepo: HomeRepo) : BaseViewModel(){

    fun performLogout() = homeRepo.performLogout()

    fun inAppRatingComplete() = homeRepo.inAppRatingComplete()

    fun observeLogoutResponse() = homeRepo.observeLogoutResponse()

    fun observeInAppRating() = homeRepo.observeInAppRating()
}