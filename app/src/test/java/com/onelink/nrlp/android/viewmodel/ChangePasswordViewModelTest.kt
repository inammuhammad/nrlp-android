package com.onelink.nrlp.android.viewmodel

import com.onelink.nrlp.android.features.changePassword.repo.ChangePasswordRepo
import com.onelink.nrlp.android.features.changePassword.viewmodel.ChangePasswordFragmentViewModel
import com.onelink.nrlp.android.base.BaseViewModelTest
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.mock

class ChangePasswordViewModelTest : BaseViewModelTest() {

    companion object {
        private const val PASSWORD = "Current@123"

        private const val NEW_PASSWORD = "NewPass@123"

        private const val RE_PASSWORD = "NewPass@123"

        private const val PASSWORD_INVALID = "hello@"
    }

    private lateinit var viewModel: ChangePasswordFragmentViewModel

    private lateinit var changePasswordRepo: ChangePasswordRepo


    override fun setUp() {
        super.setUp()
        this.changePasswordRepo = mock(ChangePasswordRepo::class.java)
        viewModel = ChangePasswordFragmentViewModel(changePasswordRepo)
    }


    @Test
    fun validationPassedSuccess() {

        assertTrue(viewModel.validationPasswordPassed.value != false)

        assertTrue(viewModel.validationRePasswordPassed.value != false)

        assertTrue(viewModel.validationOldPasswordPassed.value != false)

        viewModel.validationPasswordPassed.value = false

        viewModel.validationRePasswordPassed.value = false

        viewModel.validationOldPasswordPassed.value = false

        val isPassed = viewModel.validationsPassed(
            PASSWORD,
            NEW_PASSWORD,
            RE_PASSWORD
        )

        assertTrue(isPassed)

        assertNotNull(viewModel.validationPasswordPassed.value)

        assertTrue(viewModel.validationRePasswordPassed.value!!)

        assertNotNull(viewModel.validationOldPasswordPassed.value)

        assertTrue(viewModel.validationPasswordPassed.value!!)

        assertNotNull(viewModel.validationRePasswordPassed.value)

        assertTrue(viewModel.validationOldPasswordPassed.value!!)

    }

    @Test
    fun checkRePassValidationSuccess() = assertTrue(
        viewModel.checkRePassValidation(
            NEW_PASSWORD,
            RE_PASSWORD
        )
    )

    @Test
    fun checkRePassValidationFailed() = assertTrue(
        viewModel.checkRePassValidation(
            NEW_PASSWORD,
            RE_PASSWORD
        )
    )

    @Test
    fun passwordValidationFailed() = assertFalse(
        viewModel.checkPassValidation(
            PASSWORD_INVALID
        )
    )

}