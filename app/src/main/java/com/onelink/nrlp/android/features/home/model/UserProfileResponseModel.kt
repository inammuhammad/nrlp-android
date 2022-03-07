package com.onelink.nrlp.android.features.home.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.math.BigInteger

data class UserProfileResponseModel(@SerializedName("data") val profileModel: UserProfileModel)

data class UserProfileModel(
    @SerializedName("id") val id: Int,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("nic_nicop") val cnicNicop: BigInteger,
    @SerializedName("resident_id") val residentId: String?,
    @SerializedName("passport_type") val passportType: String,
    @SerializedName("passport_id") val passportId: String,
    @SerializedName("mobile_no") val mobileNo: String,
    @SerializedName("email") val email: String?,
    @SerializedName("user_type") val userType: String,
    @SerializedName("loyalty_level") val loyaltyLevel: String,
    @SerializedName("loyalty_points") val loyaltyPoints: BigDecimal,
    @SerializedName("usd_balance") val usdBalance: BigDecimal,
    @SerializedName("member_since") val memberSince: String,
    @SerializedName("redeemable_pkr") val redeemablePkr: String,
    @SerializedName("mother_maiden_name") val motherMaidenName: String,
    @SerializedName("place_of_birth") val placeOfBirth: String,
    @SerializedName("cnic_nicop_issuance_date") val cnicNicopIssuanceDate: String,
    @SerializedName("nadra_verified") val nadraVerified: String?,
    @SerializedName("require_nadra_verification") val requireNadraVerification: Boolean?,
    @SerializedName("receiver_count") val receiverCount: Int?

)