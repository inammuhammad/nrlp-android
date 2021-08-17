package com.onelink.nrlp.android.features.home.fragments

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.databinding.HomeFragmentBinding
import com.onelink.nrlp.android.features.home.view.HomeActivity
import com.onelink.nrlp.android.features.redeem.view.RedeemActivity
import com.onelink.nrlp.android.utils.*
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.view.hometiles.HomeTileModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.ly_loyalty_level_title.view.*
import java.util.*
import javax.inject.Inject


open class HomeFragment :
    BaseFragment<HomeFragmentViewModel, HomeFragmentBinding>(HomeFragmentViewModel::class.java) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.home_fragment

    override fun getTitle() = resources.getString(R.string.home_title)

    protected var homeTilesList = mutableListOf<HomeTileModel>()

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getViewM(): HomeFragmentViewModel =
        ViewModelProvider(this, viewModelFactory).get(HomeFragmentViewModel::class.java)


    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        UserData.getUser()?.let {
            context?.let { context ->
                binding.ivHomeBgLoyaltyCard.setLoyaltyCardBackground(context, it.loyaltyLevel)
                binding.lyTitle.ivLoyaltyCardTitle.setLoyaltyTitleBackground(
                    context,
                    it.loyaltyLevel
                )
            }
            binding.lyTitle.tvLoyaltyLevel.text = it.loyaltyLevel.capitalize(Locale.getDefault())
        }


        binding.tvRedeemPoints.setOnSingleClickListener {
//            showComingSoonDialog()
            launchRedeemPoints()
        }

        binding.ivRightArrow.setOnSingleClickListener {
            launchRedeemPoints()
//            showComingSoonDialog()
        }

        showUserData()

        viewModel.observeUserProfile().observe(this, { response ->
            if (response.status == Status.SUCCESS) showUserData()
        })
    }

    private fun showUserData() {
        UserData.getUser()?.let {
            binding.tvUserName.text = it.fullName
            binding.tvPoints.text = it.loyaltyPoints?.roundOff()?.toFormattedAmount()
            context?.let { context ->
                binding.ivHomeBgLoyaltyCard.setLoyaltyCardBackground(context, it.loyaltyLevel)
                binding.lyTitle.ivLoyaltyCardTitle.setLoyaltyTitleBackground(
                    context,
                    it.loyaltyLevel
                )
            }
            binding.lyTitle.tvLoyaltyLevel.text = it.loyaltyLevel.capitalize(Locale.getDefault())
        }
    }

    private fun launchRedeemPoints() {
        activity?.let {
            it.startActivity(RedeemActivity.newRedeemIntent(it))
        }
    }

    override fun refresh() {
        viewModel.getUserProfile()
        super.refresh()
    }

     protected fun showComingSoonDialog() {
        OneLinkAlertDialogsFragment.Builder()
            .setIsAlertOnly(true).setDrawable(R.drawable.ic_coming_soon_alert)
            .setTitle(getString(R.string.coming_soon))
            .setMessage((getString(R.string.coming_soon_msg)).toSpanned())
            .setNeutralButtonText(getString(R.string.okay)).setNegativeButtonText("")
            .setPositiveButtonText("").setCancelable(false).show(
                childFragmentManager, TAG
            )
    }

    companion object {
        private const val TAG = "homeFragment"
        private const val COMING_SOON = 0x322
    }
}
