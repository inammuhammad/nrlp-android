package com.onelink.nrlp.android.features.viewStatement.models

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/**
 * Created by Qazi Abubakar on 17/07/2020.
 */
data class StatementDetailModel(
    @SerializedName("name") val name: String?,
    @SerializedName("status") val status: String,
    @SerializedName("type") val type: String?,
    @SerializedName("points") val points: BigDecimal,
    @SerializedName("date") val date: String,
    @SerializedName("transaction_id") val transactionId: String
)

enum class Type {
    Credit,
    Debit
}