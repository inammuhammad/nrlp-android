package com.onelink.nrlp.android.features.rate.fragments

import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.databinding.FragmentSelfAwardRatingBinding
import com.onelink.nrlp.android.features.rate.viewmodels.RateViewModel
import com.onelink.nrlp.android.features.selfAwardPoints.viewmodel.SelfAwardPointsFragmentViewModel

class TransactionRatingFragment : BaseFragment<RateViewModel, FragmentSelfAwardRatingBinding>
    (RateViewModel::class.java) {

    override fun getLayoutRes() = R.layout.fragment_self_award_rating

    companion object {
        @JvmStatic
        fun newInstance() = TransactionRatingFragment()
    }

}