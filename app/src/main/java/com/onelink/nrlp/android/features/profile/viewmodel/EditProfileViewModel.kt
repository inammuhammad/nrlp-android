package com.onelink.nrlp.android.features.profile.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.features.profile.repo.ProfileRepo
import com.onelink.nrlp.android.features.profile.repo.UpdateProfileConstants
import com.onelink.nrlp.android.utils.Constants
import com.onelink.nrlp.android.utils.ValidationUtils
import javax.inject.Inject


class EditProfileViewModel @Inject constructor(private val profileRepo: ProfileRepo) :
    BaseViewModel() {
    val cnicNumber = MutableLiveData("")
    val alias = MutableLiveData("")
    val residentId = MutableLiveData<String>("")
    val passportId = MutableLiveData<String>("")
    val passportType = MutableLiveData<String>(Constants.SPINNER_PASSPORT_TYPE_HINT)
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
    val oldCountry = MutableLiveData<String>("")
    val oldResidentID = MutableLiveData<String>("")
    val oldPassportType = MutableLiveData<String>("")
    val oldPassportId = MutableLiveData<String>("")
    val validationEmailPassed = MutableLiveData(true)
    val validationPhoneNumberPassed = MutableLiveData(true)
    val validationPassportNumberPassed = MutableLiveData(true)
    val validationUniqueIdPassed = MutableLiveData(true)
    val mobileNumberLength = MutableLiveData<Int>()
    val motherMaidenName = MutableLiveData("")
    val placeOfBirth = MutableLiveData("")
    val cnicIssuanceDate = MutableLiveData("")
    val verifyMotherMaidenName = MutableLiveData<String>("")

    fun getCountryCodes(type: String = "beneficiary") = profileRepo.getCountryCodes(type)

    fun observerCountryCodes() = profileRepo.observeCountryCodes()

    fun updateProfile() {
        profileRepo.updateProfile(getUpdateProfileRequestObject())
    }

    fun updateProfileMobile(){
        profileRepo.updateProfileMobile(getUpdateProfileRequestObject())
    }

    fun observeUpdateProfile() = profileRepo.observeUpdateProfile()

    fun observeUpdateProfileMobile() = profileRepo.observeUpdateProfileMobile()

    fun isMobileNumberChanged()=
        countryCode.value + mobileNumber.value != mobileNumFromApi.value

    fun getUpdateProfileRequestObject(): JsonObject {
        val jsonObject = JsonObject()
        if(country.value != oldCountry.value)
            jsonObject.addProperty(UpdateProfileConstants.COUNTRY, country.value)
        if (email.value != oldEmail.value)
            jsonObject.addProperty(UpdateProfileConstants.EMAIL, email.value)
        if (countryCode.value + mobileNumber.value != mobileNumFromApi.value)
            jsonObject.addProperty(UpdateProfileConstants.MOBILE_NO, mobileNumUpdated.value)

        UserData.getUser()?.let { userModel ->
            if(userModel.accountType == "beneficiary"){
                jsonObject.addProperty(UpdateProfileConstants.PASSPORT_ID, "-")
                jsonObject.addProperty(UpdateProfileConstants.PASSPORT_TYPE, "-")
                jsonObject.addProperty(UpdateProfileConstants.RESIDENT_ID, "-")
            }
            else {
                if (passportId.value != oldPassportId.value)
                    jsonObject.addProperty(UpdateProfileConstants.PASSPORT_ID, passportId.value)

                if (passportType.value != oldPassportType.value)
                    jsonObject.addProperty(UpdateProfileConstants.PASSPORT_TYPE, passportType.value)

                if (residentId.value != oldResidentID.value)
                    jsonObject.addProperty(UpdateProfileConstants.RESIDENT_ID, residentId.value)

                jsonObject.addProperty(
                    UpdateProfileConstants.MOTHER_MAIDEN_NAME,
                    verifyMotherMaidenName.value
                )
            }
        }

//        if (passportType.value != oldPassportType.value)
//            jsonObject.addProperty(UpdateProfileConstants.PASSPORT_TYPE, passportType.value)
//
//        if (residentId.value != oldResidentID.value)
//            jsonObject.addProperty(UpdateProfileConstants.RESIDENT_ID, residentId.value)


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

    val isDifferentResidentId = MediatorLiveData<Boolean>().apply {
        addSource(residentId) {
            val valid = it != oldResidentID.value
            value = valid
        }
    }

    val isDifferentPassportType = MediatorLiveData<Boolean>().apply {
        addSource(passportType) {
            val valid = it != oldPassportType.value
            value = valid
        }
    }

    val isDifferentPassportId = MediatorLiveData<Boolean>().apply {
        addSource(passportId) {
            val valid = it != oldPassportId.value
            value = valid
        }
    }

    val isDifferentCountry = MediatorLiveData<Boolean>().apply {
        addSource(country) {
            val valid = it != oldCountry.value
            value = valid
        }
    }

    val residentIdNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(residentId)
    }

    val passportIdNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(passportId)
    }

    val isPassportTypeSelected = MediatorLiveData<Boolean>().apply {
        addSource(passportType) {
            val valid = passportType.value?.let { it1 ->
                ValidationUtils.isSpinnerNotEmpty(
                        it1,
                        Constants.SPINNER_PASSPORT_TYPE_HINT
                )
            }
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

    fun checkPassportValidation(string: String) =
        ValidationUtils.isPassportNumberValid(string)

    fun checkUniqueIdValidation(string: String) =
        string.isNotEmpty()


    fun validationsPassed(email: String, phoneNumber: String, phoneNumberLength: Int?,
           uniqueId: String, passportNo: String): Boolean {
        val isPhoneNumberValid: Boolean = checkPhoneNumberValidation(phoneNumber, phoneNumberLength)
        val isEmailValid: Boolean = checkEmailValidation(email)
        val isUniqueIdValid: Boolean = checkUniqueIdValidation(uniqueId)
        val isPassportNoValid: Boolean = checkPassportValidation(passportNo)
        validationPhoneNumberPassed.value = isPhoneNumberValid
        validationEmailPassed.value = isEmailValid
        validationPassportNumberPassed.value = isPassportNoValid
        validationUniqueIdPassed.value = isUniqueIdValid
        return isPhoneNumberValid && isEmailValid && isUniqueIdValid && isPassportNoValid
    }

    fun beneficiaryValidation(phoneNumber: String): Boolean{
        val isPhoneNumberValid: Boolean = checkPhoneNumberValidation(phoneNumber, 0)
        validationPhoneNumberPassed.value = isPhoneNumberValid
        return isPhoneNumberValid
    }
}