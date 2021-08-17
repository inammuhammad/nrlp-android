package com.onelink.nrlp.android.features.viewStatement.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Qazi Abubakar
 */
data class LoyaltyStatementRequestModel(
    @SerializedName("from_date")
    val fromDate: String?,
    @SerializedName("to_date")
    val toDate: String?
)