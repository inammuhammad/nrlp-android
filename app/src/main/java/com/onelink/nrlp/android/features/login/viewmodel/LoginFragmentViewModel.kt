package com.onelink.nrlp.android.features.login.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.login.model.LoginRequest
import com.onelink.nrlp.android.features.login.repo.LoginRepo
import com.onelink.nrlp.android.features.splash.repo.AuthKeyRepo
import com.onelink.nrlp.android.utils.ValidationUtils
import java.util.*
import javax.inject.Inject

class LoginFragmentViewModel @Inject constructor(
    private val loginRepo: LoginRepo,
    private val authRepo: AuthKeyRepo
) : BaseViewModel() {

    val cnicNicopNumber = MutableLiveData("")
    val password = MutableLiveData("")
    val accountType = MutableLiveData(-1)
    val validationCnicPassed = MutableLiveData(true)
    val validationPasswordPassed = MutableLiveData(true)
    val validationSelectAccountPassed = MutableLiveData(true)
    var isUnverifiedDeviceFlow = false

    private fun doLogin(loginRequest: LoginRequest) = loginRepo.login(loginRequest)


    fun observeLogin() = loginRepo.observeLogin()

    fun getAuthKey(id: Int, nic: String, remitter: String, beneficiary: String) {
        val accountType =
            if (id == R.id.radio1) remitter.toLowerCase(Locale.getDefault()) else beneficiary.toLowerCase(
                Locale.getDefault()
            )
        authRepo.getAuthKey(accountType, nic.replace("-", ""))
    }


    fun observeAuthKey() = authRepo.observeAuthKey()

    override fun onCleared() {
        loginRepo.onClear()
        authRepo.onClear()
        super.onCleared()
    }

    val isCnicValidationPassed = MediatorLiveData<Boolean>().apply {
        addSource(validationCnicPassed) {
            value = it

        }
    }

    val isPasswordValidationPassed = MediatorLiveData<Boolean>().apply {
        addSource(validationPasswordPassed) {
            value = it

        }
    }


    val isSelectAccountValidationPassed = MediatorLiveData<Boolean>().apply {
        addSource(validationSelectAccountPassed) {
            value = it

        }
    }

    val isValidNIC = MediatorLiveData<Boolean>().apply {
        addSource(cnicNicopNumber) {
            val valid = ValidationUtils.isCNICValid(it)
            value = valid

        }
    }

    val isPasswordValid = MediatorLiveData<Boolean>().apply {
        addSource(password) {
            val valid = ValidationUtils.isPasswordLengthValid(it)
            value = valid

        }
    }

    val isAccountTypeSelected = MediatorLiveData<Boolean>().apply {
        addSource(accountType) {
            value = it != -1
        }
    }

    fun loginCall(
        id: Int,
        cnic: String,
        pass: String,
        remitter: String,
        beneficiary: String,
        key: String
    ) {

        if (validationsPassed(id, cnic, pass)) {
            if (id == R.id.radio1) {
                doLogin(
                    LoginRequest(
                        cnic.replace("-", ""),
                        pass,
                        remitter.toLowerCase(Locale.getDefault()),
                        encryptionKey = key
                    )
                )
            } else if (id == R.id.radio2) {
                doLogin(
                    LoginRequest(
                        cnic.replace("-", ""),
                        pass,
                        beneficiary.toLowerCase(Locale.getDefault()),
                        encryptionKey = key

                    )
                )
            }
        }
    }

    fun getUserType(id: Int, remitter: String, beneficiary: String): String {
        return if (id == R.id.radio1)
            remitter.toLowerCase(Locale.getDefault())
        else
            beneficiary.toLowerCase(Locale.getDefault())
    }

    fun checkCnicValidation(string: String) =
        string.isEmpty() || ValidationUtils.isCNICValid(string)

    fun checkPassValidation(string: String) =
        string.isEmpty() || ValidationUtils.isPasswordValid(string)

    fun validationsPassed(id: Int, cnic: String, pass: String): Boolean {
        val isCnicValid: Boolean = checkCnicValidation(cnic)
        val isPasswordValid: Boolean = checkPassValidation(pass)
        val isSelectAccountValid: Boolean = id != -1
        if (cnic.isNotEmpty()) validationCnicPassed.value = isCnicValid
        if (pass.isNotEmpty()) validationPasswordPassed.value = isPasswordValid
        validationSelectAccountPassed.value = isSelectAccountValid
        return isCnicValid && isPasswordValid && isSelectAccountValid
    }
}