package com.onelink.nrlp.android.features.forgotPassword.viewmodel

import android.content.res.Resources
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.forgotPassword.models.ForgotPasswordRequestModel
import com.onelink.nrlp.android.features.forgotPassword.repo.ForgotPasswordRepo
import com.onelink.nrlp.android.features.splash.repo.AuthKeyRepo
import com.onelink.nrlp.android.utils.Constants
import com.onelink.nrlp.android.utils.ValidationUtils
import java.util.*
import javax.inject.Inject


class ForgotPasswordFragmentViewModel @Inject constructor(private val forgotPasswordRepo: ForgotPasswordRepo , private val authRepo: AuthKeyRepo) :
    BaseViewModel() {
    val accountType = MutableLiveData(Constants.SPINNER_ACCOUNT_TYPE_HINT)
    val cnicNicopNumber = MutableLiveData("")
    val motherMaidenName = MutableLiveData("")
    val validationCnicPassed = MutableLiveData(true)
    val validationMotherMaidenNamePassed = MutableLiveData(true)


    fun getAuthKey(nic: String, accountType: String) {
        authRepo.getAuthKey(accountType, nic)
    }

    fun observeAuthKey() = authRepo.observeAuthKey()


    fun forgotPassword(forgotPasswordRequestModel: ForgotPasswordRequestModel) =
        forgotPasswordRepo.forgotPassword(forgotPasswordRequestModel)

    fun observeForgotPasswordResponse() = forgotPasswordRepo.observeForgotPasswordResponse()

    val cnicNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(cnicNicopNumber)
    }

    val isAccountTypeSelected = MediatorLiveData<Boolean>().apply {
        addSource(accountType) {
            val valid = accountType.value?.let { value ->
                ValidationUtils.isSpinnerNotEmpty(
                    value,
                    Constants.SPINNER_ACCOUNT_TYPE_HINT
                )
            }
            value = valid
        }
    }

    fun getAccountType(resources: Resources): String {
        return when {
            accountType.value.toString() == resources.getString(R.string.remitter) -> Constants.REMITTER
            accountType.value.toString() == resources.getString(R.string.beneficiary) -> Constants.BENEFICIARY
            else -> ""
        }
    }

    val isCnicValidationPassed = MediatorLiveData<Boolean>().apply {
        addSource(validationCnicPassed) {
            value = it
        }
    }

    fun checkCnicValidation(string: String) =
        string.isEmpty() || ValidationUtils.isCNICValid(string)

    fun checkNameValidation(string: String) =
        ValidationUtils.isMotherNameValid(string)

    private fun MediatorLiveData<Boolean>.validateNonNull(it: MutableLiveData<String>) {
        addSource(it) {
            value = it.isNotEmpty()
        }
    }

    fun validationsPassed(cnic: String): Boolean {
        val isCnicValid: Boolean = checkCnicValidation(cnic)
        validationCnicPassed.value = isCnicValid
        return isCnicValid
    }
}