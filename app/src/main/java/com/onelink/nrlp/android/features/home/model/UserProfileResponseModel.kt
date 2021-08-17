package com.onelink.nrlp.android.features.home.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.math.BigInteger

data class UserProfileResponseModel(@SerializedName("data") val profileModel: UserProfileModel)

data class UserProfileModel(
    @SerializedName("id") val id: Int,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("nic_nicop") val cnicNicop: BigInteger,
    @SerializedName("mobile_no") val mobileNo: String,
    @SerializedName("email") val email: String?,
    @SerializedName("user_type") val userType: String,
    @SerializedName("loyalty_level") val loyaltyLevel: String,
    @SerializedName("loyalty_points") val loyaltyPoints: BigDecimal
)