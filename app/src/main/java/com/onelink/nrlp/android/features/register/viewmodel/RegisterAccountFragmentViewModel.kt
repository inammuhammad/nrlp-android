package com.onelink.nrlp.android.features.register.viewmodel

import android.content.res.Resources
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.register.fragments.VerifyBeneficiaryFragment
import com.onelink.nrlp.android.features.register.fragments.VerifyRemitterFragment
import com.onelink.nrlp.android.features.splash.repo.AuthKeyRepo
import com.onelink.nrlp.android.utils.Constants
import com.onelink.nrlp.android.utils.IdentityKeyUtils
import com.onelink.nrlp.android.utils.ValidationUtils
import javax.inject.Inject

class RegisterAccountFragmentViewModel @Inject constructor(private val authRepo: AuthKeyRepo) : BaseViewModel() {
    val emailAddress = MutableLiveData<String>("")
    val cnicNicopNumber = MutableLiveData<String>("")
    val fullName = MutableLiveData<String>("")
    val country = MutableLiveData<String>("")
    val mobileNumber = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")
    val rePassword = MutableLiveData<String>("")
    val accountType = MutableLiveData<String>(Constants.SPINNER_ACCOUNT_TYPE_HINT)
    val validationCnicPassed = MutableLiveData(true)
    val validationPasswordPassed = MutableLiveData(true)
    val validationPhoneNumberPassed = MutableLiveData(true)
    val validationEmailPassed = MutableLiveData(true)
    val validationFullNamePassed = MutableLiveData(true)
    val validationRePasswordPassed = MutableLiveData(true)



    fun getAuthKey(accountType : String , nic : String) = authRepo.getAuthKey(accountType , nic)

    fun observeAuthKey() = authRepo.observeAuthKey()

    fun addNextFragment(
        resources: Resources,
        fragmentHelper: BaseFragment.FragmentNavigationHelper
    ) {
        if (accountType.value.toString() == resources.getString(R.string.remitter)) {
            fragmentHelper.addFragment(
                VerifyRemitterFragment.newInstance(),
                clearBackStack = false,
                addToBackStack = true
            )
        } else if (accountType.value.toString() == resources.getString(R.string.beneficiary)) {
            fragmentHelper.addFragment(
                VerifyBeneficiaryFragment.newInstance(),
                clearBackStack = false,
                addToBackStack = true
            )
        }
    }

    fun getAccountType(resources: Resources): String {
        return when {
            accountType.value.toString() == resources.getString(R.string.remitter) -> Constants.REMITTER
            accountType.value.toString() == resources.getString(R.string.beneficiary) -> Constants.BENEFICIARY
            else -> ""
        }
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

    fun checkRePassValidation(pass: String, rePass: String) = pass == rePass

    fun validationsPassed(
        cnic: String, fullName: String, phoneNumber: String, phoneNumberLength: Int?,
        email: String, pass: String, repass: String
    ): Boolean {
        val isCnicValid: Boolean = checkCnicValidation(cnic)
        val isPhoneNumberValid: Boolean = checkPhoneNumberValidation(phoneNumber, phoneNumberLength)
        val isPasswordValid: Boolean = checkPassValidation(pass)
        val isFullNameValid: Boolean = checkFullNameValidation(fullName)
        val isEmailValid: Boolean = checkEmailValidation(email)
        val isRePassValid: Boolean = checkRePassValidation(pass, repass)
        validationCnicPassed.value = isCnicValid
        validationPhoneNumberPassed.value = isPhoneNumberValid
        validationFullNamePassed.value = isFullNameValid
        validationEmailPassed.value = isEmailValid
        validationRePasswordPassed.value = isRePassValid
        validationPasswordPassed.value = isPasswordValid
        return isCnicValid && isPasswordValid && isRePassValid &&
                isEmailValid && isFullNameValid && isPhoneNumberValid
    }

    override fun onCleared() {
        authRepo.onClear()
        super.onCleared()
    }
}