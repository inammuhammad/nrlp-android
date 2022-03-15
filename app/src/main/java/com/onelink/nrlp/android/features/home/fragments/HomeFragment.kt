package com.onelink.nrlp.android.features.home.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.data.local.UserData
import androidx.lifecycle.Observer
import com.onelink.nrlp.android.data.local.UserModel
import com.onelink.nrlp.android.databinding.HomeFragmentBinding
import com.onelink.nrlp.android.features.home.view.HomeActivity
import com.onelink.nrlp.android.features.receiver.view.ReceiverActivity
import com.onelink.nrlp.android.features.redeem.view.RedeemActivity
import com.onelink.nrlp.android.utils.*
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import com.onelink.nrlp.android.utils.view.hometiles.HomeTileModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.ly_loyalty_level_title.view.*
import java.lang.Exception
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject


open class HomeFragment :
    BaseFragment<HomeFragmentViewModel, HomeFragmentBinding>(HomeFragmentViewModel::class.java) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

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
        oneLinkProgressDialog.showProgressDialog(activity)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        UserData.getUser()?.let {
            context?.let { context ->
                binding.ivHomeBgLoyaltyCard.setLoyaltyCard(context, it.loyaltyLevel)
               /* binding.lyTitle.ivLoyaltyCardTitle.setLoyaltyTitleBackground(
                    context,
                    it.loyaltyLevel
                )*/
            }
          //  binding.lyTitle.tvLoyaltyLevel.text = it.loyaltyLevel.capitalize(Locale.getDefault())
        }


        binding.tvRedeemPoints.setOnSingleClickListener {
//            showComingSoonDialog()
            launchRedeemPoints()
            //viewModel.navigateNadraVerification(fragmentHelper)
        }

        binding.ivRightArrow.setOnSingleClickListener {
         //   launchRedeemPoints()
//            showComingSoonDialog()
        }

        //showUserData()

        viewModel.observeUserProfile().observe(this, Observer { response ->
            if (response.status == Status.SUCCESS) {
                oneLinkProgressDialog.hideProgressDialog()
                showUserData()
            }
        })
    }

    private fun showUserData() {
        UserData.getUser()?.let {
            checkNadraVerification(it)
            if(it.accountType != Constants.BENEFICIARY.toLowerCase(Locale.getDefault()))
                checkReceiverAdded(it)
            if(it.accountType == "beneficiary") {
                binding.containerAnnualRemittance.invisible()
            }
            else {
                binding.containerAnnualRemittance.visible()
            }
            binding.tvName.text = it.fullName
            binding.tvPoints.text = it.loyaltyPoints?.roundOff()?.toFormattedAmount()
            binding.tvAnnualRemittance.text = "USD " + it.usdBalance?.roundOff()?.toFormattedAmount()
            binding.tvMemberSince.text = it.memberSince
            binding.icHelpUSD.setOnClickListener {
                showGeneralAlertDialog(this,"USD",getString(R.string.help_usd))
            }
            context?.let { context ->
                binding.ivHomeBgLoyaltyCard.setLoyaltyCard(context, it.loyaltyLevel)
               /* binding.lyTitle.ivLoyaltyCardTitle.setLoyaltyTitleBackground(
                    context,
                    it.loyaltyLevel
                )*/
            }
           // binding.lyTitle.tvLoyaltyLevel.text = it.loyaltyLevel.capitalize(Locale.getDefault())
        }
    }

    private fun launchRedeemPoints() {
        activity?.let {
            it.startActivity(RedeemActivity.newRedeemIntent(it))
        }
    }

    private fun checkNadraVerification(userModel: UserModel){
        try {
            if (userModel.requireNadraVerification!!)
                viewModel.navigateNadraVerification(fragmentHelper)
        }catch(e: Exception){}
    }

    private fun checkReceiverAdded(userModel: UserModel){
        try {
            if (userModel.receiverCount!! == 0) {
                val remittanceReceiverSP =
                    activity?.getSharedPreferences("remittanceReceiverSp", Context.MODE_PRIVATE)
                val limit = remittanceReceiverSP?.getBoolean("remitterPopupDisplayed", true)
                remittanceReceiverSP?.edit()?.putBoolean("remitterPopupDisplayed", false)?.commit()
                try {
                    if (limit!!) {
                        val intent = Intent(activity, ReceiverActivity::class.java)
                        intent.putExtra("isFromHomeScreen", true)
                        startActivity(intent)
                    }
                } catch (e: Exception) {}
            }
        }catch (e: Exception){}
    }

    override fun refresh() {
        viewModel.getUserProfile()
        super.refresh()
    }

    override fun onResume() {
        super.onResume()
        oneLinkProgressDialog.showProgressDialog(context)
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
