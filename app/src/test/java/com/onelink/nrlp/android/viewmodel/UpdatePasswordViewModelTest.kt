package com.onelink.nrlp.android.viewmodel

import com.onelink.nrlp.android.features.forgotPassword.repo.ForgotPasswordRepo
import com.onelink.nrlp.android.features.forgotPassword.viewmodel.UpdatePasswordFragmentViewModel
import com.onelink.nrlp.android.base.BaseViewModelTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito

class UpdatePasswordViewModelTest : BaseViewModelTest() {

    companion object {
        private const val NEW_PASSWORD = "NewPass@123"

        private const val RE_PASSWORD = "NewPass@123"

    }

    private lateinit var viewModel: UpdatePasswordFragmentViewModel
    private lateinit var forgotPasswordRepo: ForgotPasswordRepo


    override fun setUp() {
        super.setUp()
        forgotPasswordRepo = Mockito.mock(ForgotPasswordRepo::class.java)
        viewModel = UpdatePasswordFragmentViewModel(forgotPasswordRepo)
    }


    @Test
    fun validationPassedSuccess() {

        assertTrue(viewModel.validationPasswordPassed.value != false)

        assertTrue(viewModel.validationRePasswordPassed.value != false)


        viewModel.validationPasswordPassed.value = false

        viewModel.validationRePasswordPassed.value = false


        val isPassed = viewModel.validationsPassed(
            NEW_PASSWORD,
            RE_PASSWORD
        )

        assertTrue(isPassed)

        assertNotNull(viewModel.isPasswordValidationPassed.value)

        assertTrue(viewModel.isPasswordValidationPassed.value!!)

        assertNotNull(viewModel.isRePasswordValidationPassed.value)

        assertTrue(viewModel.isRePasswordValidationPassed.value!!)

        assertTrue(
            viewModel.isPasswordValidationPassed.value!!
                    && viewModel.isRePasswordValidationPassed.value!!
        )

    }


}