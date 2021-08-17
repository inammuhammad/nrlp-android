package com.onelink.nrlp.android.features.splash.viewmodel

import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.utils.SgTils
import javax.inject.Inject

class SplashViewModel @Inject constructor() : BaseViewModel() {
    fun clearUserData() {
        UserData.emptyUserData()
    }

    fun updateCheckSum(checkSum: String) {
        UserData.appChecksum = checkSum
    }
}