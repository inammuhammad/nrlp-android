package com.onelink.nrlp.android.viewmodel

import com.onelink.nrlp.android.features.profile.repo.ProfileRepo
import com.onelink.nrlp.android.features.profile.viewmodel.EditProfileViewModel
import com.onelink.nrlp.android.base.BaseViewModelTest
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito

class EditProfileViewModelTest : BaseViewModelTest() {

    companion object {
        private const val CNIC = "34101-5417680-8"

        private const val ALIAS = "alias"

        private const val COUNTRY = "Pakistan"

        private const val MOBILE_NUMBER_OLD = "3317887777"

        private const val MOBILE_NUMBER = "3317887766"

        private const val EMAIL_OLD = "oldemail@email.com"

        private const val HINT_LENGTH = 5
    }

    private lateinit var viewModel: EditProfileViewModel
    private lateinit var profileRepo: ProfileRepo


    override fun setUp() {
        super.setUp()
        profileRepo = Mockito.mock(ProfileRepo::class.java)
        viewModel = EditProfileViewModel(profileRepo)
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

        assertTrue(viewModel.validationEmailPassed.value != false)

        assertTrue(viewModel.validationPhoneNumberPassed.value != false)


        viewModel.validationEmailPassed.value = false

        viewModel.validationPhoneNumberPassed.value = false

        val isPassed = viewModel.validationsPassed(
            EMAIL_OLD,
            MOBILE_NUMBER_OLD,
            MOBILE_NUMBER_OLD.length
        )

        assertTrue(isPassed)

        assertNotNull(viewModel.validationEmailPassed.value)

        assertTrue(viewModel.validationEmailPassed.value!!)

        assertNotNull(viewModel.validationPhoneNumberPassed.value)

        assertTrue(viewModel.validationPhoneNumberPassed.value!!)

        assertTrue(
            viewModel.validationPhoneNumberPassed.value!!
                    && viewModel.validationEmailPassed.value!!
        )
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