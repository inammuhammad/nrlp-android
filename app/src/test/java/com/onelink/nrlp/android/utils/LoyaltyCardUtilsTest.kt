package com.onelink.nrlp.android.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.onelink.nrlp.android.R
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations

class LoyaltyCardUtilsTest {

    companion object {
        private const val IC_BRONZE = R.drawable.ic_loyalty_points_bronze

        private const val IC_GOLD = R.drawable.ic_loyalty_points_gold

        private const val IC_PLATINUM = R.drawable.ic_loyalty_points_platinum

        private const val IC_SILVER = R.drawable.ic_loyalty_points_silver

        private const val BG_BRONZE = R.drawable.ic_title_bronze

        private const val BG_GOLD = R.drawable.ic_title_gold

        private const val BG_PLATINUM = R.drawable.ic_title_platinum

        private const val BG_SILVER = R.drawable.ic_title_silver
    }

    @get:Rule
    var rule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun getLoyaltyCardBackgroundTest() {

        assertEquals(
            IC_BRONZE,
            getLoyaltyCardBackground(LoyaltyCardConstants.BRONZE)
        )

        assertEquals(
            IC_GOLD,
            getLoyaltyCardBackground(LoyaltyCardConstants.GOLD)
        )

        assertEquals(
            IC_PLATINUM,
            getLoyaltyCardBackground(LoyaltyCardConstants.PLATINUM)
        )

        assertEquals(
            IC_SILVER,
            getLoyaltyCardBackground(LoyaltyCardConstants.SILVER)
        )
    }

    @Test
    fun getLoyaltyTitleBackgroundTest() {
        assertEquals(
            BG_BRONZE,
            getLoyaltyTitleBackground(LoyaltyCardConstants.BRONZE)
        )

        assertEquals(
            BG_GOLD,
            getLoyaltyTitleBackground(LoyaltyCardConstants.GOLD)
        )

        assertEquals(
            BG_PLATINUM,
            getLoyaltyTitleBackground(LoyaltyCardConstants.PLATINUM)
        )

        assertEquals(
            BG_SILVER,
            getLoyaltyTitleBackground(LoyaltyCardConstants.SILVER)
        )
    }
}