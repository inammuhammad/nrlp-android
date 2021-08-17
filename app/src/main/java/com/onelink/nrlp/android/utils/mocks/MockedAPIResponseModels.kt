package com.onelink.nrlp.android.utils.mocks

import com.google.gson.Gson
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.features.language.model.LanguageResponseModel
import com.onelink.nrlp.android.features.profile.models.ProfileResponseModel
import com.onelink.nrlp.android.features.redeem.model.RedeemInitializeResponseModel
import com.onelink.nrlp.android.features.redeem.model.RedeemPartnerResponseModel
import com.onelink.nrlp.android.features.redeem.model.RedeemSuccessResponseModel
import com.onelink.nrlp.android.models.GeneralMessageResponseModel

@Suppress("unused")
class MockedAPIResponseModels {
    companion object {

        private const val mockedBeneficiaryDeleteJsonString =
            "{\"message\": \"Beneficiary deleted successfully\"}"
        private const val mockedVerifyOTPString = "{\"message\": \"otp verified\"}"
        private const val mockedResendOTPString = "{\"message\": \"otp resent\"}"
        private const val mockedProfileJsonString =
            "{\"message\": \"success\", \"data\": { \"full_name\": \"Ahmad Khan\", \"nic_nicop\": \"4422335566889\", \"mobile_no\": \"+923125234587\" }}"
        private const val mockedInitializeRedeemString =
            "{\"message\": \"OTP send to your mobile number\", \"data\": { \"transaction_id\": \"1a2B3c4D\" }}"
        private const val mockedRedeemSuccessString =
            "{\"message\": \"Redemption successfully completed\", \"data\": { \"transaction_id\": \"1a2B3c4D\" }} "
        private const val mockedRedemptionPartnersString =
            "{\"message\": \"success\", \"data\": [{ \"id\": 1, \"partner_name\": \"Pakistan International Airline\", \"categories\": [{ \"id\": 1, \"category_name\": \"Category 1\", \"points\": 500},{ \"id\": 2, \"category_name\": \"Category 2\", \"points\": 250} ]},{ \"id\": 2, \"partner_name\": \"NADRA\", \"categories\": [{ \"id\": 1, \"category_name\": \"Category 1\", \"points\": 800},{ \"id\": 2, \"category_name\": \"Category 2\", \"points\": 2500} ]},{ \"id\": 3, \"partner_name\": \"Pakistan Post Office\", \"categories\": [{ \"id\": 1, \"category_name\": \"Category 1\", \"points\": 5000},{ \"id\": 2, \"category_name\": \"Category 2\", \"points\": 9250} ]},{ \"id\": 4, \"partner_name\": \"Civil Aviation Authority\", \"categories\": [{ \"id\": 1, \"category_name\": \"Category 3\", \"points\": 8000},{ \"id\": 2, \"category_name\": \"Category 4\", \"points\": 150}]}]}"
        private const val mockedLanguageString =
            """{"data" : [{"language_name" :"English","id":"1" },{"language_name" : "Urdu",  "id":"2" }]}"""

        fun getMockedVerifyOTPAPIResponse() = BaseResponse.success(
            Gson().fromJson(
                mockedVerifyOTPString,
                GeneralMessageResponseModel::class.java
            ), code = 200
        )

        fun getMockedResendOTPAPIResponse() = BaseResponse.success(
            Gson().fromJson(
                mockedResendOTPString,
                GeneralMessageResponseModel::class.java
            ), code = 200
        )

        fun getMockedBeneficiaryDeleteResponse() = BaseResponse.success(
            Gson().fromJson(
                mockedBeneficiaryDeleteJsonString,
                GeneralMessageResponseModel::class.java
            ), code = 200
        )

        fun getMockedProfileResponse() = BaseResponse.success(
            Gson().fromJson(
                mockedProfileJsonString,
                ProfileResponseModel::class.java
            ), code = 200
        )

        fun getRedemptionPartnersResponse() = BaseResponse.success(
            Gson().fromJson(
                mockedRedemptionPartnersString,
                RedeemPartnerResponseModel::class.java
            ), code = 200
        )

        fun getRedemptionInitializeResponse() = BaseResponse.success(
            Gson().fromJson(
                mockedInitializeRedeemString,
                RedeemInitializeResponseModel::class.java
            ), code = 200
        )

        fun getRedemptionSuccessResponse() = BaseResponse.success(
            Gson().fromJson(
                mockedRedeemSuccessString,
                RedeemSuccessResponseModel::class.java
            ), code = 200
        )

        fun getLanguageApiResposne() = BaseResponse.success(
            Gson().fromJson(
                mockedLanguageString,
                LanguageResponseModel::class.java
            ), code = 200
        )
    }
}
