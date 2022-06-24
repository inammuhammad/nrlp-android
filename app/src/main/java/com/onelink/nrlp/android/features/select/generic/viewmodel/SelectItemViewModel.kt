package com.onelink.nrlp.android.features.select.generic.viewmodel

import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.select.generic.model.BranchCenterRequestModel
import com.onelink.nrlp.android.features.select.generic.repo.SelectItemRepo
import java.util.*
import javax.inject.Inject

class SelectItemViewModel @Inject constructor(private val selectItemRepo: SelectItemRepo) :
    BaseViewModel() {

    fun getBranchCenter(pseName: String) =
        selectItemRepo.getBranchCenter(BranchCenterRequestModel(pseName))

    fun observeBranchCenter() = selectItemRepo.observeBranchCenter()

    override fun onCleared() {
        selectItemRepo.onClear()
        super.onCleared()
    }
}