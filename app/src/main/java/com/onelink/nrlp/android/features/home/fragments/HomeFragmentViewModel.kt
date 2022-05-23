package com.onelink.nrlp.android.features.home.fragments

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.home.model.NadraDetailsRequestModel
import com.onelink.nrlp.android.features.home.model.VerifyFatherNameRequestModel
import com.onelink.nrlp.android.features.home.repo.HomeRepo
import com.onelink.nrlp.android.utils.ValidationUtils
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class HomeFragmentViewModel @Inject constructor(private val homeRepo: HomeRepo) : BaseViewModel() {

    val mothersMaidenName = MutableLiveData<String>("")
    val fatherName = MutableLiveData<String>("")
    val placeOfBirth = MutableLiveData<String>("")
    val fullName = MutableLiveData<String>("")
    val cnicNicopDateOfIssuance = MutableLiveData<String>("")
    var rawDate: String = ""
    val rawFromDate = MutableLiveData<String>("")
    val validationCnicNicopIssuanceDatePassed = MutableLiveData(true)
    val validationFullNamePassed = MutableLiveData(true)
    val validationMotherMaidenNamePassed = MutableLiveData(true)
    val validationPlaceOfBirthPassed = MutableLiveData(true)
    val validationFatherNamePassed = MutableLiveData(true)

    fun getUserProfile() = homeRepo.getUserProfile()

    fun observeUserProfile() = homeRepo.observeUserProfile()

    fun observeUpdateNadraDetails() = homeRepo.observeUpdateNadra()

    fun observeVerifyFatherName() = homeRepo.observeVerifyFatherNameResponse()

    fun verifyFatherName(name: String) {
        homeRepo.verifyFatherName(VerifyFatherNameRequestModel(name))
    }

    fun getDateInStringFormat(calendar: Calendar?): String? {
        val dateString =
            SimpleDateFormat("dd/M/yyyy", Locale.US).parse(rawDate ?: "") ?: return ""
        val day = calendar?.get(Calendar.DATE)
        return SimpleDateFormat("dd-MMM-yy", Locale.US).format(dateString)
    }

    private fun MediatorLiveData<Boolean>.validateNonNull(it: MutableLiveData<String>) {
        addSource(it) {
            value = it.isNotEmpty()
        }
    }

    val cnicNicopDateOfIssuanceNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(cnicNicopDateOfIssuance)
    }

    val fullNameNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(fullName)
    }

    val mothersMaidenNameNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(mothersMaidenName)
    }

    val placeOfBirthNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(placeOfBirth)
    }

    val fatherNameNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(fatherName)
    }

    val isFullNameValidationPassed = MediatorLiveData<Boolean>().apply {
        addSource(validationFullNamePassed) {
            value = it

        }
    }

    val isMotherMaidenNameValidationPassed = MediatorLiveData<Boolean>().apply {
        addSource(validationMotherMaidenNamePassed) {
            value = it

        }
    }

    val isFatherNameValidationPassed = MediatorLiveData<Boolean>().apply {
        addSource(validationFatherNamePassed) {
            value = it

        }
    }

    val isCnicNicopIssuanceDateValidationPassed = MediatorLiveData<Boolean>().apply {
        addSource(validationCnicNicopIssuanceDatePassed) {
            value = it

        }
    }

    val isPlaceOfBirthValidationPassed = MediatorLiveData<Boolean>().apply {
        addSource(validationPlaceOfBirthPassed) {
            value = it
        }
    }


    fun checkCnicDateIssueValid(string: String) =
        string.isEmpty() || ValidationUtils.isDateValid(string)

    fun checkFullNameValidation(string: String) =
        string.isEmpty() || ValidationUtils.isNameValid(string)

    fun checkMotherNameValidation(string: String) =
        string.isEmpty() || ValidationUtils.isMotherNameValid(string)

    fun checkFatherNameValidation(name: String) = ValidationUtils.isMotherNameValid(name)

    fun validationsPassed(
        fullName: String, motherName: String = "",cnicIssueDate: String = ""
    ): Boolean {
        val isDateOfIssueValid: Boolean = checkCnicDateIssueValid(cnicIssueDate)
        val isFullNameValid: Boolean = checkFullNameValidation(fullName)
        val isMotherNameValid: Boolean = checkMotherNameValidation(motherName)
        validationCnicNicopIssuanceDatePassed.value = isDateOfIssueValid
        validationFullNamePassed.value = isFullNameValid
        validationMotherMaidenNamePassed.value = isMotherNameValid
        return  isFullNameValid &&  isMotherNameValid
    }

    fun validateNadraVerification(
        fullName: String, motherName: String, cnicIssueDate: String, placeOfBirth: String,
    ): Boolean{
        val isDateOfIssueValid: Boolean = checkCnicDateIssueValid(cnicIssueDate)
        val isFullNameValid: Boolean = checkFullNameValidation(fullName)
        val isMotherNameValid: Boolean = checkMotherNameValidation(motherName)
        val isPlaceOfBirthValid: Boolean = checkMotherNameValidation(placeOfBirth)
        validationCnicNicopIssuanceDatePassed.value = isDateOfIssueValid
        validationFullNamePassed.value = isFullNameValid
        validationMotherMaidenNamePassed.value = isMotherNameValid
        validationPlaceOfBirthPassed.value = isPlaceOfBirthValid
        return  isFullNameValid &&  isMotherNameValid && isPlaceOfBirthValid
    }

    fun validateFatherName(name: String): Boolean {
        val isFatherNameValid: Boolean = checkFatherNameValidation(name)
        validationFatherNamePassed.value = isFatherNameValid
        return isFatherNameValid
    }

    fun updateNadraDetails(
        motherMaidenName: String,
        placeOfBirth: String,
        cnicNicopDateOfIssue: String,
        fullName: String
    ) {
       if(validationsPassed(fullName,motherMaidenName,cnicNicopDateOfIssue)){
           homeRepo.updateNadraDetails(
               NadraDetailsRequestModel(
                   motherMaidenName,
                   placeOfBirth,
                   cnicNicopDateOfIssue,
                   fullName
               )
           )
       }
    }

    fun navigateNadraVerification(fragmentHelper: BaseFragment.FragmentNavigationHelper){
        fragmentHelper.addFragment(
            NadraVerificationRequiredFragment.newInstance(),
            clearBackStack = false,
            addToBackStack = true
        )
    }

    fun navigateFatherNameVerification(fragmentHelper: BaseFragment.FragmentNavigationHelper){
        fragmentHelper.addFragment(
            FatherNameVerificationFragment.newInstance(),
            clearBackStack = true,
            addToBackStack = false
        )
    }

    override fun onCleared() {
        homeRepo.onClear()
        super.onCleared()
    }
}
