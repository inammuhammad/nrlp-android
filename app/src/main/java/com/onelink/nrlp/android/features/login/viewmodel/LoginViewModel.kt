package com.onelink.nrlp.android.features.login.viewmodel

import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.data.local.UserData
import javax.inject.Inject

class LoginViewModel @Inject constructor() : BaseViewModel() {
    fun clearUserData() {
        UserData.emptyUserData()
    }
}