package com.onelink.nrlp.android.features.nrlpBenefits.viewmodel

import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.nrlpBenefits.model.RedemptionPartnerModel

/**
 * Created by Qazi Abubakar
 */
class NrlpBenefitsSharedViewModel : BaseViewModel() {
    val redemptionPartnerModel = MutableLiveData<RedemptionPartnerModel>()
}