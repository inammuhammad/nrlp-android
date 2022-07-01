package com.onelink.nrlp.android.repos.viewstatement

import com.onelink.nrlp.android.features.viewStatement.models.*
import com.onelink.nrlp.android.models.GeneralMessageResponseModel

fun getLoyaltyStatementRequest() = LoyaltyStatementRequestModel(
    "2982094823",
    "2039481230948"
)

fun getStatementsResponse() = StatementsResponseModel(
    "Hello asfkajsflka",
    StatementsDataModel(
        1,
        arrayListOf(
            StatementDetailModel(
                "Hamza",
                "mango",
                "Credit",
                123.toBigDecimal(),
                "190238102",
                "456"
            )
        )
    )
)

fun getDetailedStatementRequest() = DetailedStatementRequestModel(
    "humxa@gmail.com",
    "123120398",
    "1290830182"
)

fun getDetailsStatementResponse() = GeneralMessageResponseModel(
    "Get details Success"
)