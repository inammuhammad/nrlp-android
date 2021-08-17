package com.onelink.nrlp.android.features.changePassword.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.data.NetworkHelper
import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.changePassword.models.ChangePasswordRequest
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import javax.inject.Inject

open class ChangePasswordRepo @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val serviceGateway: ServiceGateway
) {

    val changePasswordResponse =
        MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()

    fun changePassword(changePasswordRequest: ChangePasswordRequest) {
        networkHelper.serviceCall(serviceGateway.changePassword(changePasswordRequest))
            .observeForever {
                changePasswordResponse.value = it
            }
    }

    fun observeChangePassword() =
        changePasswordResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun onClear() {
        networkHelper.dispose()
        networkHelper.disposable = null
    }
}
