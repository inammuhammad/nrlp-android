package com.onelink.nrlp.android.features.viewStatement.viewmodel

import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.viewStatement.models.LoyaltyStatementRequestModel
import com.onelink.nrlp.android.features.viewStatement.repo.ViewStatementRepo
import javax.inject.Inject

class LoyaltyStatementFragmentViewModel @Inject constructor(private val viewStatementRepo: ViewStatementRepo) :
    BaseViewModel() {
    fun getStatements() = viewStatementRepo.getStatements(LoyaltyStatementRequestModel(null, null))
    fun observeStatements() = viewStatementRepo.observeStatements()
}