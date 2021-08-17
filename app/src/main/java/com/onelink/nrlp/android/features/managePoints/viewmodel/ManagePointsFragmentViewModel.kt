package com.onelink.nrlp.android.features.managePoints.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.managePoints.model.TransferPointsRequest
import com.onelink.nrlp.android.features.managePoints.repo.ManagePointsFragmentRepo
import com.onelink.nrlp.android.models.BeneficiaryDetailsModel
import com.onelink.nrlp.android.utils.ValidationUtils
import java.math.BigInteger
import javax.inject.Inject

class ManagePointsFragmentViewModel @Inject constructor(private val managePointsRepo: ManagePointsFragmentRepo) :
    BaseViewModel() {
    val points = MutableLiveData<String>("")
    val selectedBene = MutableLiveData<BeneficiaryDetailsModel>(null)
    val validationPointsPassed = MutableLiveData(true)
    val validationSelectedBenePassed = MutableLiveData(true)

    fun isValidTransaction(enteredPoints: BigInteger, totalPoints: BigInteger): Boolean {
        return enteredPoints <= totalPoints
    }

    fun getBeneficiary() = managePointsRepo.getAllBeneficiaries()

    fun observeBeneficiary() = managePointsRepo.observeBeneficiaryResponse()

    private fun transferPoints(transferPointsRequest: TransferPointsRequest) =
        managePointsRepo.transferPoints(transferPointsRequest)

    fun observeTransferPoints() = managePointsRepo.observeTransferPoints()

    val isBeneSelected = MediatorLiveData<Boolean>().apply {
        addSource(selectedBene) {
            val valid = selectedBene.value?.let { it1 ->
                ValidationUtils.isSpinnerNotEmpty(
                    it1.alias,
                    ""
                )
            }
            value = valid
        }
    }

    val isPointsEntered = MediatorLiveData<Boolean>().apply {
        addSource(points) {
            value = it.isNotEmpty() && it != "0"
        }
    }

    fun transferPointsCall(id: Int, points: String) {
        if (validationsPassed(id, points)) {
            transferPoints(TransferPointsRequest(id.toString(), points = points))
        }
    }

    fun validationsPassed(id: Int, points: String): Boolean {
        val isBeneficiaryValid = id != -1
        val isPointsValid = ValidationUtils.arePointsValid(points)
        validationSelectedBenePassed.value = isBeneficiaryValid
        validationPointsPassed.value = isPointsValid
        return isBeneficiaryValid && isPointsValid
    }

}