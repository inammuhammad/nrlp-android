package com.onelink.nrlp.android.features.redeem.fragments

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.databinding.FragmentRedemptionUscDescriptionBinding
import com.onelink.nrlp.android.features.redeem.viewmodels.RedeemSharedViewModel
import com.onelink.nrlp.android.features.redeem.viewmodels.RedemptionPIADescriptionViewModel
import com.onelink.nrlp.android.features.redeem.viewmodels.RedemptionUSCDescriptionViewModel
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import com.onelink.nrlp.android.utils.roundOff
import com.onelink.nrlp.android.utils.setLoyaltyCard
import com.onelink.nrlp.android.utils.setLoyaltyCardBackground
import com.onelink.nrlp.android.utils.toFormattedAmount
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.ly_loyalty_points_balance.view.*
import javax.inject.Inject

class RedemptionUSCDescriptionFragment : BaseFragment<RedemptionUSCDescriptionViewModel,FragmentRedemptionUscDescriptionBinding>
    (RedemptionUSCDescriptionViewModel::class.java)  {

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var redeemSharedViewModel: RedeemSharedViewModel? = null

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() = RedemptionUSCDescriptionFragment()
    }

    override fun getTitle(): String = resources.getString(R.string.redeem)
    override fun getLayoutRes() = R.layout.fragment_redemption_usc_description

    override fun getViewM(): RedemptionUSCDescriptionViewModel =
        ViewModelProvider(this,viewModelFactory).get(RedemptionUSCDescriptionViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        UserData.getUser()?.let {
            binding.lyLoyaltyPointsBalance.tvPoints.text =
                it.loyaltyPoints?.roundOff()?.toFormattedAmount()
            binding.lyLoyaltyPointsBalance.tvName.text = it.fullName
            binding.lyLoyaltyPointsBalance.tvMemberSince.text = it.memberSince
            context?.let { context ->
                binding.lyLoyaltyPointsBalance.ivHomeBgLoyaltyCard.setLoyaltyCard(
                    context, it.loyaltyLevel
                )
            }
        }
        activity?.let {
            redeemSharedViewModel = ViewModelProvider(it).get(RedeemSharedViewModel::class.java)
        }
        binding.btnPositive.setOnClickListener {
            viewModel.addNextFragment(fragmentHelper)
        }
        binding.btnNegative.setOnClickListener {
            fragmentHelper.onBack()
        }
    }

}