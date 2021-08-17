package com.onelink.nrlp.android.features.home.fragments

import android.os.Bundle
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.features.home.fragments.adapters.HomeTilesAdapter
import com.onelink.nrlp.android.features.nrlpBenefits.view.NrlpBenefitsActivity
import com.onelink.nrlp.android.features.viewStatement.view.ViewStatementActivity
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.toSpanned
import com.onelink.nrlp.android.utils.view.HomeTileUtils
import com.onelink.nrlp.android.utils.view.hometiles.HomeTileModel
import dagger.android.support.AndroidSupportInjection

/**
 * Created by Umar Javed.
 */

class BeneficiaryHomeFragment : HomeFragment() {

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        homeTilesList = HomeTileUtils.getBeneficiaryHomeTilesList()
        val homeTilesAdapter = HomeTilesAdapter(activity, homeTilesList, ::onHomeTileClicked)
        binding.homeTilesGrid.isExpanded = true
        binding.homeTilesGrid.adapter = homeTilesAdapter
    }

    private fun onHomeTileClicked(homeTile: HomeTileModel) {
        when (homeTile.key) {
            HomeTileUtils.VIEW_STATEMENT_BENEFICIARY -> launchViewStatement()
            HomeTileUtils.VIEW_NRLP_BENEFITS_BENEFICIARY -> launchNrlpBenefits()
            else -> return
        }
    }

    private fun launchViewStatement() {
        activity?.let {
            it.startActivity(ViewStatementActivity.newViewStatementIntent(it))
        }
    }

    private fun launchNrlpBenefits() {
        activity?.let {
            it.startActivity(NrlpBenefitsActivity.newViewStatementIntent(it))
        }
    }

    companion object {
        private const val TAG = "beneficiaryHomeFragment"
        private const val COMING_SOON = 0x324

        @JvmStatic
        fun newInstance() = BeneficiaryHomeFragment()
    }
}
