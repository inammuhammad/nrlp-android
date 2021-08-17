package com.onelink.nrlp.android.viewmodel

import com.onelink.nrlp.android.features.login.repo.LoginRepo
import com.onelink.nrlp.android.features.login.viewmodel.LoginFragmentViewModel
import com.onelink.nrlp.android.base.BaseViewModelTest
import com.onelink.nrlp.android.features.splash.repo.AuthKeyRepo
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito

class LoginViewModelTest : BaseViewModelTest() {

    companion object {
        private const val CNIC = "34101-5417680-8"

        private const val CNIC_INVALID = "341015417688"

        private const val PASSWORD = "Hello@123"

    }

    private lateinit var viewModel: LoginFragmentViewModel
    private lateinit var loginRepo: LoginRepo
    private lateinit var authKeyRepo: AuthKeyRepo


    override fun setUp() {
        super.setUp()
        loginRepo = Mockito.mock(LoginRepo::class.java)
        authKeyRepo = Mockito.mock(AuthKeyRepo::class.java)
        viewModel = LoginFragmentViewModel(loginRepo, authKeyRepo)
    }

    @Test
    fun validationPassedSuccess() {

        assertTrue(viewModel.validationCnicPassed.value != false)

        assertTrue(viewModel.validationPasswordPassed.value != false)

        assertTrue(viewModel.validationSelectAccountPassed.value != false)

        viewModel.validationCnicPassed.value = false

        viewModel.validationPasswordPassed.value = false

        viewModel.validationSelectAccountPassed.value = false

        val isPassed = viewModel.validationsPassed(
            1,
            CNIC,
            PASSWORD
        )

        assertTrue(isPassed)

        assertNotNull(viewModel.validationCnicPassed.value)

        assertTrue(viewModel.validationCnicPassed.value!!)

        assertNotNull(viewModel.validationPasswordPassed.value)

        assertTrue(viewModel.validationPasswordPassed.value!!)

        assertNotNull(viewModel.validationSelectAccountPassed.value)

        assertTrue(viewModel.validationSelectAccountPassed.value!!)

    }


    @Test
    fun validationPassedFailed() {

        assertFalse(viewModel.validationCnicPassed.value == false)

        assertFalse(viewModel.validationPasswordPassed.value == false)

        assertFalse(viewModel.validationSelectAccountPassed.value == false)

        val isPassed = viewModel.validationsPassed(
            1,
            CNIC_INVALID,
            PASSWORD
        )

        assertFalse(isPassed)

    }


}