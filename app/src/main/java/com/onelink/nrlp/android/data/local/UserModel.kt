package com.onelink.nrlp.android.data.local

import android.hardware.camera2.CameraCaptureSession
import java.math.BigDecimal
import java.math.BigInteger

data class UserModel(
    val token: String,
    val id: Int,
    val fullName: String,
    val cnicNicop: BigInteger,
    val mobileNo: String,
    val email: String?,
    val accountType: String,
    val loyaltyLevel: String,
    val loyaltyPoints: BigDecimal?,
    val sessionKey : String = "",
    val inActiveTime : Long = 0,
    val expiresIn : Long = 0
)