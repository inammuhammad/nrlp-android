package com.onelink.nrlp.android.features.redeem.model

import com.google.gson.annotations.SerializedName

data class RedeemInitializeFBRResponseModel (
    @SerializedName("message") val message: String,
    @SerializedName("transaction_id") val transactionId: String,
    @SerializedName("bill_inquiry_response") val billInquiryResponse: BillInquiryResponseModel,
    @SerializedName("inquiryMessage") val inquiryMessage: String,
)

data class BillInquiryResponseModel(
    @SerializedName("AmountAfterDueDate") val amountAfterDueDate: String,
    @SerializedName("AmountWithinDueDate") val amountWithinDueDate: String,
    @SerializedName("BillStatus") val billStatus: String,
    @SerializedName("BillingMonth") val billingMonth: String,
    @SerializedName("ChannelID") val channelId: String,
    @SerializedName("ClientID") val clientID: String,
    @SerializedName("CompanyCode") val companyCode: String,
    @SerializedName("ConsumerNo") val consumerNo: String,
    @SerializedName("CustomerName") val customerName: String,
    @SerializedName("DueDate") val dueDate: String,
    @SerializedName("RRN") val rrn: String,
    @SerializedName("ResponseCode") val responseCode: String,
    @SerializedName("ResponseDetail") val responseDetail: String,
    @SerializedName("STAN") val stan: String,
    @SerializedName("Signature") val signature: String,
)