package com.onelink.nrlp.android.features.register.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Qazi Abubakar on 09/07/2020.
 */
data class RegisterFlowDataModel(
    @SerializedName("fullName")
    var fullName: String,
    @SerializedName("CnicNicop")
    var cnicNicop: String,
    @SerializedName("phoneNumber")
    var phoneNumber: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("country")
    var country: String,

    @SerializedName("residentId")
    var residentId: String?,
    @SerializedName("passportType")
    var passportType: String?,
    @SerializedName("passportId")
    var passportId: String?,

    @SerializedName("password")
    var password: String,
    @SerializedName("rePassword")
    var rePassword: String,
    @SerializedName("accountType")
    var accountType: String,
    @SerializedName("referenceNumber")
    var referenceNumber: String,
    @SerializedName("transactionAmount")
    var transactionAmount: String,
    @SerializedName("registrationCode")
    var registrationCode: String,
    @SerializedName("otpCode")
    var otpCode: String
)
