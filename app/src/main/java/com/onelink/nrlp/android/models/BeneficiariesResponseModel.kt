package com.onelink.nrlp.android.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Qazi Abubakar on 11/07/2020.
 */
data class BeneficiariesResponseModel(@SerializedName("data") val data: ArrayList<BeneficiaryDetailsModel>)
