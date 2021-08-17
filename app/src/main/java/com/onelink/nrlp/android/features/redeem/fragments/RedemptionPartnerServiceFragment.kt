package com.onelink.nrlp.android.features.redeem.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.databinding.FragmentRedemptionPartnerServiceBinding
import com.onelink.nrlp.android.features.faqs.adapter.RVAdapter
import com.onelink.nrlp.android.features.redeem.adapter.RedemServiceAdapter
import com.onelink.nrlp.android.features.redeem.model.RedeemCategoryModel
import com.onelink.nrlp.android.features.redeem.model.RedeemPartnerModel
import com.onelink.nrlp.android.features.redeem.viewmodels.RedeemSharedViewModel
import com.onelink.nrlp.android.features.redeem.viewmodels.RedemptionFragmentPartnerServiceViewModel
import com.onelink.nrlp.android.utils.*
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.ly_loyalty_points_balance.view.*
import javax.inject.Inject

const val REDEMPTION_CREATE_DIALOG = 4003
const val TAG_REDEMPTION_CREATE_DIALOG = "redemption_create_dialog"
const val NOT_ENOUGH_POINTS_DIALOG = 5001
const val TAG_NOT_ENOUGH_POINTS_DIALOG = "not_enough_points_dialog"

class RedemptionPartnerServiceFragment :
    BaseFragment<RedemptionFragmentPartnerServiceViewModel, FragmentRedemptionPartnerServiceBinding>(
        RedemptionFragmentPartnerServiceViewModel::class.java
    ), RedemServiceAdapter.OnItemClickListener,
    OneLinkAlertDialogsFragment.OneLinkAlertDialogListeners {

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var adapter: RVAdapter

    private var redeemSharedViewModel: RedeemSharedViewModel? = null

    private lateinit var redeemPartnerModel: RedeemPartnerModel

    private lateinit var redeemCategoryModel: RedeemCategoryModel

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() = RedemptionPartnerServiceFragment()
    }

    override fun getViewM(): RedemptionFragmentPartnerServiceViewModel =
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(RedemptionFragmentPartnerServiceViewModel::class.java)

    override fun getLayoutRes() = R.layout.fragment_redemption_partner_service
    override fun getTitle(): String = resources.getString(R.string.redeem)
    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        UserData.getUser()?.let {
            viewModel.loyaltyPointBalance.value = it.loyaltyPoints?.toBigInteger()
            binding.lyLoyaltyPointsBalance.tvPoints.text =
                it.loyaltyPoints?.roundOff()?.toFormattedAmount()
            context?.let { context ->
                binding.lyLoyaltyPointsBalance.ivHomeBgLoyaltyCard.setLoyaltyCardBackground(
                    context, it.loyaltyLevel
                )
            }
        }
        activity?.let {
            redeemSharedViewModel = ViewModelProvider(it).get(RedeemSharedViewModel::class.java)
        }
        initObservers()
    }

    private fun initObservers() {
        redeemSharedViewModel?.redeemPartnerModel?.observe(this,
            Observer {
                redeemPartnerModel = it
                viewModel.partnerId.value = redeemPartnerModel.id
                val redeemCategoryModels: List<RedeemCategoryModel> = redeemPartnerModel.categories
                val string = String.format(
                    getString(R.string.redeem_partner_detail), redeemPartnerModel.partnerName
                )
                binding.tvRedeemPartner.text = string
                binding.rvStatementsRedem.setHasFixedSize(true)
                binding.rvStatementsRedem.adapter =
                    RedemServiceAdapter(context, redeemCategoryModels, this)
            })

        viewModel.observeInitializeRedemption().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        redeemSharedViewModel?.setRedeemCategoryModel(redeemCategoryModel)
                        redeemSharedViewModel?.setTransactionId(it.transactionId)
                        fragmentHelper.addFragment(
                            RedeemOtpAuthentication.newInstance(),
                            clearBackStack = false,
                            addToBackStack = true
                        )
                    }

                }
                Status.ERROR -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.error?.let {
                        showGeneralErrorDialog(this, it)
                    }
                }
                Status.LOADING -> {
                    oneLinkProgressDialog.showProgressDialog(activity)
                }
            }
        })
    }

    private fun showRedeemCreatedDialog(str: Spanned) {
        val oneLinkAlertDialogsFragment = OneLinkAlertDialogsFragment.newInstance(
            false,
            R.drawable.ic_redem_dialog,
            getString(R.string.redem_points),
            str,
            "",
            positiveButtonText = "Confirm",
            negativeButtonText = "Cancel"
        )
        oneLinkAlertDialogsFragment.setTargetFragment(
            this,
            REDEMPTION_CREATE_DIALOG
        )
        oneLinkAlertDialogsFragment.show(parentFragmentManager, TAG_REDEMPTION_CREATE_DIALOG)
    }

    private fun showNotEnoughPointsDialog() {
        val oneLinkAlertDialogsFragment = OneLinkAlertDialogsFragment.newInstance(
            true,
            R.drawable.ic_error_dialog,
            getString(R.string.oh_snap),
            (getString(R.string.not_enough_points_loyalty_points_msg)).toSpanned(),
            positiveButtonText = resources.getString(R.string.yes),
            negativeButtonText = resources.getString(R.string.no),
            neutralButtonText = getString(R.string.okay)
        )
        oneLinkAlertDialogsFragment.setTargetFragment(
            this,
            NOT_ENOUGH_POINTS_DIALOG
        )
        oneLinkAlertDialogsFragment.show(parentFragmentManager, TAG_NOT_ENOUGH_POINTS_DIALOG)
        oneLinkAlertDialogsFragment.isCancelable = false
    }

    override fun onItemClicked(redeemCategoryModel: RedeemCategoryModel) {
        this.redeemCategoryModel = redeemCategoryModel
        viewModel.points.value = redeemCategoryModel.points.toBigInteger()
        viewModel.categoryName.value = redeemCategoryModel.categoryName
        viewModel.categoryId.value = redeemCategoryModel.id
        viewModel.partnerName.value = redeemPartnerModel.partnerName
        val s = String.format(
            getString(R.string.redem_confirm_detail),
            viewModel.points.value,
            viewModel.categoryName.value,
            viewModel.partnerName.value
        )
        val str = SpannableStringBuilder(s)
        str.setSpan(
            StyleSpan(Typeface.BOLD),
            s.indexOf(viewModel.points.value.toString()),
            s.indexOf(viewModel.points.value.toString()) + viewModel.points.value.toString().length +
                    resources.getString(R.string.points_simple).length + 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        str.setSpan(
            StyleSpan(Typeface.BOLD), s.indexOf(viewModel.categoryName.value.toString()),
            s.indexOf(viewModel.categoryName.value.toString()) + viewModel.categoryName.value.toString().length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        str.setSpan(
            StyleSpan(Typeface.BOLD), s.indexOf(viewModel.partnerName.value.toString()),
            s.indexOf(viewModel.partnerName.value.toString()) + viewModel.partnerName.value.toString().length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        if (viewModel.isValidTransaction(
                viewModel.points.value!!,
                viewModel.loyaltyPointBalance.value!!
            )
        ) showRedeemCreatedDialog(str.toHtmlString().parseHtml())
        else showNotEnoughPointsDialog()
    }

    override fun onPositiveButtonClicked(targetCode: Int) {
        super.onPositiveButtonClicked(targetCode)
        when (targetCode) {
            REDEMPTION_CREATE_DIALOG -> {
                viewModel.makeInitializeRedemptionCall()
            }
        }
    }

}