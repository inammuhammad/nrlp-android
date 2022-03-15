package com.onelink.nrlp.android.features.register.viewmodel

import android.content.res.Resources
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.register.fragments.*
import com.onelink.nrlp.android.features.register.models.RegisterBeneficiaryRequest
import com.onelink.nrlp.android.features.register.models.RegisterFlowDataModel
import com.onelink.nrlp.android.features.register.models.RegisterRemitterRequest
import com.onelink.nrlp.android.features.register.registerRepo.RegisterRepo
import com.onelink.nrlp.android.features.splash.repo.AuthKeyRepo
import com.onelink.nrlp.android.utils.Constants
import com.onelink.nrlp.android.utils.IdentityKeyUtils
import com.onelink.nrlp.android.utils.ValidationUtils
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RegisterAccountFragmentViewModel @Inject constructor(
    private val authRepo: AuthKeyRepo, private val registerRepo: RegisterRepo
) : BaseViewModel() {
    val emailAddress = MutableLiveData<String>("")
    val cnicNicopNumber = MutableLiveData<String>("")
    val cnicNicopDateOfIssuance = MutableLiveData<String>("")
    val fullName = MutableLiveData<String>("")
    val mothersMaidenName = MutableLiveData<String>("")
    val placeOfBirth = MutableLiveData<String>("")
    val country = MutableLiveData<String>("")
    val residentId = MutableLiveData<String>("")
    val passportId = MutableLiveData<String>("")
    val mobileNumber = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")
    val rePassword = MutableLiveData<String>("")
    val accountType = MutableLiveData<String>(Constants.SPINNER_ACCOUNT_TYPE_HINT)
    val passportType = MutableLiveData<String>(Constants.SPINNER_PASSPORT_TYPE_HINT)
    val validationCnicPassed = MutableLiveData(true)
    val validationPasswordPassed = MutableLiveData(true)
    val validationPhoneNumberPassed = MutableLiveData(true)
    val validationEmailPassed = MutableLiveData(true)
    val validationFullNamePassed = MutableLiveData(true)
    val validationRePasswordPassed = MutableLiveData(true)
    val validationMotherMaidenNamePassed = MutableLiveData(true)
    val validationPlaceOfBirthPassed = MutableLiveData(true)
    val validationCnicNicopIssuanceDatePassed = MutableLiveData(true)
    val validationPassportNumberPassed = MutableLiveData(true)

    val etOTP1 = MutableLiveData<String>("")
    val etOTP2 = MutableLiveData<String>("")
    val etOTP3 = MutableLiveData<String>("")
    val etOTP4 = MutableLiveData<String>("")

    var rawDate: String = ""
    val rawFromDate = MutableLiveData<String>("")
    val rawToDate = MutableLiveData<String>("")
    //val fromDate = MutableLiveData<String>("")
    val toDate = MutableLiveData<String>("")

    val validEditText1 = MediatorLiveData<Boolean>().apply {
        addSource(etOTP1) {
            value = it.isNotEmpty()
        }
    }

    val validEditText2 = MediatorLiveData<Boolean>().apply {
        addSource(etOTP2) {
            value = it.isNotEmpty()
        }
    }

    val validEditText3 = MediatorLiveData<Boolean>().apply {
        addSource(etOTP3) {
            value = it.isNotEmpty()
        }
    }

    val validEditText4 = MediatorLiveData<Boolean>().apply {
        addSource(etOTP4) {
            value = it.isNotEmpty()
        }
    }


    fun getAuthKey(accountType : String , nic : String) = authRepo.getAuthKey(accountType , nic)

    fun observeAuthKey() = authRepo.observeAuthKey()

//    val isNextActive = MediatorLiveData<Boolean>().apply {
//        if (accountType.value.toString() == "Remitter") {
//                validateNonNull(cnicNicopNumber).toString().equals("true") &&
//                validateNonNull(fullName).toString().equals("true") &&
//                validateNonNull(mobileNumber).toString().equals("true") &&
//                validateNonNull(password).toString().equals("true") &&
//                validateNonNull(rePassword).toString().equals("true") &&
//                validateNonNull(rePassword).toString().equals("true") &&
//                validateNonNull(residentId).toString().equals("true") &&
//                validateNonNull(passportId).toString().equals("true") &&
//                accountType.value.toString() != Constants.SPINNER_ACCOUNT_TYPE_HINT &&
//                passportType.value.toString() != Constants.SPINNER_PASSPORT_TYPE_HINT
//        }
//        else{
//
//        }
//    }
//
//    fun isNextEnable(): Boolean {
//        if (accountType.value.toString() == "Remitter") {
//            if(cnicNotEmpty.value.toString().equals("true") && fullNameNotEmpty.value.toString().equals("true") && mobileNumberNotEmpty.value.toString().equals("true") && passwordNotEmpty.value.toString().equals("true") && rePasswordNotEmpty.value.toString().equals("true") && accountType.value.toString() != Constants.SPINNER_ACCOUNT_TYPE_HINT && passportType.value.toString() != Constants.SPINNER_PASSPORT_TYPE_HINT && residentIdNotEmpty.value.toString().equals("true") && passportIdNotEmpty.value.toString().equals("true"))
//                return true
//        }
//        else{
//            if(cnicNotEmpty.value.toString().equals("true") && fullNameNotEmpty.value.toString().equals("true") && mobileNumberNotEmpty.value.toString().equals("true") && passwordNotEmpty.value.toString().equals("true") && rePasswordNotEmpty.value.toString().equals("true") && accountType.value.toString() != Constants.SPINNER_ACCOUNT_TYPE_HINT)
//                return true
//        }
//        return false
//    }

    fun addNextFragment(
        resources: Resources,
        fragmentHelper: BaseFragment.FragmentNavigationHelper
    ) {
        if (accountType.value.toString() == resources.getString(R.string.remitter)) {
            fragmentHelper.addFragment(
                OtpAuthenticationFragment.newInstance(),
                clearBackStack = false,
                addToBackStack = true
            )
        } else if (accountType.value.toString() == resources.getString(R.string.beneficiary)) {
            fragmentHelper.addFragment(
                RegisterBeneficiaryFragment.newInstance(),
                clearBackStack = false,
                addToBackStack = true
            )
        }
    }

    fun addBeneficiaryTermsFragment(
        resources: Resources,
        fragmentHelper: BaseFragment.FragmentNavigationHelper
    ){
        fragmentHelper.addFragment(
            TermsAndConditionsFragment.newInstance(),
            clearBackStack = false,
            addToBackStack = true
        )
    }

    fun getAccountType(resources: Resources): String {
        return when {
            accountType.value.toString() == resources.getString(R.string.remitter) -> Constants.REMITTER
            accountType.value.toString() == resources.getString(R.string.beneficiary) -> Constants.BENEFICIARY
            else -> ""
        }
    }

    fun getPassportType(resources: Resources): String {
        return when {
            passportType.value.toString() == resources.getString(R.string.international_passport) -> Constants.InternationalPassport
            passportType.value.toString() == resources.getString(R.string.pakistani_passport) -> Constants.PakistaniPassport
            else -> ""
        }
    }

    fun makeRegisterCall(registerFlowDataModel: RegisterFlowDataModel) {
        val accountType = registerFlowDataModel.accountType
        if (accountType == Constants.REMITTER.toLowerCase(Locale.getDefault())) {
            registerRemitter(
                RegisterRemitterRequest(
                    nicNicop = registerFlowDataModel.cnicNicop,
                    mobileNo = registerFlowDataModel.phoneNumber,
                    password = registerFlowDataModel.password,
                    fullName = registerFlowDataModel.fullName,
                    userType = registerFlowDataModel.accountType,
                    /*referenceNo = registerFlowDataModel.referenceNumber,
                    amount = registerFlowDataModel.transactionAmount,*/
                    email = registerFlowDataModel.email,
                    residentId = registerFlowDataModel.residentId,
                    passportType = registerFlowDataModel.passportType,
                    passportId = registerFlowDataModel.passportId,
                    country = registerFlowDataModel.country,
                    motherMaidenName = registerFlowDataModel.motherMaidenName,
                    placeOfBirth = registerFlowDataModel.placeOfBirth,
                    cnicNicopIssueDate = registerFlowDataModel.cnicNicopIssueDate,
                    sotp = "1"
                )
            )
        }
        else
        {
            registerBeneficiary(
                RegisterBeneficiaryRequest(
                    nicNicop = registerFlowDataModel.cnicNicop,
                    mobileNo = "-", //registerFlowDataModel.phoneNumber,
                    password = "-", //registerFlowDataModel.password,
                    fullName = "-", //registerFlowDataModel.fullName,
                    userType = registerFlowDataModel.accountType,
                    email = "-", //registerFlowDataModel.email,
                    registrationCode = registerFlowDataModel.registrationCode,
                    motherMaidenName = registerFlowDataModel.motherMaidenName,
                    residentId = "-", //registerFlowDataModel.residentId,
                    passportType = "-", //registerFlowDataModel.passportType,
                    passportId = "-", //registerFlowDataModel.passportId,
                    country = "-", //registerFlowDataModel.country,
                    placeOfBirth = "-", //registerFlowDataModel.placeOfBirth,
                    cnicNicopIssueDate = "-", //registerFlowDataModel.cnicNicopIssueDate,
                    sotp = "1"
            ))
        }
    }

    private fun registerRemitter(registerRemitterRequest: RegisterRemitterRequest) =
        registerRepo.registerRemitter(registerRemitterRequest)

    private fun registerBeneficiary(registerBeneficiaryRequest: RegisterBeneficiaryRequest) =
        registerRepo.registerBeneficiary(registerBeneficiaryRequest)

    fun observeRegisterUser() = registerRepo.observeRegisterUser()

    fun getDateInStringFormat(calendar: Calendar?): String? {
        val dateString =
            SimpleDateFormat("dd/M/yyyy", Locale.US).parse(rawDate ?: "") ?: return ""
        val day = calendar?.get(Calendar.DATE)
        return SimpleDateFormat("dd-MMM-yy", Locale.US).format(dateString)
        /*return if (day !in 11..18) when (day?.rem(10)) {
            1 -> SimpleDateFormat("d'st' MMMM yyyy", Locale.US).format(dateString)
            2 -> SimpleDateFormat("d'nd' MMMM yyyy", Locale.US).format(dateString)
            3 -> SimpleDateFormat("d'rd' MMMM yyyy", Locale.US).format(dateString)
            else -> SimpleDateFormat("d'th' MMMM yyyy", Locale.US).format(dateString)
        } else SimpleDateFormat("d'th' MMMM yyyy", Locale.US).format(dateString)*/
    }

    @Suppress("unused")
    val emailNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(emailAddress)
    }

    val cnicNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(cnicNicopNumber)
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

    val cnicNicopDateOfIssuanceNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(cnicNicopDateOfIssuance)
    }

    val residentIdNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(residentId)
    }

    val passportIdNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(passportId)
    }

    val countryNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(country)
    }

    val mobileNumberNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(mobileNumber)
    }

    val passwordNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(password)
    }

    val rePasswordNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(rePassword)
    }

    val isAccountTypeSelected = MediatorLiveData<Boolean>().apply {
        addSource(accountType) {
            val valid = accountType.value?.let { it1 ->
                ValidationUtils.isSpinnerNotEmpty(
                    it1,
                    Constants.SPINNER_ACCOUNT_TYPE_HINT
                )
            }
            value = valid
        }
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

    fun phoneNumberHint(length: Int): String {
        var hintString = ""
        for (i in 0 until length) {
            hintString += "x"
        }
        return hintString
    }

    private fun MediatorLiveData<Boolean>.validateNonNull(it: MutableLiveData<String>) {
        addSource(it) {
            value = it.isNotEmpty()
        }
    }

    val isCnicValidationPassed = MediatorLiveData<Boolean>().apply {
        addSource(validationCnicPassed) {
            value = it

        }
    }

    val isPhoneNumberValidationPassed = MediatorLiveData<Boolean>().apply {
        addSource(validationPhoneNumberPassed) {
            value = it

        }
    }

    val isPasswordValidationPassed = MediatorLiveData<Boolean>().apply {
        addSource(validationPasswordPassed) {
            value = it

        }
    }

    val isFullNameValidationPassed = MediatorLiveData<Boolean>().apply {
        addSource(validationFullNamePassed) {
            value = it

        }
    }

    val isRePasswordValidationPassed = MediatorLiveData<Boolean>().apply {
        addSource(validationRePasswordPassed) {
            value = it

        }
    }

    val isEmailValidationPassed = MediatorLiveData<Boolean>().apply {
        addSource(validationEmailPassed) {
            value = it

        }
    }

    val isMotherMaidenNameValidationPassed = MediatorLiveData<Boolean>().apply {
        addSource(validationMotherMaidenNamePassed) {
            value = it

        }
    }

    val isCnicNicopIssuanceDateValidationPassed = MediatorLiveData<Boolean>().apply {
        addSource(validationCnicNicopIssuanceDatePassed) {
            value = it

        }
    }

    val isPassportNumberPassed = MediatorLiveData<Boolean>().apply {
        addSource(validationPassportNumberPassed) {
            value = it

        }
    }

    fun checkCnicValidation(string: String) =
        string.isEmpty() || ValidationUtils.isCNICValid(string)

    fun checkPassValidation(string: String) =
        string.isEmpty() || ValidationUtils.isPasswordValid(string)

    fun checkPhoneNumberValidation(string: String, int: Int?) =
        string.isEmpty() || ValidationUtils.isPhoneNumberValid(string, int)

    fun checkFullNameValidation(string: String) =
        string.isEmpty() || ValidationUtils.isNameValid(string)

    fun checkEmailValidation(string: String) =
        string.isEmpty() || ValidationUtils.isEmailValid(string)

    fun checkMotherNameValidation(string: String) =
        string.isEmpty() || ValidationUtils.isMotherNameValid(string)

    fun checkCnicDateIssueValid(string: String) =
        string.isEmpty() || ValidationUtils.isDateValid(string)

    fun checkRePassValidation(pass: String, rePass: String) = pass == rePass

    fun checkPassportNumberValid(string: String) =
        string.isEmpty() || ValidationUtils.isPassportNumberValid(string)

    fun validationsPassed(
        cnic: String, fullName: String, phoneNumber: String, phoneNumberLength: Int?,
        email: String, pass: String, repass: String, motherName: String = "",
        cnicIssueDate: String = "", passportNum: String = ""
    ): Boolean {
        val isCnicValid: Boolean = checkCnicValidation(cnic)
        val isPhoneNumberValid: Boolean = checkPhoneNumberValidation(phoneNumber, phoneNumberLength)
        val isPasswordValid: Boolean = checkPassValidation(pass)
        val isFullNameValid: Boolean = checkFullNameValidation(fullName)
        val isEmailValid: Boolean = checkEmailValidation(email)
        val isRePassValid: Boolean = checkRePassValidation(pass, repass)
        val isMotherNameValid: Boolean = checkMotherNameValidation(motherName)
        val isDateOfIssueValid: Boolean = checkCnicDateIssueValid(cnicIssueDate)
        val isPassportNumberValid: Boolean = checkPassportNumberValid(passportNum)
        validationCnicPassed.value = isCnicValid
        validationPhoneNumberPassed.value = isPhoneNumberValid
        validationFullNamePassed.value = isFullNameValid
        validationEmailPassed.value = isEmailValid
        validationRePasswordPassed.value = isRePassValid
        validationPasswordPassed.value = isPasswordValid
        validationCnicNicopIssuanceDatePassed.value = isDateOfIssueValid
        validationMotherMaidenNamePassed.value = isMotherNameValid
        validationPassportNumberPassed.value = isPassportNumberValid
        return isCnicValid && isPasswordValid && isRePassValid && isPassportNumberValid &&
                isEmailValid && isFullNameValid && isPhoneNumberValid && isMotherNameValid
    }

    fun isBeneficiaryCnicValid(cnic: String): Boolean {
        val isCnicValid: Boolean = checkCnicValidation(cnic)
        validationCnicPassed.value = isCnicValid
        return isCnicValid
    }

    override fun onCleared() {
        authRepo.onClear()
        super.onCleared()
    }
}