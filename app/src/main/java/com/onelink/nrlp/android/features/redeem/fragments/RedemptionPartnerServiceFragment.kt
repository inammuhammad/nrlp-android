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
import com.onelink.nrlp.android.features.redeem.view.RedeemSuccessActivity
import com.onelink.nrlp.android.features.redeem.viewmodels.RedeemSharedViewModel
import com.onelink.nrlp.android.features.redeem.viewmodels.RedemptionFragmentPartnerServiceViewModel
import com.onelink.nrlp.android.utils.*
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDGIPDialogFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.ly_loyalty_points_balance.view.*
import javax.inject.Inject

const val REDEMPTION_CREATE_DIALOG = 4003
const val TAG_REDEMPTION_CREATE_DIALOG = "redemption_create_dialog"
const val NOT_ENOUGH_POINTS_DIALOG = 5001
const val TAG_NOT_ENOUGH_POINTS_DIALOG = "not_enough_points_dialog"
const val REDEMPTION_DETAIL_DIALOG = 4004
const val TAG_REDEMPTION_DETAIL_DIALOG = "redemption_detail_dialog"

class RedemptionPartnerServiceFragment :
    BaseFragment<RedemptionFragmentPartnerServiceViewModel, FragmentRedemptionPartnerServiceBinding>(
        RedemptionFragmentPartnerServiceViewModel::class.java
    ), RedemServiceAdapter.OnItemClickListener,
    OneLinkAlertDialogsFragment.OneLinkAlertDialogListeners,
    OneLinkAlertDGIPDialogFragment.OneLinkAlertDialogListeners {

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var adapter: RVAdapter

    private var redeemSharedViewModel: RedeemSharedViewModel? = null

    private lateinit var redeemPartnerModel: RedeemPartnerModel

    private lateinit var redeemCategoryModel: RedeemCategoryModel

    private lateinit var strMsg: Spanned

    private lateinit var cnicEntered: String
    private lateinit var mobileNoEntered: String
    private lateinit var emailEntered:String

    private var redeemablePKR: Double = 1.1

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
            redeemablePKR = it.redeemable_pkr!!.toDouble()
            viewModel.loyaltyPointBalance.value = it.loyaltyPoints?.toBigInteger()
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
                    RedemServiceAdapter(
                        context, redeemCategoryModels,
                        this, redeemPartnerModel.partnerName
                    )
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

        viewModel.observeInitializeRedemptionOTP().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        redeemSharedViewModel?.setTransactionId(it.transactionId)
                        redeemSharedViewModel?.setAmount(redeemCategoryModel.points.toString())
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

    private fun showRedeemDGIPDialog(str: Spanned) {
        val oneLinkAlertDialogsFragment = OneLinkAlertDGIPDialogFragment.newInstance(
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
            REDEMPTION_DETAIL_DIALOG
        )
        oneLinkAlertDialogsFragment.show(parentFragmentManager, TAG_REDEMPTION_DETAIL_DIALOG)
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
        redeemSharedViewModel?.setRedeemCategoryModel(redeemCategoryModel)
        viewModel.points.value = redeemCategoryModel.points.toBigInteger()
        viewModel.categoryName.value = redeemCategoryModel.categoryName
        viewModel.categoryId.value = redeemCategoryModel.id
        viewModel.partnerName.value = redeemPartnerModel.partnerName
        /* val s = String.format(
            getString(R.string.redem_confirm_detail),
            viewModel.points.value,
            viewModel.categoryName.value,
            viewModel.partnerName.value
        )*/
        val s = String.format(
            getString(R.string.redem_dgip_confirm_detail),
            viewModel.points.value,
            viewModel.categoryName.value,
            viewModel.partnerName.value
        )
        val str = SpannableStringBuilder(s)
        str.setSpan(
            StyleSpan(Typeface.BOLD),
            s.indexOf(viewModel.points.value.toString()),
            s.indexOf(viewModel.points.value.toString()) + viewModel.points.value.toString().length,
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
        /* if (viewModel.isValidTransaction(
                viewModel.points.value!!,
                viewModel.loyaltyPointBalance.value!!
            )
        ) showRedeemCreatedDialog(str.toHtmlString().parseHtml())
        else showNotEnoughPointsDialog()*/
        strMsg = str.toHtmlString().parseHtml()
        if (redeemPartnerModel.partnerName == "Passport") {
            showRedeemDGIPDialog(str.toHtmlString().parseHtml())
        }
        else if(redeemPartnerModel.partnerName == "PIA") {
            viewModel.addPIADescriptionFragment(fragmentHelper)
        }
        else if(redeemPartnerModel.partnerName == "NADRA") {
            viewModel.addNADRADescriptionFragment(fragmentHelper)
        }
        else if(redeemPartnerModel.partnerName == "USC") {
            viewModel.addUSCDescriptionFragment(fragmentHelper)
        }
        else if(redeemPartnerModel.partnerName == "OPF") {
            viewModel.addOPFVoucherFragment(fragmentHelper)
        }
        else if (redeemPartnerModel.partnerName == "SLIC") {
            viewModel.addSLICPolicyFragment(fragmentHelper)
        }
        else if(redeemPartnerModel.partnerName == "BEOE") {
            viewModel.addBEOECNICFragment(fragmentHelper)
        }
    }

    override fun onPositiveButtonClicked(targetCode: Int) {
        super.onPositiveButtonClicked(targetCode)
        when (targetCode) {
            REDEMPTION_CREATE_DIALOG -> {
               // viewModel.makeInitializeRedemptionCall()
                if(true/*viewModel.compareRedeemAmount(redeemablePKR,redeemCategoryModel.points.toDouble())*/) {
                    viewModel.makeInitializeRedemptionOTPCall(
                        redeemPartnerModel.partnerName,
                        redeemPartnerModel.partnerName,
                        redeemCategoryModel.categoryName.replace("/","_"),
                        cnicEntered.cleanNicNumber(),
                        mobileNoEntered,
                        emailEntered,
                        "1",
                        redeemCategoryModel.points.toString()
                    )
                }
                else {
                    showNotEnoughPointsDialog()
                }



                /*val intent = RedeemSuccessActivity.newRedeemSuccessIntent(requireContext())
                intent.putExtra(
                    IntentConstants.TRANSACTION_ID, "1234567"
                )
                intent.putExtra(
                    IntentConstants.PARTNER_NAME,viewModel.partnerName.value.toString()
                )
                intent.putExtra(
                    IntentConstants.REDEEM_POINTS,(viewModel.points.value)
                )
                startActivity(intent)
                requireActivity().finish()*/
            }
           /* REDEMPTION_DETAIL_DIALOG -> {
                showRedeemCreatedDialog(strMsg)
            }*/
        }
    }

    override fun onConfirmButtonCLicked(targetCode: Int,cnic: String, mobileNo: String, email: String) {
        when(targetCode) {
            REDEMPTION_DETAIL_DIALOG -> {
                cnicEntered = cnic
                mobileNoEntered = mobileNo
                emailEntered = email
                showRedeemCreatedDialog(strMsg)
            }
        }
    }

}