package com.onelink.nrlp.android.features.complaint.models

import com.google.gson.annotations.SerializedName

@Suppress("unused")
data class AddComplaintRequestModel(
    @SerializedName("c_registered")
    val Registered : Int,
    @SerializedName("c_user_type")
    val User_Type: String,
    @SerializedName("c_complaint_type_id")
    val Complaint_Type_Id : Int,
    @SerializedName("c_nic_nicop")
    val NIC_NICOP: String,
    @SerializedName("c_name")
    val Name: String,
    @SerializedName("c_mobile_no")
    val Mobile_No: String,
    @SerializedName("c_email")
    val Email: String,
    @SerializedName("c_country_of_residence")
    val Country_of_residence: String,
    @SerializedName("c_comments")
    val Comments: String=""
)