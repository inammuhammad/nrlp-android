package com.onelink.nrlp.android.features.register.viewmodel

import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.register.models.RegisterFlowDataModel

/**
 * Created by Qazi Abubakar on 08/07/2020.
 */
class SharedViewModel : BaseViewModel() {
    val maxProgress = MutableLiveData<Int>()
    val startRegisterSuccessActivity = MutableLiveData<Boolean>(false)

    var registerFlowDataModel = MutableLiveData<RegisterFlowDataModel>()

    fun setRegisterFlowDataModel(it: RegisterFlowDataModel) {
        registerFlowDataModel.value = it
    }
}