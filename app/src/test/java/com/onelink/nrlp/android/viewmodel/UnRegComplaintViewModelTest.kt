package com.onelink.nrlp.android.viewmodel

import com.onelink.nrlp.android.base.BaseViewModelTest
import com.onelink.nrlp.android.features.complaint.repo.ComplainRepo
import com.onelink.nrlp.android.features.complaint.viewmodel.UnRegComplaintSharedViewModel
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

class UnRegComplaintViewModelTest : BaseViewModelTest() {

    companion object {
        private const val CNIC = "34101-5417680-8"

        private const val ALIAS = "alias"

        private const val COUNTRY = "Pakistan"

        private const val MOBILE_NUMBER = "3317887766"

        private const val EMAIL = "oldemail@email.com"
        
        private const val DETAILS="this is sample complaint"
        
        private const val MOBILE_OPERATOR="Zong"

        private const val HINT_LENGTH = 5
    }

    private lateinit var viewModel: UnRegComplaintSharedViewModel
    private lateinit var complainRepo: ComplainRepo


    override fun setUp() {
        super.setUp()
        complainRepo = Mockito.mock(ComplainRepo::class.java)
        viewModel = UnRegComplaintSharedViewModel(complainRepo)
    }

    @Test
    fun phoneNumberHintSuccess() = Assert.assertEquals(
        viewModel.phoneNumberHint(HINT_LENGTH).length,
        HINT_LENGTH
    )


    @Test
    fun phoneNumberHintFailed() = Assert.assertNotEquals(
        viewModel.phoneNumberHint(0),HINT_LENGTH
    )


    @Test
    fun validationPassedSuccess() {

        Assert.assertTrue(viewModel.validationEmailPassed.value != false)

        Assert.assertTrue(viewModel.validationPhoneNumberPassed.value != false)


        viewModel.validationEmailPassed.value = false

        viewModel.validationPhoneNumberPassed.value = false

       /* val isPassed = viewModel.detailsValidationsPassed(
            UnRegComplaintViewModelTest.EMAIL_OLD,
            UnRegComplaintViewModelTest.MOBILE_NUMBER_OLD,
            UnRegComplaintViewModelTest.MOBILE_NUMBER_OLD.length
        )

        Assert.assertTrue(isPassed)*/

        Assert.assertNotNull(viewModel.validationEmailPassed.value)

        Assert.assertTrue(viewModel.validationEmailPassed.value!!)

        Assert.assertNotNull(viewModel.validationPhoneNumberPassed.value)

        Assert.assertTrue(viewModel.validationPhoneNumberPassed.value!!)

        Assert.assertTrue(
            viewModel.validationPhoneNumberPassed.value!!
                    && viewModel.validationEmailPassed.value!!
        )
    }


    @Test
    fun cnicNumberEmitSuccess() {

        Assert.assertNotNull(viewModel.cnicNumber)

        Assert.assertTrue(viewModel.cnicNumber.value?.isEmpty() != false)

        viewModel.cnicNumber.value = CNIC

        Assert.assertEquals(viewModel.cnicNumber.value, CNIC)

        Assert.assertNotNull(
            viewModel.cnicNumber.value
        )

    }

    @Test
    fun cnicNumberEmitFail() {

        Assert.assertFalse(viewModel.cnicNumber.value?.isNotEmpty() == true)

        Assert.assertNotEquals(viewModel.cnicNumber.value, CNIC)

    }

    @Test
    fun aliasEmitSuccess() {

        Assert.assertNotNull(viewModel.alias)

        Assert.assertTrue(viewModel.alias.value?.isEmpty() != false)

        viewModel.alias.value = ALIAS

        Assert.assertEquals(viewModel.alias.value, ALIAS)

        Assert.assertNotNull(
            viewModel.alias.value
        )

    }

    @Test
    fun aliasEmitFail() {

        Assert.assertFalse(viewModel.alias.value?.isNotEmpty() == true)

        Assert.assertNotEquals(viewModel.alias.value, ALIAS)

    }



    @Test
    fun countryEmitSuccess() {

        Assert.assertNotNull(viewModel.country)

        Assert.assertTrue(viewModel.country.value?.isEmpty() != false)

        viewModel.country.value = COUNTRY

        Assert.assertEquals(viewModel.country.value, COUNTRY)

        Assert.assertNotNull(
            viewModel.country.value
        )

    }

    @Test
    fun countryEmitFail() {

        Assert.assertFalse(viewModel.country.value?.isNotEmpty() == true)

        Assert.assertNotEquals(viewModel.country.value, COUNTRY)

    }

    fun mobileNumberEmitSuccess() {

        Assert.assertNotNull(viewModel.mobileNumber)

        Assert.assertTrue(viewModel.mobileNumber.value?.isEmpty() != false)

        viewModel.mobileNumber.value = MOBILE_NUMBER

        Assert.assertEquals(viewModel.mobileNumber.value, MOBILE_NUMBER)

        Assert.assertNotNull(
            viewModel.mobileNumber.value
        )

    }

    @Test
    fun mobileNumberEmitFail() {

        Assert.assertFalse(viewModel.mobileNumber.value?.isNotEmpty() == true)

        Assert.assertNotEquals(viewModel.mobileNumber.value, MOBILE_NUMBER)

    }
}