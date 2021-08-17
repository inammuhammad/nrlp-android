package com.onelink.nrlp.android.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Qazi Abubakar
 */
@Parcelize
open class LoginCredentials(val cnic: String, val password: String, val accountType: String) :
    Parcelable