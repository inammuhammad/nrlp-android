package com.onelink.nrlp.android.features.redeem.viewmodels

import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.redeem.model.RedeemCategoryModel
import com.onelink.nrlp.android.features.redeem.model.RedeemPartnerModel
import javax.inject.Inject

class RedeemSharedViewModel @Inject constructor() : BaseViewModel() {
    var redeemPartnerModel = MutableLiveData<RedeemPartnerModel>()
    var redeemCategoryModel = MutableLiveData<RedeemCategoryModel>()
    var transactionId = MutableLiveData<String>()
    var amount = MutableLiveData<String>()
    var psid = MutableLiveData<String>()

    fun setRedeemPartnerModel(it: RedeemPartnerModel) {
        redeemPartnerModel.postValue(it)
    }

    fun setRedeemCategoryModel(it: RedeemCategoryModel) {
        redeemCategoryModel.postValue(it)
    }

    fun setTransactionId(it: String) {
        transactionId.postValue(it)
    }

    fun setAmount(it: String) {
        amount.postValue(it)
    }

    fun setPSID(it: String) {
        psid.postValue(it)
    }
}