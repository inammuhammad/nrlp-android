package com.onelink.nrlp.android.features.viewStatement.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Qazi Abubakar on 17/07/2020.
 */
data class StatementsDataModel(
    @SerializedName("currentpointbalance") val currentpointbalance: Int,
    @SerializedName("statements") val statements: ArrayList<StatementDetailModel>
)
