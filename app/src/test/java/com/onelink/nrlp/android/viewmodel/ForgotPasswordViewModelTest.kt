package com.onelink.nrlp.android.viewmodel

import com.onelink.nrlp.android.features.forgotPassword.repo.ForgotPasswordRepo
import com.onelink.nrlp.android.features.forgotPassword.viewmodel.ForgotPasswordFragmentViewModel
import com.onelink.nrlp.android.utils.Constants
import com.onelink.nrlp.android.base.BaseViewModelTest
import com.onelink.nrlp.android.features.splash.repo.AuthKeyRepo
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito

class ForgotPasswordViewModelTest : BaseViewModelTest() {

    companion object {
        private const val CNIC = "34101-5417680-8"
    }

    private lateinit var viewModel: ForgotPasswordFragmentViewModel
    private lateinit var forgotPasswordRepo: ForgotPasswordRepo
    private lateinit var authKeyRepo: AuthKeyRepo

    override fun setUp() {
        super.setUp()
        forgotPasswordRepo = Mockito.mock(ForgotPasswordRepo::class.java)
        authKeyRepo = Mockito.mock(AuthKeyRepo::class.java)
        viewModel = ForgotPasswordFragmentViewModel(forgotPasswordRepo, authKeyRepo)
    }

    @Test
    fun validationPassedSuccess() {

        assertNotNull(viewModel.accountType.value)

        assertEquals(Constants.SPINNER_ACCOUNT_TYPE_HINT, viewModel.accountType.value)

        assertTrue(viewModel.validationCnicPassed.value != false)

        assertTrue(viewModel.cnicNicopNumber.value.isNullOrEmpty())


        viewModel.validationCnicPassed.value = false


        val isPassed = viewModel.validationsPassed(
            CNIC
        )

        assertTrue(isPassed)

    }

}