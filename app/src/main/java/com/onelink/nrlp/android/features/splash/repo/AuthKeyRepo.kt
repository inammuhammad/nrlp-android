package com.onelink.nrlp.android.features.splash.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.data.NetworkHelper
import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.utils.AESEncryptionHelper
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.features.splash.model.AuthResponseModel
import com.onelink.nrlp.android.utils.IdentityKeyUtils
import com.onelink.nrlp.android.utils.LukaKeRakk
import java.util.*
import javax.inject.Inject

open class AuthKeyRepo @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val serviceGateway: ServiceGateway
) {
    private val authResponse = MutableLiveData<BaseResponse<AuthResponseModel>>()

    fun getAuthKey(accountType: String, nic: String) {
        storeIdentityKey(nic)

        networkHelper.serviceCall(
            serviceGateway.getAuthKey(
                accountType.toLowerCase(Locale.getDefault()),
                "",
                UserData.appChecksum ?: ""
            )
        )
            .observeForever {
                authResponse.value = it
                storeDecryptedIV()
            }
    }

    private fun storeDecryptedIV() {
        UserData.finalEncryptionIV =
            AESEncryptionHelper.decrypt(
                authResponse.value?.data?.data?.key ?: "",
                LukaKeRakk.kcon().take(32),
                LukaKeRakk.kcon().takeLast(16)
            )
    }

    private fun storeIdentityKey(nic: String) {
        UserData.identityKey = IdentityKeyUtils.generateKey(nic.toCharArray())
    }

    fun observeAuthKey() = authResponse as LiveData<BaseResponse<AuthResponseModel>>

    fun onClear() {
        networkHelper.dispose()
        networkHelper.disposable = null
    }


}
