package com.onelink.nrlp.android.utils.view

import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.utils.view.hometiles.HomeTileModel

class HomeTileUtils {
    companion object {
        const val MANAGE_BENEFICIARY = 1
        const val MANAGE_POINTS = 2
        const val VIEW_STATEMENT_REMITTER = 3
        const val VIEW_NRLP_BENEFITS_REMITTER = 4
        const val SELF_AWARD_POINTS = 5
        const val VIEW_STATEMENT_BENEFICIARY = 6
        const val VIEW_NRLP_BENEFITS_BENEFICIARY = 7

        fun getRemitterHomeTilesList() : MutableList<HomeTileModel> {
            val homeTilesList = mutableListOf<HomeTileModel>()
            homeTilesList.add(HomeTileModel(MANAGE_BENEFICIARY, R.string.manage_beneficiary_title, R.drawable.ic_beneficiaries_tile))
          //  homeTilesList.add(HomeTileModel(MANAGE_POINTS, R.string.transfer_points, R.drawable.ic_loyalty_points_tile))
            homeTilesList.add(HomeTileModel(VIEW_STATEMENT_REMITTER, R.string.view_stmnt, R.drawable.ic_loyalty_statement_tile))
            homeTilesList.add(HomeTileModel(VIEW_NRLP_BENEFITS_REMITTER, R.string.view_nrlp_benefits, R.drawable.ic_nrlp_benefits_tile))
          //  homeTilesList.add(HomeTileModel(SELF_AWARD_POINTS, R.string.self_award_points, R.drawable.ic_self_award_points_update))
            return homeTilesList
        }

        fun getBeneficiaryHomeTilesList() : MutableList<HomeTileModel> {
            val homeTilesList = mutableListOf<HomeTileModel>()
            homeTilesList.add(HomeTileModel(VIEW_STATEMENT_BENEFICIARY, R.string.view_stmnt, R.drawable.ic_loyalty_statement_tile))
            homeTilesList.add(HomeTileModel(VIEW_NRLP_BENEFITS_BENEFICIARY, R.string.view_nrlp_benefits, R.drawable.ic_nrlp_benefits_tile))
            return homeTilesList
        }
    }
}