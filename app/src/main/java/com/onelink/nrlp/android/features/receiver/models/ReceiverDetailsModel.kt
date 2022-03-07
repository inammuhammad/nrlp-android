package com.onelink.nrlp.android.features.receiver.models

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class ReceiverDetailsModel(
    @SerializedName("remitter_cnic") val remitterCnic: BigInteger,
    @SerializedName("receiver_cnic") val receiverCnic: BigInteger,
    @SerializedName("receiver_name") val receiverName: String,
    @SerializedName("receiver_mobile_no") val receiverMobileNumber: String,
    @SerializedName("rec_bank_name") val recBankName: String?,
    @SerializedName("rec_bank_iban") val recBankIban: String?,
    @SerializedName("link_status") val linkStatus: String,
    @SerializedName("link_date") val linkDate: String
)