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
    @SerializedName("session_key") val sessionKey: String,
    @SerializedName("no_of_beneficiaries_allowed") val no_of_beneficiaries_allowed: Int
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
    @SerializedName("loyalty_points") val loyaltyPoints: BigDecimal,
    @SerializedName("usd_balance") val usdBalance: BigDecimal,
    @SerializedName("member_since") val memberSince: String?,
    @SerializedName("redeemable_pkr") val redeemablePkr: String?,
    @SerializedName("mother_maiden_name") val motherMaidenName: String?,
    @SerializedName("place_of_birth") val placeOfBirth: String?,
    @SerializedName("cnic_nicop_issuance_date") val cnicNicopIssuanceDate: String?,
    @SerializedName("nadra_verified") val nadraVerified: String?,
    @SerializedName("require_nadra_verification") val requireNadraVerification: Boolean?,
    @SerializedName("notification_count") val notification_count: Int?,
    @SerializedName("country") val country: String?,
    @SerializedName("father_name") val fatherName: String?,
    @SerializedName("nadra_status_code") val nadraStatus: String?

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
            "0".toBigDecimal(),
            "0".toBigDecimal(),
            "",
            "",
            "",
            "",
            "",
            "",
            false,
            0,
            "",
            "",
            ""
        )
    }
}