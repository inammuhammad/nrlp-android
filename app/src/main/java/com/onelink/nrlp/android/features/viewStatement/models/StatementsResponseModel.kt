package com.onelink.nrlp.android.features.viewStatement.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Qazi Abubakar on 17/07/2020.
 */
data class StatementsResponseModel(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: StatementsDataModel
)
