package com.onelink.nrlp.android.features.viewStatement.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.viewStatement.models.DetailedStatementRequestModel
import com.onelink.nrlp.android.features.viewStatement.repo.ViewStatementRepo
import com.onelink.nrlp.android.utils.ValidationUtils
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AdvancedLoyaltyStatementFragmentViewModel @Inject constructor(private val viewStatementRepo: ViewStatementRepo) :
    BaseViewModel() {
    var rawDate: String = ""
    val rawFromDate = MutableLiveData<String>("")
    val rawToDate = MutableLiveData<String>("")
    val fromDate = MutableLiveData<String>("")
    val toDate = MutableLiveData<String>("")
    val emailAddress = MutableLiveData<String>("")
    val validationEmailPassed = MutableLiveData(true)



    fun getDetailedStatements(detailedStatementRequestModel: DetailedStatementRequestModel) =
        viewStatementRepo.getDetailedStatements(detailedStatementRequestModel)

    fun observeDetailedStatement() = viewStatementRepo.observeDetailedStatement()

    val fromDateNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(fromDate)
    }

    val toDateNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(toDate)
    }

    val emailNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(emailAddress)
    }

    private fun MediatorLiveData<Boolean>.validateNonNull(it: MutableLiveData<String>) {
        addSource(it) {
            value = it.isNotEmpty()
        }
    }

    val isEmailValidationPassed = MediatorLiveData<Boolean>().apply {
        addSource(validationEmailPassed) {
            value = it
        }
    }

    fun getDateInStringFormat(calendar: Calendar?): String? {
        val dateString =
            SimpleDateFormat("dd/M/yyyy", Locale.US).parse(rawDate ?: "") ?: return ""
        val day = calendar?.get(Calendar.DATE)
        return if (day !in 11..18) when (day?.rem(10)) {
            1 -> SimpleDateFormat("d'st' MMMM yyyy", Locale.US).format(dateString)
            2 -> SimpleDateFormat("d'nd' MMMM yyyy", Locale.US).format(dateString)
            3 -> SimpleDateFormat("d'rd' MMMM yyyy", Locale.US).format(dateString)
            else -> SimpleDateFormat("d'th' MMMM yyyy", Locale.US).format(dateString)
        } else SimpleDateFormat("d'th' MMMM yyyy", Locale.US).format(dateString)
    }

    fun getDateInApiFormat(string: String): String? {
        val dateString = SimpleDateFormat("dd/M/yyyy", Locale.US).parse(string) ?: return ""
        return SimpleDateFormat("yyyyMMdd", Locale.US).format(dateString)
    }

    fun validationsPassed(
        email: String
    ): Boolean {
        val isEmailValid: Boolean = ValidationUtils.isEmailValid(email)
        validationEmailPassed.value = isEmailValid
        return isEmailValid
    }
}