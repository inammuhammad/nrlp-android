package com.onelink.nrlp.android.features.selfAwardPoints.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.managePoints.model.TransferPointsRequest
import com.onelink.nrlp.android.features.managePoints.repo.ManagePointsFragmentRepo
import com.onelink.nrlp.android.features.register.models.VerifyReferenceNumberRequest
import com.onelink.nrlp.android.features.selfAwardPoints.model.SelfAwardPointsRequest
import com.onelink.nrlp.android.features.selfAwardPoints.repo.SelfAwardPointsFragmentRepo
import com.onelink.nrlp.android.models.BeneficiaryDetailsModel
import com.onelink.nrlp.android.utils.ValidationUtils
import java.math.BigInteger
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SelfAwardPointsFragmentViewModel @Inject constructor(private val selfAwardPointsFragmentRepo: SelfAwardPointsFragmentRepo) :
    BaseViewModel() {

    var rawDate: String = ""
    val rawRemittanceDate = MutableLiveData<String>("")
    val remittanceDate = MutableLiveData<String>("")
    val referenceNumber = MutableLiveData<String>("")
    val cnicAccountNumber = MutableLiveData<String>("")
    val accountIbanNumber = MutableLiveData<String>("")
    val passportNumber = MutableLiveData<String>("")
    val transactionAmount = MutableLiveData<String>("")
    val transactionType = MutableLiveData<String>("")

    fun verifySafeAwardValidTransaction(selfAwardPointsRequest: JsonObject) =
            selfAwardPointsFragmentRepo.verifySelfAwardValidTransaction(selfAwardPointsRequest)

    fun observeSafeAwardValidTransaction() = selfAwardPointsFragmentRepo.observeSelfAwardValidTransaction()

    override fun onCleared() {
        selfAwardPointsFragmentRepo.onClear()
        super.onCleared()
    }

    private fun MediatorLiveData<Boolean>.validateNonNull(it: MutableLiveData<String>) {
        addSource(it) {
            value = it.isNotEmpty()
        }
    }

    val validReferenceNumber = MediatorLiveData<Boolean>().apply {
        addSource(referenceNumber) {
            value = it.isNotEmpty() && ValidationUtils.isTransactionNoValid(it)
        }
    }

    val validTransactionAmount = MediatorLiveData<Boolean>().apply {
        addSource(transactionAmount) {
            val valid = ValidationUtils.isValidAmount(it)
            value = valid
        }
    }

    val validCnicAccountNumber = MediatorLiveData<Boolean>().apply {
        addSource(cnicAccountNumber) {
            val valid = ValidationUtils.isCNICValid(it)
            value = valid
        }
    }

    val validIbanAccountNumber = MediatorLiveData<Boolean>().apply {
        addSource(accountIbanNumber) {
            val valid = ValidationUtils.isIbanAccountNumberValid(it)
            value = valid
        }
    }

    val validPassportNumber = MediatorLiveData<Boolean>().apply {
        addSource(passportNumber) {
            val valid = ValidationUtils.isPassportNumberValid(it)
            value = valid
        }
    }

    val remittanceDateNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(remittanceDate)
    }

    val referenceNumberNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(referenceNumber)
    }

    val transactionAmountNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(transactionAmount)
    }

    val cnicNumberNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(cnicAccountNumber)
    }

    val accountNumberNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(accountIbanNumber)
    }

    val passportNumberNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(passportNumber)
    }

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

    fun getDateInApiFormat(string: String): String? {
        val dateString = SimpleDateFormat("dd/M/yyyy", Locale.US).parse(string) ?: return ""
        return SimpleDateFormat("dd-MMM-yy", Locale.US).format(dateString)
    }
}