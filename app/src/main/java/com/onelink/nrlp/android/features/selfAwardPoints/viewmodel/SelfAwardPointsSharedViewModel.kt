package com.onelink.nrlp.android.features.selfAwardPoints.viewmodel

import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.selfAwardPoints.model.SelfAwardPointsRequest


class SelfAwardPointsSharedViewModel: BaseViewModel() {
    var selfAwardPointsRequestModel = MutableLiveData<SelfAwardPointsRequest>()

    fun setSelfAwardPointsFlowDataModel(it: SelfAwardPointsRequest) {
        selfAwardPointsRequestModel.value = it
    }

}