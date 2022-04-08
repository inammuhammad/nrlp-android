package com.onelink.nrlp.android.features.redeem.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import androidx.lifecycle.Observer
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.databinding.FragmentRedemptionSlicPolicyBinding
import com.onelink.nrlp.android.features.redeem.model.RedeemCategoryModel
import com.onelink.nrlp.android.features.redeem.model.RedeemPartnerModel
import com.onelink.nrlp.android.features.redeem.view.RedeemSuccessActivity
import com.onelink.nrlp.android.features.redeem.viewmodels.RedeemSharedViewModel
import com.onelink.nrlp.android.features.redeem.viewmodels.RedemptionSLICPolicyViewModel
import com.onelink.nrlp.android.features.selfAwardPoints.view.SelfAwardPointsSuccessActivity
import com.onelink.nrlp.android.utils.*
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertAmountDialogFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.ly_loyalty_points_balance.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class RedemptionSLICPolicyFragment : BaseFragment<RedemptionSLICPolicyViewModel,FragmentRedemptionSlicPolicyBinding>
    (RedemptionSLICPolicyViewModel::class.java) , OneLinkAlertAmountDialogFragment.OneLinkAlertDialogListeners, OneLinkAlertDialogsFragment.OneLinkAlertDialogListeners{

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var redeemSharedViewModel: RedeemSharedViewModel? = null

    private lateinit var redeemCategoryModel: RedeemCategoryModel

    private lateinit var redeemPartnerModel: RedeemPartnerModel

    private lateinit var points: String

    private var redeemablePKR: Double = 1.1

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() = RedemptionSLICPolicyFragment()
    }

    override fun getTitle(): String = resources.getString(R.string.redeem)
    override fun getLayoutRes() = R.layout.fragment_redemption_slic_policy
    override fun getViewM(): RedemptionSLICPolicyViewModel =
        ViewModelProvider(this,viewModelFactory).get(RedemptionSLICPolicyViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        UserData.getUser()?.let {
            redeemablePKR = it.redeemable_pkr!!.toDouble()
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
            val intent = context?.let { it1 ->
                RedeemSuccessActivity.newRedeemSuccessIntent(
                    it1
                )
            }
            intent?.putExtra(
                IntentConstants.TRANSACTION_ID, "454"
            )
            intent?.putExtra(
                IntentConstants.PARTNER_NAME, "45"
            )
            intent?.putExtra(
                IntentConstants.REDEEM_POINTS,
                123456789123456789.toBigInteger()
            )
            intent?.putExtra(
                IntentConstants.PSID, "12"
            )
            startActivity(intent)
            activity?.finish()
            /*if (viewModel.checkVoucherValidation(binding.etPolicy.text.toString())) {
                binding.tilVoucher.clearError()
                viewModel.makeInitializeRedemptionCall(redeemPartnerModel.partnerName,
                    redeemPartnerModel.partnerName,
                    redeemCategoryModel.categoryName,
                    binding.etPolicy.text.toString())
            } else {
                binding.tilVoucher.error = "Please enter valid Policy No"
            }*/
        }
        binding.btnNegative.setOnClickListener {
            fragmentHelper.onBack()
        }

        initObservers()
    }

    private fun initObservers () {
       redeemSharedViewModel?.redeemCategoryModel?.observe(this, Observer {
           redeemCategoryModel = it
       })

        redeemSharedViewModel?.redeemPartnerModel?.observe(this,
           Observer {
                redeemPartnerModel = it
            })

        viewModel.observeInitializeRedemption().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        redeemSharedViewModel?.setTransactionId(it.transactionId)
                        val apiDate = SimpleDateFormat("dd/MM/yyyy").parse(it.billInquiryResponse.dueDate.getParse())
                        if (Date().after(apiDate)) {
                            points = it.billInquiryResponse.amountAfterDueDate
                        }
                        else {
                            points = it.billInquiryResponse.amountWithinDueDate
                        }

                        val voucherNo = binding.etPolicy.text.toString()
                        val partner = redeemPartnerModel.partnerName
                        val s = String.format(
                            getString(R.string.policy_no),
                            voucherNo,
                            points,
                            partner
                        )
                        val str = SpannableStringBuilder(s)
                        str.setSpan(
                            StyleSpan(Typeface.BOLD),
                            s.indexOf(voucherNo),
                            s.indexOf(voucherNo) + voucherNo.length ,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        str.setSpan(
                            StyleSpan(Typeface.BOLD), s.indexOf(points.toString()),
                            s.indexOf(points.toString()) + points.toString().length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        str.setSpan(
                            StyleSpan(Typeface.BOLD), s.indexOf(partner),
                            s.indexOf(partner) + partner.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        if(redeemCategoryModel.categoryName.contains("loan", true))
                            showRedeemAmountCreatedDialog(str.toHtmlString().parseHtml())
                        else
                            showRedeemCreatedDialog(str.toHtmlString().parseHtml())
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
                        redeemSharedViewModel?.setPSID(binding.etPolicy.text.toString())
                        redeemSharedViewModel?.setAmount(points)
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

    private fun showRedeemAmountCreatedDialog(str: Spanned) {
        val oneLinkAlertDialogsFragment = OneLinkAlertAmountDialogFragment.newInstance(
            false,
            R.drawable.ic_redem_dialog,
            getString(R.string.redem_points),
            str,
            redeemCategoryModel.categoryName,
            positiveButtonText = "Confirm",
            negativeButtonText = "Cancel"
        )
        oneLinkAlertDialogsFragment.setTargetFragment(
            this,
            REDEMPTION_CREATE_DIALOG
        )
        oneLinkAlertDialogsFragment.show(parentFragmentManager, TAG_REDEMPTION_CREATE_DIALOG)
    }

    private fun showRedeemCreatedDialog(str: Spanned) {
        val oneLinkAlertDialogsFragment = OneLinkAlertDialogsFragment.newInstance(
            false,
            R.drawable.ic_redem_dialog,
            getString(R.string.redem_points),
            str,
            redeemCategoryModel.categoryName,
            positiveButtonText = "Confirm",
            negativeButtonText = "Cancel"
        )
        oneLinkAlertDialogsFragment.setTargetFragment(
            this,
            REDEMPTION_AMOUNT_DIALOG
        )
        oneLinkAlertDialogsFragment.show(parentFragmentManager, TAG_REDEMPTION_AMOUNT_DIALOG)
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

    override fun onPositiveButtonClicked(targetCode: Int) {
        super.onPositiveButtonClicked(targetCode)
        when (targetCode) {
            REDEMPTION_CREATE_DIALOG -> {

                if(true
                    //viewModel.compareRedeemAmount(redeemablePKR,points.toDouble())
                ) {
                    viewModel.makeInitializeRedemptionOTPCall(redeemPartnerModel.partnerName,
                        redeemPartnerModel.partnerName,
                        redeemCategoryModel.categoryName,
                        binding.etPolicy.text.toString(),
                        points,
                        "1"
                    )
                }
                else {
                   showNotEnoughPointsDialog()
                }
            }
        }
    }

    override fun onPositiveButtonClicked(targetCode: Int, amountEntered: Int) {
        super.onPositiveButtonClicked(targetCode)
        when (targetCode) {
            REDEMPTION_AMOUNT_DIALOG -> {
                if(viewModel.checkAmountValidation(points.toInt(), amountEntered)) {
                    if(true
                        //viewModel.compareRedeemAmount(redeemablePKR,amountEntered.toDouble())
                    ) {
                        points = amountEntered.toString()
                        binding.tilVoucher.clearError()
                        viewModel.makeInitializeRedemptionOTPCall(
                            redeemPartnerModel.partnerName,
                            redeemPartnerModel.partnerName,
                            redeemCategoryModel.categoryName,
                            binding.etPolicy.text.toString(),
                            amountEntered.toString(),
                            "1"
                        )
                    }
                    else {
                        showNotEnoughPointsDialog()
                    }

                } else {
                    Toast.makeText(requireContext(),"Please enter valid amount",Toast.LENGTH_SHORT).show()
                    oneLinkProgressDialog.hideProgressDialog()
                }
            }
        }
    }

    override fun onNegativeButtonClicked(targetCode: Int) {
        super.onNegativeButtonClicked(targetCode)
        oneLinkProgressDialog.hideProgressDialog()
    }

}