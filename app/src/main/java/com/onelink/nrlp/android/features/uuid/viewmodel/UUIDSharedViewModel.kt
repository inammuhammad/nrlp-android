package com.onelink.nrlp.android.features.uuid.viewmodel

import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.models.LoginCredentials
import javax.inject.Inject

open class UUIDSharedViewModel @Inject constructor() : BaseViewModel(){
    var loginCredentials = MutableLiveData<LoginCredentials>()

    fun setLoginCredentialsModel(it: LoginCredentials) {
        loginCredentials.postValue(it)
    }

}