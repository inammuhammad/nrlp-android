package com.onelink.nrlp.android.features.selfAwardPoints.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.selfAwardPoints.model.SelfAwardPointsRequest
import com.onelink.nrlp.android.utils.SelfAwardRequestConstants


class SelfAwardPointsSharedViewModel: BaseViewModel() {
    var selfAwardRowId = MutableLiveData<String>()
    var selfAwardPointsRequestModel= MutableLiveData<JsonObject>()

   fun setSelfAwardRowIdModel(it:String){
       selfAwardRowId.value=it
       selfAwardPointsRequestModel.value?.addProperty(
           SelfAwardRequestConstants.Self_Award_Row_ID,
           it
       )
   }

    fun setSelfAwardPointsFlowDataModel(it:JsonObject){
        selfAwardPointsRequestModel.value=it
    }

}