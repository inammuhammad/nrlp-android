package com.onelink.nrlp.android.viewmodel

import com.onelink.nrlp.android.features.viewStatement.repo.ViewStatementRepo
import com.onelink.nrlp.android.features.viewStatement.viewmodel.AdvancedLoyaltyStatementFragmentViewModel
import com.onelink.nrlp.android.base.BaseViewModelTest
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.mock
import java.util.*

class AdvancedLoyaltyStatementViewModelTest : BaseViewModelTest() {

    companion object {

        private const val EMAIL = "test@gmail.com"

        private const val EMAIL_INVALID = "test@gmailcom"

        private val CALENDAR = Calendar.getInstance()

        private const val DATE_FORMATTED = "17th February 2020"

        private const val DATE_INVALID = "17 February 2020"
    }

    private lateinit var viewModel: AdvancedLoyaltyStatementFragmentViewModel
    private lateinit var viewStatementRepo: ViewStatementRepo


    override fun setUp() {
        super.setUp()
        this.viewStatementRepo = mock(ViewStatementRepo::class.java)
        this.viewModel = AdvancedLoyaltyStatementFragmentViewModel(viewStatementRepo)
        this.viewModel.rawDate = "17/2/2020"
        CALENDAR.timeInMillis = System.currentTimeMillis()
    }


    @Test
    fun validationPassedSuccess() {

        assertTrue(viewModel.validationEmailPassed.value != false)

        viewModel.validationEmailPassed.value = false

        val isPassed = viewModel.validationsPassed(
            EMAIL
        )

        assertTrue(isPassed)

        assertNotNull(viewModel.validationEmailPassed.value)

        assertTrue(viewModel.validationEmailPassed.value!!)

    }

    @Test
    fun validationPassedFailed() {

        assertTrue(viewModel.validationEmailPassed.value != false)

        viewModel.validationEmailPassed.value = false

        val isPassed = viewModel.validationsPassed(
            EMAIL_INVALID
        )

        assertFalse(isPassed)

        assertNotNull(viewModel.validationEmailPassed.value)

        assertFalse(viewModel.validationEmailPassed.value!!)

    }

    @Test
    fun getDateInStringFormatSuccess() {
        val result = viewModel.getDateInStringFormat(CALENDAR)
        assertEquals(
            DATE_FORMATTED,
            result
        )
    }

    @Test
    fun getDateInStringFormatFailed() {
        val result = viewModel.getDateInStringFormat(CALENDAR)
        assertNotEquals(
            DATE_INVALID,
            result
        )
    }

}