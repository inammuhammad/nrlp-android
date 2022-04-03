package com.onelink.nrlp.android.features.beneficiary.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.beneficiary.models.AddBeneficiaryRequestModel
import com.onelink.nrlp.android.features.beneficiary.models.DeleteBeneficiaryRequestModel
import com.onelink.nrlp.android.features.beneficiary.models.ResendBeneficiaryOtpRequestModel
import com.onelink.nrlp.android.features.beneficiary.models.UpdateBeneficiaryRequestModel
import com.onelink.nrlp.android.features.beneficiary.repo.BeneficiaryRepo
import com.onelink.nrlp.android.utils.Constants
import com.onelink.nrlp.android.utils.ValidationUtils
import javax.inject.Inject


open class BeneficiaryDetailsViewModel @Inject constructor(private val beneficiaryRepo: BeneficiaryRepo) :
    BaseViewModel() {
    val cnicNumber = MutableLiveData<String>("")
    val alias = MutableLiveData<String>("")
    val country = MutableLiveData<String>("")
    val mobileNumber = MutableLiveData<String>("")
    val validationCnicPassed = MutableLiveData(true)
    val validationAliasPassed = MutableLiveData(true)
    val validationPhoneNumberPassed = MutableLiveData(true)
    val validationOtherRelationshipPassed = MutableLiveData(true)
    val beneficiaryRelation = MutableLiveData<String>("")
    val oldCnic = MutableLiveData<String>("")
    val oldCountry = MutableLiveData<String>("")
    val oldMobileNumber = MutableLiveData<String>("")


    fun deleteBeneficiary(deleteBeneficiaryRequestModel: DeleteBeneficiaryRequestModel) =
        beneficiaryRepo.deleteBeneficiary(deleteBeneficiaryRequestModel)

    fun addBeneficiaryResendOtp(request:ResendBeneficiaryOtpRequestModel)=
        beneficiaryRepo.addBeneficiaryResendOtp(request)

    fun updateBeneficiary(request: UpdateBeneficiaryRequestModel)=
        beneficiaryRepo.updateBeneficiary(request)

    fun getCountryCodes(type: String = "beneficiary") = beneficiaryRepo.getCountryCodes(type)

    fun observeBeneficiaryResendOtp() =beneficiaryRepo.observeBeneficiaryResendOtpResponse()

    fun observeBeneficiaryDeleteResponse() = beneficiaryRepo.observeBeneficiaryDeleteResponse()

    fun observeBeneficiaryUpdateResponse() =beneficiaryRepo.observerBeneficiaryUpdateResponse()

    fun addBeneficiary(addBeneficiaryRequestModel: AddBeneficiaryRequestModel) =
        beneficiaryRepo.addBeneficiary(addBeneficiaryRequestModel)

    fun observeBeneficiaryAddResponse() = beneficiaryRepo.observeBeneficiaryAddResponse()

    fun observerCountryCodes() = beneficiaryRepo.observeCountryCodes()

    override fun onCleared() {
        beneficiaryRepo.onClear()
        super.onCleared()
    }

    val cnicNumberNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(cnicNumber)
    }
    val aliasNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(alias)
    }
    val ccountryNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(country)
    }
    val mobileNumberNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(mobileNumber)
    }
    val relationShipNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(beneficiaryRelation)
    }

    private fun MediatorLiveData<Boolean>.validateNonNull(it: MutableLiveData<String>) {
        addSource(it) { value = it.isNotEmpty() }
    }

    fun phoneNumberHint(length: Int): String {
        var hintString = ""
        for (i in 0 until length) {
            hintString += "x"
        }
        return hintString
    }

    fun checkCnicValidation(string: String) =
        string.isEmpty() || ValidationUtils.isCNICValid(string)

    fun checkAliasValidation(string: String) =
        string.isEmpty() || ValidationUtils.isNameValid(string)

    fun checkPhoneNumberValidation(string: String, int: Int?) =
        ValidationUtils.isPhoneNumberValid(string, int)

    fun checkEditPhoneNumberValidation(string: String, int: Int?) =
        ValidationUtils.isPhoneNumberValid(string, int)

    fun checkEditCnicValidation(string: String) =
        ValidationUtils.isCNICValid(string)

    fun checkOtherRelationshipValidation(string: String) =
        ValidationUtils.isNameValid(string)

    fun validationsPassed(
        cnic: String, alias: String, phoneNumber: String, phoneNumberLength: Int?,
        otherRelationship: String, relationshipText: String
    ): Boolean {
        val isCnicValid: Boolean = checkCnicValidation(cnic)
        val isPhoneNumberValid: Boolean = checkPhoneNumberValidation(phoneNumber, phoneNumberLength)
        val isFullNameValid: Boolean = checkAliasValidation(alias)
        val isOtherRelationShipValid: Boolean = checkOtherRelationshipValidation(relationshipText)
        validationCnicPassed.value = isCnicValid
        validationPhoneNumberPassed.value = isPhoneNumberValid
        validationAliasPassed.value = isFullNameValid
        if(otherRelationship == "Other" || otherRelationship == "کوئی اور" ) {
            validationOtherRelationshipPassed.value = isOtherRelationShipValid
            return isCnicValid && isFullNameValid && isPhoneNumberValid && isOtherRelationShipValid
        }
        return isCnicValid && isFullNameValid && isPhoneNumberValid
    }

    fun editValidationsPassed(
        cnic: String, phoneNumber: String, phoneNumberLength: Int?
    ): Boolean {
        val isCnicValid: Boolean = checkEditCnicValidation(cnic)
        val isPhoneNumberValid: Boolean = checkEditPhoneNumberValidation(phoneNumber, phoneNumberLength)
        validationCnicPassed.value = isCnicValid
        validationPhoneNumberPassed.value = isPhoneNumberValid
        return isCnicValid && isPhoneNumberValid
    }
}