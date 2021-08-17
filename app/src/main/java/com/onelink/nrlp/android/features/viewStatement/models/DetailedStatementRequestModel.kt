package com.onelink.nrlp.android.features.viewStatement.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Qazi Abubakar on 11/07/2020.
 */

class DetailedStatementRequestModel(
    @SerializedName("email")
    val email: String,
    @SerializedName("from_date")
    val fromDate: String?,
    @SerializedName("to_date")
    val toDate: String?
)