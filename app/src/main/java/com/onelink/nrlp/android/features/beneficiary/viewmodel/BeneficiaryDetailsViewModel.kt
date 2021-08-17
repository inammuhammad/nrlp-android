package com.onelink.nrlp.android.features.beneficiary.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.beneficiary.models.AddBeneficiaryRequestModel
import com.onelink.nrlp.android.features.beneficiary.models.DeleteBeneficiaryRequestModel
import com.onelink.nrlp.android.features.beneficiary.repo.BeneficiaryRepo
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

    fun deleteBeneficiary(deleteBeneficiaryRequestModel: DeleteBeneficiaryRequestModel) =
        beneficiaryRepo.deleteBeneficiary(deleteBeneficiaryRequestModel)

    fun observeBeneficiaryDeleteResponse() = beneficiaryRepo.observeBeneficiaryDeleteResponse()

    fun addBeneficiary(addBeneficiaryRequestModel: AddBeneficiaryRequestModel) =
        beneficiaryRepo.addBeneficiary(addBeneficiaryRequestModel)

    fun observeBeneficiaryAddResponse() = beneficiaryRepo.observeBeneficiaryAddResponse()

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
        string.isEmpty() || ValidationUtils.isPhoneNumberValid(string, int)

    fun validationsPassed(
        cnic: String, alias: String, phoneNumber: String, phoneNumberLength: Int?
    ): Boolean {
        val isCnicValid: Boolean = checkCnicValidation(cnic)
        val isPhoneNumberValid: Boolean = checkPhoneNumberValidation(phoneNumber, phoneNumberLength)
        val isFullNameValid: Boolean = checkAliasValidation(alias)
        validationCnicPassed.value = isCnicValid
        validationPhoneNumberPassed.value = isPhoneNumberValid
        validationAliasPassed.value = isFullNameValid
        return isCnicValid && isFullNameValid && isPhoneNumberValid
    }
}