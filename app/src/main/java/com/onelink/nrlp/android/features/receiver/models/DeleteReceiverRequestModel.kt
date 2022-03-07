package com.onelink.nrlp.android.features.receiver.models

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

class DeleteReceiverRequestModel(
    @SerializedName("nic_nicop")
    var nicNicop: String?
)