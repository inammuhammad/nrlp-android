package com.onelink.nrlp.android.features.complaint.viewmodel

import android.content.res.Resources
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.complaint.fragment.registered.RegComplaintDetailsFragment
import com.onelink.nrlp.android.features.complaint.fragment.registered.RegComplaintResponseFragment
import com.onelink.nrlp.android.features.complaint.repo.ComplainRepo
import com.onelink.nrlp.android.utils.COMPLAINT_TYPE
import com.onelink.nrlp.android.utils.ValidationUtils
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RegComplaintSharedViewModel @Inject
constructor(private val complainRepo: ComplainRepo)
    : BaseViewModel() {

    val complaintType = MutableLiveData(-1)
    val complaintText = MutableLiveData("")
    val userType = MutableLiveData("")
    val complaintId = MutableLiveData("")

    val cnicNumber = MutableLiveData<String>("")
    val cnicAccountNumber = MutableLiveData<String>("")
    val selfAwardPassport = MutableLiveData<String>("")
    val selfAwardIban = MutableLiveData<String>("")
    val country = MutableLiveData<String>("")
    val redemptionCountry = MutableLiveData<String>("")
    val redemptionBranchCenter = MutableLiveData<String>("")
    val mobileNumber = MutableLiveData<String>("")
    val uscBeoeNumber = MutableLiveData<String>("")
    val details = MutableLiveData("")
    val otherDetails = MutableLiveData("")
    val mobileOperator = MutableLiveData("")
    val transactionType = MutableLiveData("")
    val transactionDate = MutableLiveData("")
    val transactionAmount = MutableLiveData("")
    val transactionId = MutableLiveData("")
    val remittingEntity = MutableLiveData("")
    val redemptionPartners = MutableLiveData("")
    val selfAwardType = MutableLiveData("")

    val partnerList = MutableLiveData<List<String>>()

    val validationSelectAccountPassed = MutableLiveData(true)
    val validationSelectComplaintPassed = MutableLiveData(true)
    val validationCnicPassed = MutableLiveData(true)
    val validationSelfAwardCnicPassed = MutableLiveData(true)
    val validationIbanPassed = MutableLiveData(true)
    val validationPassportPassed = MutableLiveData(true)
    val validationAliasPassed = MutableLiveData(true)
    val validationPhoneNumberPassed = MutableLiveData(true)
    val validationUscBeoeNumberPassed = MutableLiveData(true)
    val validationEmailPassed = MutableLiveData(true)
    val validationBeneficiaryMobileOperatorPassed = MutableLiveData(true)
    val validationMobileOperatorPassed = MutableLiveData(true)
    val validationBeneficiaryCnicPassed = MutableLiveData(true)
    val validationTransactionTypePassed = MutableLiveData(true)
    val validationBeneficiaryAccountPassed = MutableLiveData(true)
    val validationRemittingEntityPassed = MutableLiveData(true)
    val validationTransactionIdPassed = MutableLiveData(true)
    val validationTransactionAmountPassed = MutableLiveData(true)
    val validationRedemptionPartnerPassed = MutableLiveData(true)
    val validationSelfAwardTypePassed = MutableLiveData(true)
    val validationSpecifyDetailsPassed = MutableLiveData(true)
    val validationSpecifyOtherDetailsPassed = MutableLiveData(true)
    val validationBeneficiaryCountryPassed = MutableLiveData(true)
    val validationRedemptionCountryPassed = MutableLiveData(true)
    val validationBranchCenterPassed = MutableLiveData(true)
    val validationBeneficiaryCnicPointsPassed = MutableLiveData(true)
    val validationTransactionDatePassed = MutableLiveData(true)

    fun observeAddComplainResponse() = complainRepo.observeAddComplainResponse()

    fun makeComplainCall(addComplaintRequestModel: JsonObject) =
        complainRepo.addComplainRequest(addComplaintRequestModel)

    fun getRedeemPartner() = complainRepo.getRedeemPartner()

    fun observerRedeemPartner() = complainRepo.observeRedeemPartner()

    val transactionTypeNotEmpty= MediatorLiveData<Boolean>().apply {
        validateNonNull(transactionType)
    }
    val cnicNumberNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(cnicNumber)
    }
    val ccountryNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(country)
    }
    val mobileNumberNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(mobileNumber)
    }
    val detailsNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(details)
    }
    val otherDetailsNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(otherDetails)
    }
    val redemptionPartnerNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(redemptionPartners)
    }
    val remittingEntityNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(remittingEntity)
    }
    val transactionIdNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(transactionId)
    }
    val transactionAmountNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(transactionAmount)
    }
    val transactionDateNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(transactionDate)
    }
    val mobileOperatorNotEmpty= MediatorLiveData<Boolean>().apply {
        validateNonNull(mobileOperator)
    }
    val validCnicAccountNumber = MediatorLiveData<Boolean>().apply {
        addSource(cnicAccountNumber) {
            val valid = ValidationUtils.isSelfAwardBeneficiaryAccountValid(it)
            value = valid
        }
    }

    fun checkCnicValidation(string: String) =
        ValidationUtils.isCNICValid(string)


    fun checkPhoneNumberValidation(string: String, int: Int?=10) =
        ValidationUtils.isPhoneNumberValid(string, int)

    fun checkMobileOperatorValidation(string:String) =
        string.isNotEmpty() && ValidationUtils.isNameValid(string)

    fun checkTransactionTypeValidation(string:String) =
        string.isNotEmpty()

    fun checkBeneficiaryAccount(string:String) =
        string.isNotEmpty()

    fun checkRemittingEntity(string: String) =
        string.isNotEmpty()

    fun checkTransactionId(string: String) =
        string.isNotEmpty() && ValidationUtils.isTransactionNoValid(string)

    fun checkTransactionAmount(string: String) =
        ValidationUtils.isAmountValid(string) //string.isNotEmpty()

    fun checkNotEmpty(string: String) =
        string.isNotEmpty()

    fun checkSpecifyDetails(string: String) =
        string.isNotEmpty() && string.length >= 15

    fun checkIban(string: String) =
        ValidationUtils.isIbanAccountNumberValid(string)

    fun checkPassportNumber(string: String) =
        ValidationUtils.isPassportNumberValid(string)

    private fun MediatorLiveData<Boolean>.validateNonNull(it: MutableLiveData<String>) {
        addSource(it) { value = it.isNotEmpty() }
    }

    fun phoneNumberHint(length: Int): String {
        var hintString = ""
        for (i in 0 until length) {
            hintString += "x"
        }
        return hintString
    }

    fun radioValidationPassed(id:Int):Boolean{
        val isSelectValid: Boolean = id != -1
        validationSelectAccountPassed.value = isSelectValid
        return isSelectValid
    }

    fun gotoComplaintDetailsFragment(
        resources: Resources,
        fragmentHelper: BaseFragment.FragmentNavigationHelper,
        id:Int
    ){
        when(id){
            R.id.unableToOtpReg -> {
                complaintText.postValue(resources.getString(R.string.unable_to_otp))
                complaintType.postValue(COMPLAINT_TYPE.UNABLE_TO_RECEIVE_OTP)
            }

            R.id.unableToAddBeneficiary -> {
                complaintText.postValue(resources.getString(R.string.unable_to_add_beneficiary))
                complaintType.postValue(COMPLAINT_TYPE.UNABLE_TO_ADD_BENEFICIARY)
            }

            R.id.unableToTransferPoints -> {
                complaintText.postValue(resources.getString(R.string.unable_to_transfer_points))
                complaintType.postValue(COMPLAINT_TYPE.UNABLE_TO_TRANSFER_POINTS_TO_BENEFICIARY)
            }

            R.id.unableToSelfAward -> {
                complaintText.postValue(resources.getString(R.string.unable_to_self_award_points))
                complaintType.postValue(COMPLAINT_TYPE.UNABLE_TO_SELF_AWARDS_POINTS)
            }

            R.id.unableToRedemption -> {
                complaintText.postValue(resources.getString(R.string.redemption_issues))
                complaintType.postValue(COMPLAINT_TYPE.REDEMPTION_ISSUES)
            }

            R.id.others2 -> {
                complaintText.postValue(resources.getString(R.string.others))
                complaintType.postValue(COMPLAINT_TYPE.OTHERS)
            }
        }
        emptyComplaintDetails()
        clearValidations()
        fragmentHelper.addFragment(
            RegComplaintDetailsFragment.newInstance(),
            clearBackStack = false,
            addToBackStack = true
        )
    }
    fun getDateInStringFormat(calendar: Calendar?): String? {
        val dateString =
            SimpleDateFormat("dd/M/yyyy", Locale.US).parse(transactionDate.value ?: "") ?: return ""
        val day = calendar?.get(Calendar.DATE)
        return SimpleDateFormat("dd-MMM-yy", Locale.US).format(dateString)
    }

    fun gotoComplaintResponseFragment(
        resources: Resources,
        fragmentHelper: BaseFragment.FragmentNavigationHelper,
    ){
        fragmentHelper.addFragment(
            RegComplaintResponseFragment.newInstance(),
            clearBackStack = true,
            addToBackStack = false
        )
    }

    fun clearValidations() {
        validationAliasPassed.postValue(true)
        validationCnicPassed.postValue(true)
        validationIbanPassed.postValue(true)
        validationPassportPassed.postValue(true)
        validationEmailPassed.postValue(true)
        validationPhoneNumberPassed.postValue(true)
        validationUscBeoeNumberPassed.postValue(true)
        validationMobileOperatorPassed.postValue(true)
        validationBeneficiaryCnicPassed.postValue(true)
        validationBeneficiaryMobileOperatorPassed.postValue(true)
        validationBeneficiaryAccountPassed.postValue(true)
        validationRemittingEntityPassed.postValue(true)
        validationTransactionIdPassed.postValue(true)
        validationTransactionAmountPassed.postValue(true)
        validationSpecifyOtherDetailsPassed.postValue(true)
        validationSpecifyDetailsPassed.postValue(true)
        validationRedemptionPartnerPassed.postValue(true)
        validationBeneficiaryCountryPassed.postValue(true)
        validationRedemptionCountryPassed.postValue(true)
        validationBranchCenterPassed.postValue(true)
        validationTransactionTypePassed.postValue(true)
        validationBeneficiaryCnicPointsPassed.postValue(true)
        validationTransactionDatePassed.postValue(true)
        validationSelfAwardTypePassed.postValue(true)
    }

    private fun emptyComplaintDetails(){
        cnicAccountNumber.postValue("")
        transactionType.postValue("")
        transactionAmount.postValue("")
        transactionDate.postValue("")
        transactionId.postValue("")
        remittingEntity.postValue("")
        redemptionPartners.postValue("")
        selfAwardType.postValue("")
        cnicNumber.postValue("")
        country.postValue("")
        redemptionCountry.postValue("")
        redemptionBranchCenter.postValue("")
        mobileNumber.postValue("")
        uscBeoeNumber.postValue("")
        details.postValue("")
        mobileOperator.postValue("")
        selfAwardIban.postValue("")
        selfAwardPassport.postValue("")
    }
}