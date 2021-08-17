package com.onelink.nrlp.android.features.home.fragments

import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.home.repo.HomeRepo
import javax.inject.Inject

class HomeFragmentViewModel @Inject constructor(private val homeRepo: HomeRepo) : BaseViewModel() {

    fun getUserProfile() = homeRepo.getUserProfile()

    fun observeUserProfile() = homeRepo.observeUserProfile()

    override fun onCleared() {
        homeRepo.onClear()
        super.onCleared()
    }
}
