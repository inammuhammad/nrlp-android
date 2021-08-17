package com.onelink.nrlp.android.viewmodel

import com.onelink.nrlp.android.features.beneficiary.repo.BeneficiaryRepo
import com.onelink.nrlp.android.features.beneficiary.viewmodel.BeneficiaryDetailsViewModel
import com.onelink.nrlp.android.base.BaseViewModelTest
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito

class BeneficiaryDetailsViewModelTest : BaseViewModelTest() {

    companion object {

        private const val CNIC = "34101-5417680-8"

        private const val CNIC_INVALID = "341015417688"

        private const val ALIAS = "alias"

        private const val ALIAS_INVALID = "alias123@"

        private const val COUNTRY = "Pakistan"

        private const val MOBILE_NUMBER = "3317887777"

        private const val MOBILE_NUMBER_INVALID = "a3317887777"

        private const val HINT_LENGTH = 5
    }

    private lateinit var viewModel: BeneficiaryDetailsViewModel
    private lateinit var beneficiaryRepo: BeneficiaryRepo


    override fun setUp() {
        super.setUp()
        beneficiaryRepo = Mockito.mock(BeneficiaryRepo::class.java)
        viewModel = BeneficiaryDetailsViewModel(beneficiaryRepo)
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

        assertTrue(viewModel.validationAliasPassed.value != false)

        assertTrue(viewModel.validationPhoneNumberPassed.value != false)

        viewModel.validationCnicPassed.value = false

        viewModel.validationAliasPassed.value = false

        viewModel.validationPhoneNumberPassed.value = false

        val isPassed = viewModel.validationsPassed(
            CNIC,
            ALIAS,
            MOBILE_NUMBER,
            MOBILE_NUMBER.length
        )

        assertTrue(isPassed)

        assertNotNull(viewModel.validationCnicPassed.value)

        assertTrue(viewModel.validationCnicPassed.value!!)

        assertNotNull(viewModel.validationAliasPassed.value)

        assertTrue(viewModel.validationAliasPassed.value!!)

        assertNotNull(viewModel.validationPhoneNumberPassed.value)

        assertTrue(viewModel.validationPhoneNumberPassed.value!!)

    }

    @Test
    fun validationCNICFailed() {
        assertTrue(viewModel.validationCnicPassed.value != false)
        viewModel.validationCnicPassed.value = false

        assertFalse(
            viewModel.validationsPassed(
                CNIC_INVALID,
                ALIAS,
                MOBILE_NUMBER,
                MOBILE_NUMBER.length
            )
        )

        assertNotNull(viewModel.validationCnicPassed.value)

        assertFalse(viewModel.validationCnicPassed.value!!)

    }

    @Test
    fun validationAliasFailed() {
        assertTrue(viewModel.validationAliasPassed.value != false)
        viewModel.validationAliasPassed.value = false

        val isPassed = viewModel.validationsPassed(
            CNIC_INVALID,
            ALIAS_INVALID,
            MOBILE_NUMBER,
            MOBILE_NUMBER.length
        )

        assertFalse(isPassed)

        assertNotNull(viewModel.validationAliasPassed.value)

        assertFalse(viewModel.validationAliasPassed.value!!)

    }

    @Test
    fun validationPhoneNumberFailed() {
        assertTrue(viewModel.validationPhoneNumberPassed.value != false)
        viewModel.validationPhoneNumberPassed.value = false

        val isPassed = viewModel.validationsPassed(
            CNIC_INVALID,
            ALIAS_INVALID,
            MOBILE_NUMBER_INVALID,
            MOBILE_NUMBER.length
        )

        assertFalse(isPassed)

        assertNotNull(viewModel.validationPhoneNumberPassed.value)

        assertFalse(viewModel.validationPhoneNumberPassed.value!!)

    }

    @Test
    fun cnicNumberEmitSuccess() {

        assertNotNull(viewModel.cnicNumber)

        assertTrue(viewModel.cnicNumber.value?.isEmpty() != false)

        viewModel.cnicNumber.value = CNIC

        assertEquals(viewModel.cnicNumber.value, CNIC)

        assertNotNull(
            viewModel.cnicNumber.value
        )

    }

    @Test
    fun cnicNumberEmitFail() {

        assertFalse(viewModel.cnicNumber.value?.isNotEmpty() == true)

        assertNotEquals(viewModel.cnicNumber.value, CNIC)

    }

    @Test
    fun aliasEmitSuccess() {

        assertNotNull(viewModel.alias)

        assertTrue(viewModel.alias.value?.isEmpty() != false)

        viewModel.alias.value = ALIAS

        assertEquals(viewModel.alias.value, ALIAS)

        assertNotNull(
            viewModel.alias.value
        )

    }

    @Test
    fun aliasEmitFail() {

        assertFalse(viewModel.alias.value?.isNotEmpty() == true)

        assertNotEquals(viewModel.alias.value, ALIAS)

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


}