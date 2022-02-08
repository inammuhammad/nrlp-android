package com.onelink.nrlp.android.features.profile.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.profile.repo.ProfileRepo
import com.onelink.nrlp.android.utils.ValidationUtils
import javax.inject.Inject

class EditProfileVerificationViewModel@Inject constructor
    (private val profileRepo: ProfileRepo) :
    BaseViewModel()
{
    val motherMaidenName=MutableLiveData("")
    val motherNameValidationPassed=MutableLiveData(true)

    val motherMaidenNameNotEmpty = MediatorLiveData<Boolean>().apply {
        validateNonNull(motherMaidenName)
    }

    fun checkNADRAverification(jsonObject: JsonObject){
        profileRepo.verifyUpdateProfileValidate(jsonObject)
    }

    fun observeProfileValidation() = profileRepo.observeEditProfileValidate()

    private fun MediatorLiveData<Boolean>.validateNonNull(it: MutableLiveData<String>) {
        addSource(it) {
            value = it.isNotEmpty()
        }
    }

    fun checkMotherNameValidation(name:String)=
        name.isNotEmpty() && ValidationUtils.isMotherNameValid(name)

    fun isValidationsPassed(name:String):Boolean{
        val isNameValid=checkMotherNameValidation(name)
        motherNameValidationPassed.postValue(isNameValid)
        return isNameValid
    }

}