package com.onelink.nrlp.android.features.forgotPassword.viewmodel

import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.forgotPassword.models.ForgotPasswordFlowDataModel
import com.onelink.nrlp.android.features.login.repo.LoginRepo
import com.onelink.nrlp.android.features.splash.repo.AuthKeyRepo
import java.util.*
import javax.inject.Inject

/**
 * Created by Qazi Abubakar on 16/07/2020.
 */
class ForgotPasswordSharedViewModel: BaseViewModel() {
    var forgotPasswordFlowDataModel = MutableLiveData<ForgotPasswordFlowDataModel>()

    fun setForgotPasswordFlowDataModel(it: ForgotPasswordFlowDataModel) {
        forgotPasswordFlowDataModel.value = it
    }

}