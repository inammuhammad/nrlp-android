package com.onelink.nrlp.android.features.receiver.viewmodel

import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.home.repo.HomeRepo
import com.onelink.nrlp.android.features.receiver.models.ReceiverDetailsModel
import com.onelink.nrlp.android.models.BeneficiariesResponseModel
import javax.inject.Inject

/**
 * Created by Qazi Abubakar on 11/07/2020.
 */
class ReceiverSharedViewModel @Inject constructor(private val homeRepo: HomeRepo) :
    BaseViewModel() {

    @Suppress("unused")
    val beneficiariesResponse = MutableLiveData<BaseResponse<BeneficiariesResponseModel>>()
    val receiverDetails = MutableLiveData<ReceiverDetailsModel>()
    val isDeleteBeneficiary = MutableLiveData<Boolean>()
    val receiverType = MutableLiveData<String>()
}