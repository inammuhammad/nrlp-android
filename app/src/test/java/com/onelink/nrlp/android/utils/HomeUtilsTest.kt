package com.onelink.nrlp.android.utils

import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.utils.view.HomeTileUtils
import com.onelink.nrlp.android.utils.view.hometiles.HomeTileModel
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class HomeUtilsTest {

    @Mock
    lateinit var remitterTiles: MutableList<HomeTileModel>

    @Mock
    lateinit var beneficiaryTiles: MutableList<HomeTileModel>


    @Before
    fun setUp() {

        remitterTiles = mutableListOf(
            HomeTileModel(
                HomeTileUtils.MANAGE_BENEFICIARY,
                R.string.manage_beneficiary_title,
                R.drawable.ic_beneficiaries_tile
            ),
            HomeTileModel(
                HomeTileUtils.MANAGE_POINTS,
                R.string.transfer_points,
                R.drawable.ic_loyalty_points_tile
            ),
            HomeTileModel(
                HomeTileUtils.VIEW_STATEMENT_REMITTER,
                R.string.view_stmnt,
                R.drawable.ic_loyalty_statement_tile
            ),
            HomeTileModel(
                HomeTileUtils.VIEW_NRLP_BENEFITS_REMITTER,
                R.string.view_nrlp_benefits,
                R.drawable.ic_nrlp_benefits_tile
            ),
            HomeTileModel(
                HomeTileUtils.SELF_AWARD_POINTS,
                R.string.self_award_points,
                R.drawable.ic_self_award_points_tile
            )
        )

        beneficiaryTiles = mutableListOf(
            HomeTileModel(
                HomeTileUtils.VIEW_STATEMENT_BENEFICIARY,
                R.string.view_stmnt,
                R.drawable.ic_loyalty_statement_tile
            ),
            HomeTileModel(
                HomeTileUtils.VIEW_NRLP_BENEFITS_BENEFICIARY,
                R.string.view_nrlp_benefits,
                R.drawable.ic_nrlp_benefits_tile
            )

        )

    }


    @Test
    fun getRemitterHomeTilesSuccess() =
        assertEquals(remitterTiles, HomeTileUtils.getRemitterHomeTilesList())

    @Test
    fun getRemitterHomeTilesFailed() =
        assertNotEquals(beneficiaryTiles, HomeTileUtils.getRemitterHomeTilesList())

    @Test
    fun getBeneficiaryHomeTileSuccess() =
        assertEquals(beneficiaryTiles, HomeTileUtils.getBeneficiaryHomeTilesList())

    @Test
    fun getBeneficiaryHomeTileFailed() =
        assertNotEquals(remitterTiles, HomeTileUtils.getBeneficiaryHomeTilesList())

}