package com.onelink.nrlp.android.features.receiver.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.beneficiary.models.AddBeneficiaryRequestModel
import com.onelink.nrlp.android.features.beneficiary.models.DeleteBeneficiaryRequestModel
import com.onelink.nrlp.android.features.beneficiary.models.ResendBeneficiaryOtpRequestModel
import com.onelink.nrlp.android.features.beneficiary.models.UpdateBeneficiaryRequestModel
import com.onelink.nrlp.android.features.beneficiary.repo.BeneficiaryRepo
import com.onelink.nrlp.android.features.receiver.models.AddReceiverRequestModel
import com.onelink.nrlp.android.features.receiver.models.DeleteReceiverRequestModel
import com.onelink.nrlp.android.features.receiver.repo.ReceiverRepo
import com.onelink.nrlp.android.utils.Constants
import com.onelink.nrlp.android.utils.ValidationUtils
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


open class ReceiverDetailsViewModel @Inject constructor(private val receiverRepo: ReceiverRepo) :
    BaseViewModel() {
    val cnicNumber = MutableLiveData<String>("")
    val cnicNicopDateOfIssuance = MutableLiveData<String>("")
    val alias = MutableLiveData<String>("")
    val placeOfBirth = MutableLiveData<String>("")
    val motherMaidenName = MutableLiveData<String>("")
    val bankName = MutableLiveData<String>("")
    val ibanNumber = MutableLiveData<String>("")
    val country = MutableLiveData<String>("")
    val mobileNumber = MutableLiveData<String>("")
    val validationCnicPassed = MutableLiveData(true)
    val validationAliasPassed = MutableLiveData(true)
    val validationPhoneNumberPassed = MutableLiveData(true)
    val validationMotherMaidenPassed = MutableLiveData(true)
    val validationBankNamePassed = MutableLiveData(true)
    val validationIbanPassed = MutableLiveData(true)
    val beneficiaryRelation = MutableLiveData<String>(Constants.SPINNER_BENEFICIARY_HINT)
    var rawDate: String = ""
    val rawFromDate = MutableLiveData<String>("")


    fun deleteBeneficiary(deleteBeneficiaryRequestModel: DeleteBeneficiaryRequestModel){} //= beneficiaryRepo.deleteBeneficiary(deleteBeneficiaryRequestModel)

    fun addBeneficiaryResendOtp(request:ResendBeneficiaryOtpRequestModel){} //= beneficiaryRepo.addBeneficiaryResendOtp(request)

    fun updateBeneficiary(request: UpdateBeneficiaryRequestModel){} //= beneficiaryRepo.updateBeneficiary(request)

    fun observeBeneficiaryResendOtp(){} //=beneficiaryRepo.observeBeneficiaryResendOtpResponse()

    fun observeBeneficiaryDeleteResponse(){} //= beneficiaryRepo.observeBeneficiaryDeleteResponse()

    fun observeBeneficiaryUpdateResponse(){} //=beneficiaryRepo.observerBeneficiaryUpdateResponse()

    fun deleteReceiver(deleteReceiverRequestModel: DeleteReceiverRequestModel) = receiverRepo.deleteReceiver(deleteReceiverRequestModel)

    fun addReceiver(addReceiverRequestModel: AddReceiverRequestModel) =
        receiverRepo.addReceiver(addReceiverRequestModel)

    fun observeReceiverAddResponse() = receiverRepo.observeReceiverAddResponse()
    fun observeReceiverDeleteResponse() = receiverRepo.observeReceiverDeleteResponse()

    override fun onCleared() {
        receiverRepo.onClear()
        super.onCleared()
    }

    val cnicNumberNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(cnicNumber)
    }
    val aliasNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(alias)
    }
    val countryNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(country)
    }
    val mobileNumberNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(mobileNumber)
    }
    val bankNameNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(bankName)
    }
    val ibanNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(ibanNumber)
    }
    val motherMaidenNameNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(motherMaidenName)
    }
    val placeOfBirthNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(placeOfBirth)
    }

    private fun MediatorLiveData<Boolean>.validateNonNull(it: MutableLiveData<String>) {
        addSource(it) { value = it.isNotEmpty() }
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

    fun phoneNumberHint(length: Int): String {
        var hintString = ""
        for (i in 0 until length) {
            hintString += "x"
        }
        return hintString
    }

    fun checkCnicValidation(string: String) =
        string.isEmpty() || ValidationUtils.isCNICValid(string)

    fun checkAliasValidation(string: String) =
        string.isEmpty() || ValidationUtils.isNameValid(string)

    fun checkPhoneNumberValidation(string: String, int: Int?) =
        string.isEmpty() || ValidationUtils.isPhoneNumberValid(string, int)

    fun checkBankNameValidation(string: String) =
        string.isNotEmpty() || ValidationUtils.isBankNameValid(string)

    fun checkIbanValidation(string: String) =
        string.isNotEmpty() || ValidationUtils.isIbanAccountNumberValid(string)

    fun checkMotherMaidenNameValidation(string: String) =
        string.isEmpty() || ValidationUtils.isNameValid(string)

    fun validationsPassed(
        cnic: String, alias: String, phoneNumber: String, phoneNumberLength: Int?
    ): Boolean {
        val isCnicValid: Boolean = checkCnicValidation(cnic)
        val isPhoneNumberValid: Boolean = checkPhoneNumberValidation(phoneNumber, phoneNumberLength)
        val isFullNameValid: Boolean = checkAliasValidation(alias)
        val isMotherMaidenNameValid = checkMotherMaidenNameValidation("")
        val isBankNameValid = checkBankNameValidation("")
        val isIbanValid = checkIbanValidation("")
        validationCnicPassed.value = isCnicValid
        validationPhoneNumberPassed.value = isPhoneNumberValid
        validationAliasPassed.value = isFullNameValid
        return isCnicValid && isFullNameValid && isPhoneNumberValid
    }
}