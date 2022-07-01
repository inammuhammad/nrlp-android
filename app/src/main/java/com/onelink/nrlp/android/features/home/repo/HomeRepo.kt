package com.onelink.nrlp.android.features.home.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.data.NetworkHelper
import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.data.local.UserModel
import com.onelink.nrlp.android.features.home.model.*
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import javax.inject.Inject

class HomeRepo @Inject constructor(
    private val networkHelper: NetworkHelper, private val serviceGateway: ServiceGateway
) {

    val profileResponse = MutableLiveData<BaseResponse<UserProfileResponseModel>>()
    val logoutResponse = MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()
    val updateNadraDataResponse = MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()
    val verifyFatherNameResponse = MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()
    val inAppRatingResponse = MutableLiveData<BaseResponse<GeneralMessageResponseModel>>()
    val popupMessageResponse = MutableLiveData<BaseResponse<PopupResponseModel>>()

    fun getUserProfile() {
        networkHelper.serviceCall(serviceGateway.getUserProfile()).observeForever {
            it.data?.let { userProfileResponseModel ->
                persistUser(userProfileResponseModel.profileModel)
            }
            profileResponse.value = it
        }
    }

    fun updateNadraDetails(updateNadraDetailsRequest: NadraDetailsRequestModel){
        networkHelper.serviceCall(serviceGateway.updateNadraDetails(updateNadraDetailsRequest))
            .observeForever { response ->
                updateNadraDataResponse.value = response
            }
    }

    fun verifyFatherName(request: VerifyFatherNameRequestModel) {
        networkHelper.serviceCall(serviceGateway.verifyFatherName(request))
            .observeForever { response ->
                verifyFatherNameResponse.value = response
            }
    }

    fun inAppRatingComplete() {
        networkHelper.serviceCall(serviceGateway.inAppRatingCancel())
            .observeForever { response ->
                inAppRatingResponse.value = response
            }
    }

    fun getPopupMessage(popupMessageRequest: PopupMessageRequest) {
        networkHelper.serviceCall(serviceGateway.getPopupMessage(popupMessageRequest))
            .observeForever { response ->
                popupMessageResponse.value = response
            }
    }

    private fun persistUser(userProfileModel: UserProfileModel) {
        UserData.setUser(getUserModelFromLoginResponse(userProfileModel))
    }

    fun observeUserProfile() = profileResponse as LiveData<BaseResponse<UserProfileResponseModel>>

    fun observeUpdateNadra() =
        updateNadraDataResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun observeVerifyFatherNameResponse() =
        verifyFatherNameResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun observeInAppRating() =
        inAppRatingResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun observePopupMessage() =
        popupMessageResponse as LiveData<BaseResponse<PopupResponseModel>>

    private fun getUserModelFromLoginResponse(userProfileModel: UserProfileModel): UserModel {
        return UserModel(
            token = UserData.getUser()?.token ?: "",
            id = userProfileModel.id,
            fullName = userProfileModel.fullName,
            residentId = userProfileModel.residentId ?: "",
            passportType = userProfileModel.passportType,
            passportId = userProfileModel.passportId,
            cnicNicop = userProfileModel.cnicNicop,
            mobileNo = userProfileModel.mobileNo,
            email = userProfileModel.email,
            accountType = userProfileModel.userType,
            loyaltyLevel = userProfileModel.loyaltyLevel,
            loyaltyPoints = userProfileModel.loyaltyPoints,
            sessionKey = UserData.getUser()?.sessionKey ?: "",
            inActiveTime = UserData.getUser()?.inActiveTime ?: 1000,
            expiresIn = UserData.getUser()?.expiresIn ?: 1000,
            usdBalance = userProfileModel.usdBalance,
            memberSince = userProfileModel.memberSince,
            redeemable_pkr = userProfileModel.redeemablePkr,
            motherMaidenName = userProfileModel.motherMaidenName,
            placeOfBirth = userProfileModel.placeOfBirth,
            cnicNicopIssuanceDate = userProfileModel.cnicNicopIssuanceDate,
            nadraVerified = userProfileModel.nadraVerified,
            requireNadraVerification = userProfileModel.requireNadraVerification,
            receiverCount = userProfileModel.receiverCount,
            notificationCount = userProfileModel.notificationCount,
            country = userProfileModel.country,
            fatherName = userProfileModel.fatherName,
            registrationRating = userProfileModel.registrationRating,
            nadraStatus = userProfileModel.nadraStatus
        )
    }

    fun performLogout() {
        networkHelper.serviceCall(
            serviceGateway.performLogout()
        ).observeForever {
            logoutResponse.value = it
        }
    }

    fun observeLogoutResponse() =
        logoutResponse as LiveData<BaseResponse<GeneralMessageResponseModel>>

    fun onClear() {
        networkHelper.dispose()
        networkHelper.disposable = null
    }
}