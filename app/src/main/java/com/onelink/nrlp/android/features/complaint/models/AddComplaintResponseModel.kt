package com.onelink.nrlp.android.features.complaint.models

import com.google.gson.annotations.SerializedName

data class AddComplaintResponseModel (
    @SerializedName("message") val message: String,
    @SerializedName("complaintId") val complaintId:String
    )