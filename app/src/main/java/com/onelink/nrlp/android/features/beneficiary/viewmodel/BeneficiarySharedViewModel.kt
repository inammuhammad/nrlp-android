package com.onelink.nrlp.android.features.beneficiary.viewmodel

import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.models.BeneficiariesResponseModel
import com.onelink.nrlp.android.models.BeneficiaryDetailsModel

/**
 * Created by Qazi Abubakar on 11/07/2020.
 */
class BeneficiarySharedViewModel : BaseViewModel() {

    @Suppress("unused")
    val beneficiariesResponse = MutableLiveData<BaseResponse<BeneficiariesResponseModel>>()
    val beneficiaryDetails = MutableLiveData<BeneficiaryDetailsModel>()
    val isDeleteBeneficiary = MutableLiveData<Boolean>()
}