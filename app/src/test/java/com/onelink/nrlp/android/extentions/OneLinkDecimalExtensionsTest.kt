package com.onelink.nrlp.android.extentions

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.onelink.nrlp.android.utils.roundOff
import com.onelink.nrlp.android.utils.toFormattedAmount
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations

class OneLinkDecimalExtensionsTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val testDecimal = 3.144359845830583.toBigDecimal()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun toFormattedAmountTest() {
        assertNotNull(
            testDecimal.toFormattedAmount()
        )

        assertEquals(
            "3.14",
            testDecimal.toFormattedAmount()
        )

        assertNotEquals(
            "314",
            testDecimal.toFormattedAmount()
        )
    }

    @Test
    fun roundOffTest() {

        assertNotNull(
            testDecimal.roundOff()
        )

        assertEquals(
            3.toBigDecimal(),
            testDecimal.roundOff()
        )

        assertNotEquals(
            3.14.toBigDecimal(),
            testDecimal.roundOff()
        )
    }


}