package com.onelink.nrlp.android.viewmodel

import com.onelink.nrlp.android.features.redeem.repo.RedemptionRepo
import com.onelink.nrlp.android.features.redeem.viewmodels.RedeemAgentConfirmationViewModel
import com.onelink.nrlp.android.base.BaseViewModelTest
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito

class RedeemAgentConfirmationViewModelTest : BaseViewModelTest() {

    companion object {
        private const val AGENT_CODE = "aei0u1"

        private const val AGENT_CODE_INVALID = "aei0u"

    }

    private lateinit var viewModel: RedeemAgentConfirmationViewModel
    private lateinit var redPartnerRepo: RedemptionRepo


    override fun setUp() {
        super.setUp()
        redPartnerRepo = Mockito.mock(RedemptionRepo::class.java)
        viewModel = RedeemAgentConfirmationViewModel(redPartnerRepo)
    }

    @Test
    fun checkAgentCodeSuccess() = assertTrue(
        viewModel.checkAgentCodeValidation(AGENT_CODE)
    )

    @Test
    fun checkAgentCodeFailed() = assertFalse(
        viewModel.checkAgentCodeValidation(AGENT_CODE_INVALID)
    )

    @Test
    fun validationPassedTest() {

        assertTrue(viewModel.agentConfirmationCode.value.isNullOrEmpty())

        viewModel.validationAgentCodePassed.value = false

        viewModel.agentConfirmationCode.value = AGENT_CODE

        viewModel.validationsPassed()

        assertNotNull(viewModel.isAgentCodeValidationPassed.value)

        assertTrue(viewModel.isAgentCodeValidationPassed.value!!)

    }
}