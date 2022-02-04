package com.onelink.nrlp.android.features.complaint.viewmodel

import android.content.res.Resources
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.complaint.fragment.unregistered.UnregComplaintDetailsFragment
import com.onelink.nrlp.android.features.complaint.fragment.unregistered.UnregComplaintResponseFragment
import com.onelink.nrlp.android.features.complaint.fragment.unregistered.UnregComplaintTypeFragment
import com.onelink.nrlp.android.features.complaint.repo.ComplainRepo
import com.onelink.nrlp.android.utils.*
import java.util.*
import javax.inject.Inject

class UnRegComplaintSharedViewModel
@Inject constructor(private val complainRepo: ComplainRepo) : BaseViewModel() {


    val accountType = MutableLiveData(-1)
    val complaintType=MutableLiveData(-1)
    val complaintText=MutableLiveData("")
    val userType=MutableLiveData("")
    val complaintId=MutableLiveData("")
    val complaintTypeIndex=MutableLiveData(-1)

    val cnicNumber = MutableLiveData<String>("")
    val alias = MutableLiveData<String>("")
    val country = MutableLiveData<String>("")
    val mobileNumber = MutableLiveData<String>("")
    val emailAddress=MutableLiveData("")
    val details=MutableLiveData("")
    val mobileOperator=MutableLiveData("")


    val validationSelectAccountPassed = MutableLiveData(true)
    val validationSelectComplaintPassed = MutableLiveData(true)
    val validationCnicPassed = MutableLiveData(true)
    val validationAliasPassed = MutableLiveData(true)
    val validationPhoneNumberPassed = MutableLiveData(true)
    val validationEmailPassed = MutableLiveData(true)
    val validationMobileOperatorPassed=MutableLiveData(true)

    private lateinit var resources: Resources

    fun observeAddComplainResponse()=complainRepo.observeAddComplainResponse()

    fun makeComplainCall(addComplaintRequestModel: JsonObject)=
        complainRepo.addComplainRequest(addComplaintRequestModel)


    val isAccountTypeSelected = MediatorLiveData<Boolean>().apply {
        addSource(accountType) {
            value = it != -1
        }
    }

    val cnicNumberNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(cnicNumber)
    }
    val aliasNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(alias)
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
    val mobileOperatorNotEmpty=MediatorLiveData<Boolean>().apply {
        validateNonNull(mobileOperator)
    }

    fun checkMobileOperatorValidation(string:String) =
        string.isNotEmpty()

    fun checkCountryValidation(string:String)=
        string.isNotEmpty()

    fun checkCnicValidation(string: String) =
        string.isNotEmpty() || ValidationUtils.isCNICValid(string)

    fun checkAliasValidation(string: String) =
        string.isNotEmpty() || ValidationUtils.isNameValid(string)

    fun checkPhoneNumberValidation(string: String, int: Int?) =
        string.isNotEmpty() || ValidationUtils.isPhoneNumberValid(string, int)

    fun checkEmailValidation(string:String)=
        string.isEmpty() || ValidationUtils.isEmailValid(string)

    fun checkDetailsValidation(string: String)=
        string.isNotEmpty()

    private fun MediatorLiveData<Boolean>.validateNonNull(it: MutableLiveData<String>) {
        addSource(it) { value = it.isNotEmpty() }
    }

    val isComplaintTypeSelected = MediatorLiveData<Boolean>().apply {
        addSource(complaintType) {
            value = it != -1
        }
    }

    fun getComplaintType():Int{
        return when(complaintText.value!!.toLowerCase(Locale.getDefault())){
            resources.getString(UnregisteredComplaintTypes.UNABLE_TO_REGISTER)
                .toLowerCase(Locale.getDefault())-> 1
            resources.getString(UnregisteredComplaintTypes.UNABLE_TO_RECEIVE_REGISTRATION_CODE)
                .toLowerCase(Locale.getDefault())-> 2
            resources.getString(UnregisteredComplaintTypes.UNABLE_TO_RECEIVE_OTP)
                .toLowerCase(Locale.getDefault())-> 3
            else -> 8
        }
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

    fun detailsValidationsPassed(
        phoneNumber: String, phoneNumberLength: Int?, country:String
    ): Boolean {
        var isCnicValid: Boolean = checkCnicValidation(cnicNumber.value!!)
        var isPhoneNumberValid: Boolean = checkPhoneNumberValidation(phoneNumber, phoneNumberLength)
        var isFullNameValid: Boolean = checkAliasValidation(alias.value!!)
        var isEmailValid:Boolean=checkEmailValidation(emailAddress.value!!)
        var isCountryValid:Boolean= checkCountryValidation(country)
        var isDetailsValid:Boolean=checkDetailsValidation(details.value!!)
        var isOperatorValid=true
        validationCnicPassed.value = isCnicValid
        validationPhoneNumberPassed.value = isPhoneNumberValid
        validationAliasPassed.value = isFullNameValid
        if(emailAddress.value!!.isEmpty())
        {
            isEmailValid=true
        }
        if(getComplaintType()==COMPLAINT_TYPE.UNABLE_TO_RECEIVE_OTP)
        {
            isOperatorValid=checkMobileOperatorValidation(mobileOperator.value!!)
            validationMobileOperatorPassed.value=isOperatorValid
            isCnicValid=true
            isDetailsValid=true
        }
        else if(getComplaintType()==COMPLAINT_TYPE.UNABLE_TO_RECEIVE_REGISTRATION_CODE){
            isFullNameValid=true
            isCnicValid=true
            isDetailsValid=true
            isOperatorValid=checkMobileOperatorValidation(mobileOperator.value!!)
            validationMobileOperatorPassed.value=isOperatorValid

        }
        return isCnicValid && isFullNameValid && isPhoneNumberValid
                && isEmailValid && isCountryValid &&isDetailsValid &&isOperatorValid
    }
    fun gotoComplaintTypeFragment(
        resources: Resources,
        fragmentHelper: BaseFragment.FragmentNavigationHelper,
        id: Int
    ) {
        if(id==R.id.remitter){
            userType.postValue(Constants.REMITTER)
        }
        else{
            userType.postValue(Constants.BENEFICIARY)
        }
        fragmentHelper.addFragment(
            UnregComplaintTypeFragment.newInstance(),
            clearBackStack = false,
            addToBackStack = true
        )
    }

    fun gotoComplaintDetailsFragment(
        resources: Resources,
        fragmentHelper: BaseFragment.FragmentNavigationHelper,
        id:Int
    ){
        when(id){
            R.id.unableToRegister ->{
                complaintText.postValue(resources.getString(R.string.unable_to_register))
                complaintTypeIndex.postValue(COMPLAINT_TYPE.UNABLE_TO_REGISTER)
            }

            R.id.unableToOtp -> {
                complaintText.postValue(resources.getString(R.string.unable_to_otp))
                complaintTypeIndex.postValue(COMPLAINT_TYPE.UNABLE_TO_RECEIVE_OTP)
            }

            R.id.unableToRegistrationCode -> {
                complaintText.postValue(resources.getString(R.string.unable_to_registration_code))
                complaintTypeIndex.postValue(COMPLAINT_TYPE.UNABLE_TO_RECEIVE_REGISTRATION_CODE)
            }

            R.id.others -> {
                complaintText.postValue(resources.getString(R.string.others))
                complaintTypeIndex.postValue(COMPLAINT_TYPE.OTHERS)
            }
        }
        emptyComplaintDetails()
        clearValidations()
        fragmentHelper.addFragment(
            UnregComplaintDetailsFragment.newInstance(),
            clearBackStack = false,
            addToBackStack = true
        )
    }

    fun resourceProvider(resources: Resources){
        this.resources=resources
    }

    fun gotoComplaintResponseFragment(
        resources: Resources,
        fragmentHelper: BaseFragment.FragmentNavigationHelper,
    ){
        fragmentHelper.addFragment(
            UnregComplaintResponseFragment.newInstance(),
            clearBackStack = true,
            addToBackStack = false
        )
    }

    fun clearValidations(){
        validationAliasPassed.postValue(true)
        validationCnicPassed.postValue(true)
        validationEmailPassed.postValue(true)
        validationPhoneNumberPassed.postValue(true)
        validationMobileOperatorPassed.postValue(true)
    }

    fun emptyComplaintDetails(){
        emailAddress.postValue("")
        cnicNumber.postValue("")
        alias.postValue("")
        country.postValue("")
        mobileNumber.postValue("")
        details.postValue("")
        mobileOperator.postValue("")
    }
}