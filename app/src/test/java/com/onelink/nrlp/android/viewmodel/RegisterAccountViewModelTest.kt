package com.onelink.nrlp.android.viewmodel

import com.onelink.nrlp.android.features.register.viewmodel.RegisterAccountFragmentViewModel
import com.onelink.nrlp.android.base.BaseViewModelTest
import com.onelink.nrlp.android.features.splash.repo.AuthKeyRepo
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito

class RegisterAccountViewModelTest : BaseViewModelTest() {

    private lateinit var viewModel: RegisterAccountFragmentViewModel
    private lateinit var authKeyRepo: AuthKeyRepo


    override fun setUp() {
        super.setUp()
        authKeyRepo = Mockito.mock(AuthKeyRepo::class.java)
        viewModel = RegisterAccountFragmentViewModel(authKeyRepo)
    }

    @Test
    fun phoneNumberHintSuccess() = assertEquals(
        viewModel.phoneNumberHint(HINT_LENGTH).length,
        HINT_LENGTH
    )


    @Test
    fun phoneNumberHintFailed() = assertNotEquals(
        viewModel.phoneNumberHint(0),
        HINT_LENGTH
    )


    @Test
    fun validationPassedSuccess() {

        assertTrue(viewModel.validationCnicPassed.value != false)

        assertTrue(viewModel.validationPasswordPassed.value != false)

        assertTrue(viewModel.validationPhoneNumberPassed.value != false)

        assertTrue(viewModel.validationEmailPassed.value != false)

        assertTrue(viewModel.validationFullNamePassed.value != false)

        assertTrue(viewModel.validationRePasswordPassed.value != false)



        viewModel.validationCnicPassed.value = false

        viewModel.validationPasswordPassed.value = false

        viewModel.validationPhoneNumberPassed.value = false

        viewModel.validationEmailPassed.value = false

        viewModel.validationFullNamePassed.value = false

        viewModel.validationRePasswordPassed.value = false

        val isPassed = viewModel.validationsPassed(
            CNIC,
            FULL_NAME,
            MOBILE_NUMBER,
            MOBILE_NUMBER.length,
            EMAIL,
            PASSWORD,
            RE_PASSWORD
        )

        assertTrue(isPassed)

        assertNotNull(viewModel.validationCnicPassed.value)

        assertTrue(viewModel.validationCnicPassed.value!!)

        assertNotNull(viewModel.validationPasswordPassed.value)

        assertTrue(viewModel.validationPasswordPassed.value!!)

        assertNotNull(viewModel.validationPhoneNumberPassed.value)

        assertTrue(viewModel.validationPhoneNumberPassed.value!!)


        assertNotNull(viewModel.validationEmailPassed.value)

        assertTrue(viewModel.validationEmailPassed.value!!)

        assertNotNull(viewModel.validationFullNamePassed.value)

        assertTrue(viewModel.validationFullNamePassed.value!!)

        assertNotNull(viewModel.validationRePasswordPassed.value)

        assertTrue(viewModel.validationRePasswordPassed.value!!)

        assertTrue(
            viewModel.validationCnicPassed.value!!
                    && viewModel.validationPasswordPassed.value!!
                    && viewModel.validationPhoneNumberPassed.value!!
                    && viewModel.validationEmailPassed.value!!
                    && viewModel.validationFullNamePassed.value!!
                    && viewModel.validationRePasswordPassed.value!!
        )

    }

    @Test
    fun validationCNICFailed() {
        assertTrue(viewModel.validationCnicPassed.value != false)
        viewModel.validationCnicPassed.value = false

        assertFalse(
            viewModel.validationsPassed(
                CNIC_INVALID,
                FULL_NAME,
                MOBILE_NUMBER,
                MOBILE_NUMBER.length,
                EMAIL,
                PASSWORD,
                RE_PASSWORD
            )
        )

        assertNotNull(viewModel.validationCnicPassed.value)

        assertFalse(viewModel.validationCnicPassed.value!!)

    }

    @Test
    fun validationFullNameFailed() {
        assertTrue(viewModel.validationFullNamePassed.value != false)
        viewModel.validationFullNamePassed.value = false

        val isPassed = viewModel.validationsPassed(
            CNIC,
            FULL_NAME_INVALID,
            MOBILE_NUMBER,
            MOBILE_NUMBER.length,
            EMAIL,
            PASSWORD,
            RE_PASSWORD
        )

        assertFalse(isPassed)

        assertNotNull(viewModel.validationFullNamePassed.value)

        assertFalse(viewModel.validationFullNamePassed.value!!)

    }

    @Test
    fun validationPhoneNumberFailed() {
        assertTrue(viewModel.validationPhoneNumberPassed.value != false)
        viewModel.validationPhoneNumberPassed.value = false

        val isPassed = viewModel.validationsPassed(
            CNIC,
            FULL_NAME,
            MOBILE_NUMBER_INVALID,
            MOBILE_NUMBER.length,
            EMAIL,
            PASSWORD,
            RE_PASSWORD
        )

        assertFalse(isPassed)

        assertNotNull(viewModel.validationPhoneNumberPassed.value)

        assertFalse(viewModel.validationPhoneNumberPassed.value!!)

    }

    @Test
    fun cnicNumberEmitSuccess() {

        assertNotNull(viewModel.cnicNicopNumber)

        assertTrue(viewModel.cnicNicopNumber.value?.isEmpty() != false)

        viewModel.cnicNicopNumber.value = CNIC

        assertEquals(viewModel.cnicNicopNumber.value, CNIC)

        assertNotNull(
            viewModel.cnicNicopNumber.value
        )

    }

    @Test
    fun cnicNumberEmitFail() {

        assertFalse(viewModel.cnicNicopNumber.value?.isNotEmpty() == true)

        assertNotEquals(viewModel.cnicNicopNumber.value, CNIC)

    }

    @Test
    fun fullNameEmitSuccess() {

        assertNotNull(viewModel.fullName)

        assertTrue(viewModel.fullName.value?.isEmpty() != false)

        viewModel.fullName.value = FULL_NAME

        assertEquals(viewModel.fullName.value, FULL_NAME)

        assertNotNull(
            viewModel.fullName.value
        )

    }

    @Test
    fun fullNameEmitFail() {

        assertFalse(viewModel.fullName.value?.isNotEmpty() == true)

        assertNotEquals(viewModel.fullName.value, FULL_NAME)

    }

    @Test
    fun countryEmitSuccess() {

        assertNotNull(viewModel.country)

        assertTrue(viewModel.country.value?.isEmpty() != false)

        viewModel.country.value = COUNTRY

        assertEquals(viewModel.country.value, COUNTRY)

        assertNotNull(
            viewModel.country.value
        )

    }

    @Test
    fun countryEmitFail() {

        assertFalse(viewModel.country.value?.isNotEmpty() == true)

        assertNotEquals(viewModel.country.value, COUNTRY)

    }

    fun mobileNumberEmitSuccess() {

        assertNotNull(viewModel.mobileNumber)

        assertTrue(viewModel.mobileNumber.value?.isEmpty() != false)

        viewModel.mobileNumber.value = MOBILE_NUMBER

        assertEquals(viewModel.mobileNumber.value, MOBILE_NUMBER)

        assertNotNull(
            viewModel.mobileNumber.value
        )

    }

    @Test
    fun mobileNumberEmitFail() {

        assertFalse(viewModel.mobileNumber.value?.isNotEmpty() == true)

        assertNotEquals(viewModel.mobileNumber.value, MOBILE_NUMBER)

    }


    companion object {

        private const val CNIC = "34101-5417680-8"

        private const val CNIC_INVALID = "341015417688"

        private const val FULL_NAME = "alias"

        private const val EMAIL = "test@gmail.com"

        private const val FULL_NAME_INVALID = "alias123@"

        private const val COUNTRY = "Pakistan"

        private const val MOBILE_NUMBER = "3317887777"

        private const val MOBILE_NUMBER_INVALID = "a3317887777"

        private const val HINT_LENGTH = 5

        private const val PASSWORD = "Hello@123"

        private const val RE_PASSWORD = "Hello@123"
    }

}