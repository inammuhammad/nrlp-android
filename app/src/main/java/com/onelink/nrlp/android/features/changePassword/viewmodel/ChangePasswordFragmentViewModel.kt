package com.onelink.nrlp.android.features.changePassword.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.changePassword.models.ChangePasswordRequest
import com.onelink.nrlp.android.features.changePassword.repo.ChangePasswordRepo
import com.onelink.nrlp.android.utils.ValidationUtils
import javax.inject.Inject

class ChangePasswordFragmentViewModel @Inject constructor(private val changePasswordRepo: ChangePasswordRepo) :
    BaseViewModel() {
    val currentPassword = MutableLiveData("")
    val newPassword = MutableLiveData("")
    val rePassword = MutableLiveData("")
    val validationPasswordPassed = MutableLiveData(true)
    val validationRePasswordPassed = MutableLiveData(true)
    val validationOldPasswordPassed = MutableLiveData(true)

    fun changePasswordCall(oldPassword: String, newPassword: String, reEnterPassword: String) {
        if (validationsPassed(oldPassword, newPassword, reEnterPassword)) {
            changePassword(ChangePasswordRequest(oldPassword, newPassword))
        }
    }

    fun changePassword(changePasswordRequest: ChangePasswordRequest) =
        changePasswordRepo.changePassword(changePasswordRequest)

    fun observerChangePassword() = changePasswordRepo.observeChangePassword()

    val oldPasswordNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(currentPassword)
    }

    val passwordNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(newPassword)
    }

    val rePasswordNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(rePassword)
    }


    private fun MediatorLiveData<Boolean>.validateNonNull(it: MutableLiveData<String>) {
        addSource(it) { value = it.isNotEmpty() }
    }

    val isPasswordValidationPassed = MediatorLiveData<Boolean>().apply {
        addSource(validationPasswordPassed) {
            value = it

        }
    }
    val isRePasswordValidationPassed = MediatorLiveData<Boolean>().apply {
        addSource(validationRePasswordPassed) {
            value = it

        }
    }

    val isOldPasswordValidationPassed = MediatorLiveData<Boolean>().apply {
        addSource(validationOldPasswordPassed) {
            value = it

        }
    }

    fun checkRePassValidation(pass: String, rePass: String) = pass == rePass

    fun checkPassValidation(string: String) =
        string.isEmpty() || ValidationUtils.isPasswordLengthValid(string)

    fun checkNewPassValidation(string: String) =
        string.isEmpty() || ValidationUtils.isPasswordValid(string)

    fun validationsPassed(oldPassword: String, password: String, rePassword: String): Boolean {
        val isPasswordValid: Boolean = checkNewPassValidation(password)
        val isOldPasswordValid: Boolean = checkPassValidation(oldPassword)
        val isRePassValid: Boolean = checkRePassValidation(password, rePassword)
        validationRePasswordPassed.value = isRePassValid
        validationPasswordPassed.value = isPasswordValid
        validationOldPasswordPassed.value = isOldPasswordValid
        return isPasswordValid && isRePassValid && isOldPasswordValid
    }
}
