package com.onelink.nrlp.android.features.register.viewmodel

import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.register.models.RegisterBeneficiaryRequest
import com.onelink.nrlp.android.features.register.models.RegisterFlowDataModel
import com.onelink.nrlp.android.features.register.models.RegisterRemitterRequest
import com.onelink.nrlp.android.features.register.registerRepo.RegisterRepo
import com.onelink.nrlp.android.utils.Constants
import java.util.*
import javax.inject.Inject


class TermsAndConditionsViewModel @Inject constructor(private val registerRepo: RegisterRepo) :
    BaseViewModel() {
    val checked = MutableLiveData<Boolean>(false)

    fun makeRegisterCall(registerFlowDataModel: RegisterFlowDataModel) {
        val accountType = registerFlowDataModel.accountType
        if (accountType == Constants.REMITTER.toLowerCase(Locale.getDefault())) {
            registerRemitter(
                RegisterRemitterRequest(
                    nicNicop = registerFlowDataModel.cnicNicop,
                    mobileNo = registerFlowDataModel.phoneNumber,
                    password = registerFlowDataModel.password,
                    fullName = registerFlowDataModel.fullName,
                    userType = registerFlowDataModel.accountType,
                    referenceNo = registerFlowDataModel.referenceNumber,
                    amount = registerFlowDataModel.transactionAmount,
                    email = registerFlowDataModel.email,
                    residentId = registerFlowDataModel.residentId,
                    passportType = registerFlowDataModel.passportType,
                    passportId = registerFlowDataModel.passportId,
                    country = registerFlowDataModel.country,
                    motherMaidenName = registerFlowDataModel.motherMaidenName,
                    placeOfBirth = registerFlowDataModel.placeOfBirth,
                    cnicNicopIssueDate = registerFlowDataModel.cnicNicopIssueDate,
                    sotp = "2"
                )
            )
        } else if (accountType == Constants.BENEFICIARY.toLowerCase(Locale.getDefault())) {
            registerBeneficiary(
                RegisterBeneficiaryRequest(
                    nicNicop = registerFlowDataModel.cnicNicop,
                    mobileNo = registerFlowDataModel.phoneNumber,
                    password = registerFlowDataModel.password,
                    fullName = registerFlowDataModel.fullName,
                    userType = registerFlowDataModel.accountType,
                    email = registerFlowDataModel.email,
                    registrationCode = registerFlowDataModel.registrationCode,
                    residentId = registerFlowDataModel.residentId,
                    passportType = registerFlowDataModel.passportType,
                    passportId = registerFlowDataModel.passportId,
                    country = registerFlowDataModel.country,
                    placeOfBirth = registerFlowDataModel.placeOfBirth,
                    cnicNicopIssueDate = registerFlowDataModel.cnicNicopIssueDate,
                    sotp = "2"
                )
            )
        }
    }

    fun getTermsAndConditions(lang: String = "en") = registerRepo.getTermsAndConditions(lang)

    fun observeTermsAndConditions() = registerRepo.observeTermsAndConditions()

    private fun registerRemitter(registerRemitterRequest: RegisterRemitterRequest) =
        registerRepo.registerRemitter(registerRemitterRequest)

    private fun registerBeneficiary(registerBeneficiaryRequest: RegisterBeneficiaryRequest) =
        registerRepo.registerBeneficiary(registerBeneficiaryRequest)

    fun observeRegisterUser() = registerRepo.observeRegisterUser()
}