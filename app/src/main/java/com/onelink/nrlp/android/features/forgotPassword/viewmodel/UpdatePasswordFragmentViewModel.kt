package com.onelink.nrlp.android.features.forgotPassword.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.forgotPassword.models.UpdatePasswordRequestModel
import com.onelink.nrlp.android.features.forgotPassword.repo.ForgotPasswordRepo
import com.onelink.nrlp.android.utils.ValidationUtils
import javax.inject.Inject

class UpdatePasswordFragmentViewModel @Inject constructor(private val forgotPasswordRepo: ForgotPasswordRepo) :
    BaseViewModel() {
    val password = MutableLiveData("")
    val rePassword = MutableLiveData("")
    val validationPasswordPassed = MutableLiveData(true)
    val validationRePasswordPassed = MutableLiveData(true)

    fun updatePassword(updatePasswordRequestModel: UpdatePasswordRequestModel) =
        forgotPasswordRepo.updatePassword(updatePasswordRequestModel)

    fun observeUpdateForgotPasswordResponse() =
        forgotPasswordRepo.observeUpdateForgotPasswordResponse()

    val passwordNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(password)
    }

    val rePasswordNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(rePassword)
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

    @Suppress("unused")
    fun checkCnicValidation(string: String) =
        string.isEmpty() || ValidationUtils.isCNICValid(string)

    fun checkRePassValidation(pass: String, rePass: String) = pass == rePass

    fun checkPassValidation(string: String) =
        string.isEmpty() || ValidationUtils.isPasswordValid(string)

    private fun MediatorLiveData<Boolean>.validateNonNull(it: MutableLiveData<String>) {
        addSource(it) {
            value = it.isNotEmpty()
        }
    }

    fun validationsPassed(password: String, rePassword: String): Boolean {
        val isPasswordValid: Boolean = checkPassValidation(password)
        val isRePassValid: Boolean = checkRePassValidation(password, rePassword)
        isPasswordValidationPassed.value = isPasswordValid
        isRePasswordValidationPassed.value = isRePassValid
        return isPasswordValid && isRePassValid
    }
}