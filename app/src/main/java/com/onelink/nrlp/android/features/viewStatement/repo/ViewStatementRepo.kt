package com.onelink.nrlp.android.features.viewStatement.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.data.NetworkHelper
import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.features.viewStatement.models.DetailedStatementRequestModel
import com.onelink.nrlp.android.features.viewStatement.models.LoyaltyStatementRequestModel
import com.onelink.nrlp.android.features.viewStatement.models.StatementsResponseModel
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import com.onelink.nrlp.android.utils.mocks.MockedAPIResponseModels
import javax.inject.Inject


/**
 * Created by Qazi Abubakar on 14/07/2020.
 */
open class ViewStatementRepo @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val serviceGateway: ServiceGateway
) {
    val statementsResponse =
        MutableLiveData<BaseResponse<StatementsResponseModel>>()
    val detailedStatementResponse =
        MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()

    fun getStatements(loyaltyStatementRequestModel: LoyaltyStatementRequestModel) {
        networkHelper.serviceCall(serviceGateway.getStatements(loyaltyStatementRequestModel))
            .observeForever {
                statementsResponse.value = it
            }
    }

    fun observeStatements() =
        statementsResponse as LiveData<BaseResponse<StatementsResponseModel>>

    fun getDetailedStatements(detailedStatementRequestModel: DetailedStatementRequestModel) {
         networkHelper.serviceCall(
             serviceGateway.getDetailedStatement(detailedStatementRequestModel)
         ).observeForever {
             detailedStatementResponse.value = it
         }
    }

    fun observeDetailedStatement() =
        detailedStatementResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>
}