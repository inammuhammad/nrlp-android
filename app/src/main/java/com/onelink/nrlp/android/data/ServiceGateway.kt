@file:Suppress("unused")

package com.onelink.nrlp.android.data

import com.google.gson.JsonObject
import com.onelink.nrlp.android.features.appnotification.models.NotificationReadRequestModel
import com.onelink.nrlp.android.features.appnotification.models.NotificationsListRequestModel
import com.onelink.nrlp.android.features.appnotification.models.NotificationsListResponse
import com.onelink.nrlp.android.features.beneficiary.models.AddBeneficiaryRequestModel
import com.onelink.nrlp.android.features.beneficiary.models.DeleteBeneficiaryRequestModel
import com.onelink.nrlp.android.features.beneficiary.models.ResendBeneficiaryOtpRequestModel
import com.onelink.nrlp.android.features.beneficiary.models.UpdateBeneficiaryRequestModel
import com.onelink.nrlp.android.features.changePassword.models.ChangePasswordRequest
import com.onelink.nrlp.android.features.complaint.models.AddComplaintResponseModel
import com.onelink.nrlp.android.features.faqs.model.FaqsResponseModel
import com.onelink.nrlp.android.features.forgotPassword.models.ForgotPasswordOTPRequestModel
import com.onelink.nrlp.android.features.forgotPassword.models.ForgotPasswordRequestModel
import com.onelink.nrlp.android.features.forgotPassword.models.UpdatePasswordRequestModel
import com.onelink.nrlp.android.features.home.model.NadraDetailsRequestModel
import com.onelink.nrlp.android.features.home.model.UserProfileResponseModel
import com.onelink.nrlp.android.features.home.model.VerifyFatherNameRequestModel
import com.onelink.nrlp.android.features.login.model.LoginRequest
import com.onelink.nrlp.android.features.login.model.LoginResponseModel
import com.onelink.nrlp.android.features.managePoints.model.TransferPointsRequest
import com.onelink.nrlp.android.features.managePoints.model.TransferPointsResponseModel
import com.onelink.nrlp.android.features.nrlpBenefits.model.NrlpBenefitsResponseModel
import com.onelink.nrlp.android.features.nrlpBenefits.model.NrlpPartnerResponseModel
import com.onelink.nrlp.android.features.profile.models.ProfileResponseModel
import com.onelink.nrlp.android.features.rate.model.RateRedemptionRequestModel
import com.onelink.nrlp.android.features.receiver.models.AddReceiverRequestModel
import com.onelink.nrlp.android.features.receiver.models.DeleteReceiverRequestModel
import com.onelink.nrlp.android.features.receiver.models.ReceiversResponseModel
import com.onelink.nrlp.android.features.redeem.model.*
import com.onelink.nrlp.android.features.register.models.*
import com.onelink.nrlp.android.features.select.city.model.CitiesRequest
import com.onelink.nrlp.android.features.select.city.model.CitiesResponseModel
import com.onelink.nrlp.android.features.select.country.model.CountryCodeResponseModel
import com.onelink.nrlp.android.features.selfAwardPoints.model.SelfAwardPointsOTPRequestModel
import com.onelink.nrlp.android.features.selfAwardPoints.model.SelfAwardPointsResponseModel
import com.onelink.nrlp.android.features.splash.model.AuthResponseModel
import com.onelink.nrlp.android.features.uuid.model.UniqueIdentifierResendOTPRequest
import com.onelink.nrlp.android.features.uuid.model.UniqueIdentifierUpdateRequest
import com.onelink.nrlp.android.features.viewStatement.models.DetailedStatementRequestModel
import com.onelink.nrlp.android.features.viewStatement.models.LoyaltyStatementRequestModel
import com.onelink.nrlp.android.features.viewStatement.models.StatementsResponseModel
import com.onelink.nrlp.android.models.BeneficiariesResponseModel
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import com.onelink.nrlp.android.utils.HeaderConstants
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Umar Javed.
 */

interface ServiceGateway {


    @GET("app-key/")
    fun getAuthKey(
        @Header("user_type") userType: String,
        @Header(HeaderConstants.DEVICE_ID) deviceId: String,
        @Header(HeaderConstants.APPLICATION_VERSION) appVersion: String
    ): Single<Response<AuthResponseModel>>

    @POST("country-codes/")
    fun getCountryCodes(@Body request: CountryCodesRequest): Single<Response<CountryCodeResponseModel>>

    @PATCH("add-beneficiary-resend-code/")
    fun addBeneficiaryResendCode(@Body request: ResendBeneficiaryOtpRequestModel): Single<Response<GeneralMessageResponseModel>>

    @PATCH("update-beneficiary/")
    fun updateBeneficiary(@Body request:UpdateBeneficiaryRequestModel):Single<Response<GeneralMessageResponseModel>>

    @POST("cities/")
    fun getCities(@Body request: CitiesRequest): Single<Response<CitiesResponseModel>>

    @POST("login/")
    fun login(@Body request: LoginRequest): Single<Response<LoginResponseModel>>

    @POST("update-identifier/")
    fun uniqueIdentifierUpdate(@Body request: UniqueIdentifierUpdateRequest): Single<Response<GeneralMessageResponseModel>>

    @POST("update-identifier-resend-otp")
    fun uniqueIdentifierResendOTP(@Body body: UniqueIdentifierResendOTPRequest): Single<Response<GeneralMessageResponseModel>>

    @POST("verify-reference-no/")
    fun verifyReferenceNumber(@Body body: VerifyReferenceNumberRequest): Single<Response<GeneralMessageResponseModel>>

    @POST("self-award-validate-transaction/")
    fun verifySelfAwardPoints(@Body body: JsonObject): Single<Response<SelfAwardPointsResponseModel>>

    @POST("verify-registration-code/")
    fun verifyRegistrationCode(@Body body: VerifyRegistrationCodeRequest): Single<Response<GeneralMessageResponseModel>>

    @POST("verify-otp/")
    fun verifyOTP(@Body body: VerifyOTPRequest): Single<Response<GeneralMessageResponseModel>>

    @POST("resend-otp/")
    fun resendOTP(@Body body: ResendOTPRequest): Single<Response<GeneralMessageResponseModel>>

    @POST("register/")
    fun registerRemitter(@Body body: RegisterRemitterRequest): Single<Response<GeneralMessageResponseModel>>

    @POST("register/")
    fun registerBeneficiary(@Body body: RegisterBeneficiaryRequest): Single<Response<GeneralMessageResponseModel>>

    @POST("terms-conditions/")
    fun getTermsAndConditions(@Body body: TermsAndConditionsRequest): Single<Response<TermsAndConditionsResponseModel>>

    @POST("terms-conditions-cancel/")
    fun termsAndConditionsCancel(@Body body: TermsAndConditionsCancelRequest): Single<Response<GeneralMessageResponseModel>>

    @GET("list/")
    fun getBeneficiaries(): Single<Response<BeneficiariesResponseModel>>

    @POST("delete/")
    fun deleteBeneficiary(@Body body: DeleteBeneficiaryRequestModel): Single<Response<GeneralMessageResponseModel>>

    @POST("add/")
    fun addBeneficiary(@Body body: AddBeneficiaryRequestModel): Single<Response<GeneralMessageResponseModel>>

    @POST("loyalty-statement")
    fun getStatements(@Body loyaltyStatementRequestModel: LoyaltyStatementRequestModel): Single<Response<StatementsResponseModel>>

    @POST("loyalty-statement")
    fun getDetailedStatement(@Body body: DetailedStatementRequestModel): Single<Response<ResponseBody>>

    @POST("forgot-password")
    fun forgotPassword(@Body body: ForgotPasswordRequestModel): Single<Response<GeneralMessageResponseModel>>

    @POST("verify-forgot-password-otp")
    fun forgotPasswordOTP(@Body body: ForgotPasswordOTPRequestModel): Single<Response<GeneralMessageResponseModel>>

    @POST("self-award-verfiy-otp")
    fun selfAwardPointsOTP(@Body body: SelfAwardPointsOTPRequestModel): Single<Response<TransferPointsResponseModel>>

    @POST("resend-forget-password-otp")
    fun forgotPasswordResendOTP(@Body body: ForgotPasswordRequestModel): Single<Response<GeneralMessageResponseModel>>

    @POST("reset-password")
    fun updatePassword(@Body body: UpdatePasswordRequestModel): Single<Response<GeneralMessageResponseModel>>

    @POST("transfer-points")
    fun transferPoints(@Body body: TransferPointsRequest): Single<Response<TransferPointsResponseModel>>

    @GET("profile")
    fun getProfile(): Single<Response<ProfileResponseModel>>

    @GET("partners-categories")
    fun getRedemptionPartners(): Single<Response<RedeemPartnerResponseModel>>

    @POST("initialize-redemption-transaction")
    fun initializeRedemption(@Body body: InitializeRedeemRequestModel): Single<Response<RedeemInitializeResponseModel>>

    @POST("initialize-redemption-transaction")
    fun initializeRedemptionNADRA(@Body body: InitializeRedeemNadraRequestModel): Single<Response<RedeemInitializeFBRResponseModel>>

    @POST("initialize-redemption-transaction")
    fun initializeRedemptionPIA(@Body body: InitializeRedeemPIARequestModel): Single<Response<RedeemInitializeFBRResponseModel>>

    @POST("initialize-redemption-transaction")
    fun initializeRedemptionFBR(@Body body: InitializeRedeemFBRRequestModel): Single<Response<RedeemInitializeFBRResponseModel>>

    @POST("initialize-redemption-transaction")
    fun initializeRedemptionOPF(@Body body: InitializeRedeemOPFRequestModel): Single<Response<RedeemInitializeFBRResponseModel>>

    @POST("initialize-redemption-transaction")
    fun initializeRedemptionFBROTP(@Body body: InitializeRedeemFBROTPRequestModel): Single<Response<RedeemInitializeFBROTPResponseModel>>

    @POST("initialize-redemption-transaction")
    fun initializeRedemptionPIAOTP(@Body body: InitializeRedeemPIAOTPRequestModel): Single<Response<RedeemInitializeFBROTPResponseModel>>

    @POST("initialize-redemption-transaction")
    fun initializeRedemptionPassportOTP(@Body body: InitializeRedeemPassportOTPRequestModel): Single<Response<RedeemInitializeFBROTPResponseModel>>

    @POST("initialize-redemption-transaction")
    fun initializeRedemptionNadraOTP(@Body body: InitializeRedeemNadraOTPRequestModel): Single<Response<RedeemInitializeFBROTPResponseModel>>

    @POST("initialize-redemption-transaction")
    fun initializeRedemptionOPFOTP(@Body body: InitializeRedeemOPFOTPRequestModel): Single<Response<RedeemInitializeFBROTPResponseModel>>

    @POST("redeem-transaction-verify-otp")
    fun verifyRedeemOTP(@Body body: VerifyRedeemOTPRequestModel): Single<Response<GeneralMessageResponseModel>>

    @POST("redemption-transaction-resend-otp")
    fun redeemResendOTP(@Body body: RedeemResendOTPRequestModel): Single<Response<GeneralMessageResponseModel>>

    @POST("complete-redeem-transaction")
    fun completeRedemption(@Body body: RedeemCompletionRequestModel): Single<Response<RedeemSuccessResponseModel>>

    @POST("complete-redeem-transaction")
    fun completeRedemptionFBR(@Body body: RedeemCompletionFBRRequestModel): Single<Response<RedeemFBRSuccessResponseModel>>

    @GET("get-faqs")
    fun getFaqs(): Single<Response<FaqsResponseModel>>

    @POST("change-password")
    fun changePassword(@Body body: ChangePasswordRequest): Single<Response<GeneralMessageResponseModel>>

    @GET("profile")
    fun getUserProfile(): Single<Response<UserProfileResponseModel>>

    @GET("logout/")
    fun performLogout(): Single<Response<GeneralMessageResponseModel>>

    @POST("update-profile-validate/")
    fun validateProfileUpdate(@Body jsonObject: JsonObject) : Single<Response<GeneralMessageResponseModel>>

    @POST("update-profile/")
    fun updateProfile(@Body jsonObject: JsonObject) : Single<Response<GeneralMessageResponseModel>>

    @POST("update-profile-send-otp")
    fun updateProfileMobile(@Body jsonObject: JsonObject): Single<Response<GeneralMessageResponseModel>>

    @POST("update-profile-verify-otp")
    fun updateProfileVerifyOtp(@Body body: JsonObject): Single<Response<GeneralMessageResponseModel>>

    @POST("update-profile-resend-otp")
    fun updateProfileResendOtp(@Body body: JsonObject): Single<Response<GeneralMessageResponseModel>>

    @GET("nrlp-benefits")
    fun getNrlpBenefits(): Single<Response<NrlpPartnerResponseModel>>

    @GET("nrlp-benefit/")
    fun getNrlpPartnerBenefits(@Query("partner_id") partnerId: Int): Single<Response<NrlpBenefitsResponseModel>>

    @POST("verify-at-nadra/")
    fun updateNadraDetails(@Body body: NadraDetailsRequestModel): Single<Response<GeneralMessageResponseModel>>

    @POST("add-complaint/")
    fun addComplaint(@Body body:JsonObject):Single<Response<AddComplaintResponseModel>>

    @POST("remitter-receiver-add/")
    fun addReceiver(@Body body: AddReceiverRequestModel): Single<Response<GeneralMessageResponseModel>>

    @GET("remitter-receiver-list/")
    fun getReceiversList(): Single<Response<ReceiversResponseModel>>

    @POST("remitter-receiver-delete/")
    fun deleteReceiver(@Body body: DeleteReceiverRequestModel): Single<Response<GeneralMessageResponseModel>>

    @GET("rr-bank-and-exchange/")
    fun getBanks(): Single<Response<com.onelink.nrlp.android.features.select.bank.model.BanksListResponse>>

    @POST("nrlp-redemption-rating/")
    fun rateRedemption(@Body body: JsonObject): Single<Response<GeneralMessageResponseModel>>

    @POST("nrlp-notification-list/")
    fun getUserNotifications(@Body body: NotificationsListRequestModel): Single<Response<NotificationsListResponse>>

    @POST("nrlp-notification-read/")
    fun markNotificationRead(@Body body: NotificationReadRequestModel): Single<Response<GeneralMessageResponseModel>>

    @POST("nrlp-notification-delete/")
    fun deleteNotification(@Body body: NotificationReadRequestModel): Single<Response<GeneralMessageResponseModel>>

    @POST("nrlpfather-verification/")
    fun verifyFatherName(@Body body: VerifyFatherNameRequestModel): Single<Response<GeneralMessageResponseModel>>

    @POST("nrlpappcancel-rating/")
    fun inAppRatingCancel(): Single<Response<GeneralMessageResponseModel>>

    @POST("nrlpappregistrationrating/")
    fun rateRegistration(@Body body: JsonObject): Single<Response<GeneralMessageResponseModel>>

}
