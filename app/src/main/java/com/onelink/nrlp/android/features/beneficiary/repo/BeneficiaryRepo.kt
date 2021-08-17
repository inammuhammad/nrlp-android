package com.onelink.nrlp.android.features.beneficiary.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.data.NetworkHelper
import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.beneficiary.models.AddBeneficiaryRequestModel
import com.onelink.nrlp.android.features.beneficiary.models.DeleteBeneficiaryRequestModel
import com.onelink.nrlp.android.models.BeneficiariesResponseModel
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import javax.inject.Inject

/**
 * Created by Qazi Abubakar on 11/07/2020.
 */
open class BeneficiaryRepo @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val serviceGateway: ServiceGateway
) {
    val beneficiariesResponse =
        MutableLiveData<BaseResponse<BeneficiariesResponseModel>>()
    val beneficiaryDeleteResponse =
        MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()
    val beneficiaryAddResponse =
        MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()

    fun getAllBeneficiaries() {
        networkHelper.serviceCall(serviceGateway.getBeneficiaries()).observeForever {
            beneficiariesResponse.value = it
        }
    }

    fun observeAllBeneficiaries() =
        beneficiariesResponse as LiveData<BaseResponse<BeneficiariesResponseModel>>

    fun deleteBeneficiary(deleteBeneficiaryRequestModel: DeleteBeneficiaryRequestModel) {
        networkHelper.serviceCall(serviceGateway.deleteBeneficiary(deleteBeneficiaryRequestModel))
            .observeForever {
                beneficiaryDeleteResponse.value = it
            }
    }

    fun observeBeneficiaryDeleteResponse() =
        beneficiaryDeleteResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun addBeneficiary(addBeneficiaryRequestModel: AddBeneficiaryRequestModel) {
        networkHelper.serviceCall(serviceGateway.addBeneficiary(addBeneficiaryRequestModel))
            .observeForever {
                beneficiaryAddResponse.value = it
            }
    }

    fun observeBeneficiaryAddResponse() =
        beneficiaryAddResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun onClear() {
        networkHelper.dispose()
        networkHelper.disposable = null
    }
}