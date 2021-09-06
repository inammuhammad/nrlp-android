package com.onelink.nrlp.android.features.login.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.math.BigInteger

data class LoginResponseModel(
    @SerializedName("message") val message: String,
    @SerializedName("token") val token: String,
    @SerializedName("user") val loginModel: LoginModel,
    @SerializedName("expires_in") val expiresIn: String,
    @SerializedName("in_activity_time") val inActivityTime: String,
    @SerializedName("session_key") val sessionKey: String
)

data class LoginModel(
    @SerializedName("id") val id: Int,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("nic_nicop") val cnicNicop: BigInteger,
    @SerializedName("mobile_no") val mobileNo: String,
    @SerializedName("resident_id") val residentId: String?,
    @SerializedName("passport_type") val passportType: String?,
    @SerializedName("passport_id") val passportId: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("user_type") val userType: String,
    @SerializedName("loyalty_level") val loyaltyLevel: String,
    @SerializedName("loyalty_points") val loyaltyPoints: BigDecimal
) {
    companion object {
        fun emptyObj() = LoginModel(
            2,
            "",
            "0".toBigInteger(),
            "0",
            "",
            "",
            "",
            "",
            "",
            "Bronze",
            "0".toBigDecimal()
        )
    }
}