package com.onelink.nrlp.android.data.local

import android.hardware.camera2.CameraCaptureSession
import java.math.BigDecimal
import java.math.BigInteger

data class UserModel(
    val token: String,
    val id: Int,
    var fullName: String,
    val residentId: String?,
    val passportType: String?,
    val passportId: String?,
    val cnicNicop: BigInteger,
    val mobileNo: String,
    val email: String?,
    val accountType: String,
    val loyaltyLevel: String,
    val loyaltyPoints: BigDecimal?,
    val sessionKey : String = "",
    val inActiveTime : Long = 0,
    val expiresIn : Long = 0,
    val usdBalance : BigDecimal?,
    val memberSince: String?,
    val redeemable_pkr: String?,
    val motherMaidenName: String?,
    val placeOfBirth: String?,
    val cnicNicopIssuanceDate: String?,
    val nadraVerified: String?,
    val requireNadraVerification: Boolean?,
    val receiverCount: Int?,
    val notificationCount: Int?
)