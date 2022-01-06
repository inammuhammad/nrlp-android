package com.onelink.nrlp.android.features.home.fragments

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.home.model.NadraDetailsRequestModel
import com.onelink.nrlp.android.features.home.repo.HomeRepo
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class HomeFragmentViewModel @Inject constructor(private val homeRepo: HomeRepo) : BaseViewModel() {

    val mothersMaidenName = MutableLiveData<String>("")
    val placeOfBirth = MutableLiveData<String>("")
    val fullName = MutableLiveData<String>("")
    val cnicNicopDateOfIssuance = MutableLiveData<String>("")
    var rawDate: String = ""
    val rawFromDate = MutableLiveData<String>("")

    fun getUserProfile() = homeRepo.getUserProfile()

    fun observeUserProfile() = homeRepo.observeUserProfile()

    fun observeUpdateNadraDetails() = homeRepo.observeUpdateNadra()

    fun getDateInStringFormat(calendar: Calendar?): String? {
        val dateString =
            SimpleDateFormat("dd/M/yyyy", Locale.US).parse(rawDate ?: "") ?: return ""
        val day = calendar?.get(Calendar.DATE)
        return SimpleDateFormat("dd-MMM-yy", Locale.US).format(dateString)
    }

    fun updateNadraDetails(
        motherMaidenName: String,
        placeOfBirth: String,
        cnicNicopDateOfIssue: String,
        fullName: String
    ) = homeRepo.updateNadraDetails(
        NadraDetailsRequestModel(
            motherMaidenName,
            placeOfBirth,
            cnicNicopDateOfIssue,
            fullName
        )
    )

    fun navigateNadraVerification(fragmentHelper: BaseFragment.FragmentNavigationHelper){
        fragmentHelper.addFragment(
            NadraVerificationRequiredFragment.newInstance(),
            clearBackStack = false,
            addToBackStack = true
        )
    }

    override fun onCleared() {
        homeRepo.onClear()
        super.onCleared()
    }
}
