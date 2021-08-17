package com.onelink.nrlp.android.features.register.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.register.models.VerifyReferenceNumberRequest
import com.onelink.nrlp.android.features.register.registerRepo.RegisterRepo
import com.onelink.nrlp.android.utils.ValidationUtils
import javax.inject.Inject

class VerifyRemitterFragmentViewModel @Inject constructor(private val registerRepo: RegisterRepo) :
    BaseViewModel() {

    val referenceNumber = MutableLiveData<String>("")
    val transactionAmount = MutableLiveData<String>("")

    fun verifyReferenceNumber(verifyReferenceNumberRequest: VerifyReferenceNumberRequest) =
        registerRepo.verifyReferenceNumber(verifyReferenceNumberRequest)

    fun observeVerifyReferenceNumber() = registerRepo.observeVerifyReferenceNumber()

    override fun onCleared() {
        registerRepo.onClear()
        super.onCleared()
    }

    val validReferenceNumber = MediatorLiveData<Boolean>().apply {
        addSource(referenceNumber) {
            value = it.isNotEmpty()
        }
    }

    val validTransactionAmount = MediatorLiveData<Boolean>().apply {
        addSource(transactionAmount) {
            val valid = ValidationUtils.isValidAmount(it)
            value = valid
        }
    }
}
