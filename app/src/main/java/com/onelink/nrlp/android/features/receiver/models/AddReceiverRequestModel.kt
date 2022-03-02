package com.onelink.nrlp.android.features.receiver.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Suppress("unused")
@Parcelize
class AddReceiverRequestModel (
    @SerializedName("nic_nicop")
    val nicNicop: String,
    @SerializedName("mobile_no")
    val mobileNo: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("mother_maiden_name")
    val motherMaidenName: String,
    @SerializedName("cnic_nicop_issuance_date")
    val cnicIssuanceDate: String,
    @SerializedName("place_of_birth")
    val placeOfBirth: String,
    @SerializedName("account_number_iban")
    val accountNumberIban: String?,
    @SerializedName("bank_name")
    val bankName: String?
): Parcelable