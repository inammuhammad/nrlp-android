package com.onelink.nrlp.android.features.profile.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.profile.repo.ProfileRepo
import com.onelink.nrlp.android.features.profile.repo.UpdateProfileConstants
import com.onelink.nrlp.android.utils.ValidationUtils
import javax.inject.Inject


class EditProfileViewModel @Inject constructor(private val profileRepo: ProfileRepo) :
    BaseViewModel() {
    val cnicNumber = MutableLiveData("")
    val alias = MutableLiveData("")
    val countryFromApi = MutableLiveData("")
    val country = MutableLiveData("")
    val countryCode = MutableLiveData("xx")
    val countryCodeFromApi = MutableLiveData("xx")
    val mobileNumber = MutableLiveData("")
    val oldMobileNumber = MutableLiveData<String>("")
    val mobileNumFromApi = MutableLiveData<String>("")
    val mobileNumUpdated = MutableLiveData<String>("")
    val email = MutableLiveData<String>("")
    val oldEmail = MutableLiveData<String>("")
    val validationEmailPassed = MutableLiveData(true)
    val validationPhoneNumberPassed = MutableLiveData(true)
    val mobileNumberLength = MutableLiveData<Int>()

    fun getCountryCodes() = profileRepo.getCountryCodes()

    fun observerCountryCodes() = profileRepo.observeCountryCodes()

    fun updateProfile() {
        profileRepo.updateProfile(getUpdateProfileRequestObject())
    }

    fun observeUpdateProfile() = profileRepo.observeUpdateProfile()

    fun getUpdateProfileRequestObject(): JsonObject {
        val jsonObject = JsonObject()
        if (email.value != oldEmail.value)
            jsonObject.addProperty(UpdateProfileConstants.EMAIL, email.value)
        if (countryCode.value + mobileNumber.value != mobileNumFromApi.value)
            jsonObject.addProperty(UpdateProfileConstants.MOBILE_NO, mobileNumUpdated.value)
        return jsonObject
    }

    override fun onCleared() {
        profileRepo.onClear()
        super.onCleared()
    }

    val isDifferentMobileNumber = MediatorLiveData<Boolean>().apply {
        addSource(mobileNumber) {
            val valid = it != oldMobileNumber.value
            value = valid
        }
    }

    val isDifferentEmailAddress = MediatorLiveData<Boolean>().apply {
        addSource(email) {
            val valid = it != oldEmail.value
            value = valid
        }
    }

    val countryNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(country)
    }
    val mobileNumberNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(mobileNumber)
    }

    private fun MediatorLiveData<Boolean>.validateNonNull(it: MutableLiveData<String>) {
        addSource(it) {
            value = it.isNotEmpty()
        }
    }

    fun phoneNumberHint(length: Int): String {
        var hintString = ""
        for (i in 0 until length) {
            hintString += "x"
        }
        return hintString
    }

    fun checkPhoneNumberValidation(string: String, int: Int?) =
        (string.isNotEmpty() && !string.contains("+")) && ValidationUtils.isPhoneNumberValid(
            string,
            int
        )

    fun checkEmailValidation(email: String) =
        !(email.isNotEmpty() && !email.contains("+")) || ValidationUtils.isEmailValid(email)

    fun validationsPassed(email: String, phoneNumber: String, phoneNumberLength: Int?): Boolean {
        val isPhoneNumberValid: Boolean = checkPhoneNumberValidation(phoneNumber, phoneNumberLength)
        val isEmailValid: Boolean = checkEmailValidation(email)
        validationPhoneNumberPassed.value = isPhoneNumberValid
        validationEmailPassed.value = isEmailValid
        return isPhoneNumberValid && isEmailValid
    }
}