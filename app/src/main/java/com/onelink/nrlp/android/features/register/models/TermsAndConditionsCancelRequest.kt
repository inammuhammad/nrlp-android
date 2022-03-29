package com.onelink.nrlp.android.features.register.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.onelink.nrlp.android.utils.LukaKeRakk
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TermsAndConditionsCancelRequest (
    @SerializedName("nic_nicop")
    var nicNicop: String,
    @SerializedName("user_type")
    var userType: String,
    @SerializedName("version_no")
    var versionNum: String,
    @SerializedName("term_condition_id")
    val termsAndConditionsId: Int,
    @SerializedName("encryption_key")
    val encryptionKey: String = LukaKeRakk.kcth()
): Parcelable