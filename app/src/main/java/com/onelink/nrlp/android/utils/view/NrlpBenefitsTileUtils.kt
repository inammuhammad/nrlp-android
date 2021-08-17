package com.onelink.nrlp.android.utils.view

import androidx.annotation.Keep
import com.onelink.nrlp.android.R

@Keep
data class PartnersTileModel(val key : Int, val string : String, val drawableId : Int)

class NrlpBenefitsTileUtils {
    companion object {
        private const val FBR = 1
        private const val PIA = 2
        private const val PCA = 3
        private const val NADRA = 4

        fun getPartnersList() : MutableList<PartnersTileModel> {
            val homeTilesList = mutableListOf<PartnersTileModel>()
            homeTilesList.add(PartnersTileModel(FBR, "Federal Board of Revenue", R.drawable.ic_loyalty_statement_tile))
            homeTilesList.add(PartnersTileModel(PIA, "Pakistan International Airlines", R.drawable.ic_nrlp_benefits_tile))
            homeTilesList.add(PartnersTileModel(PCA, "Pakistan Civil Aviation Authority", R.drawable.ic_nrlp_benefits_tile))
            homeTilesList.add(PartnersTileModel(NADRA,"NADRA", R.drawable.ic_nrlp_benefits_tile))
            return homeTilesList
        }
    }
}