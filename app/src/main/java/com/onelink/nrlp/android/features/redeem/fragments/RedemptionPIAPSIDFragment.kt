package com.onelink.nrlp.android.features.redeem.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.databinding.FragmentRedemptionPiaPsidBinding
import com.onelink.nrlp.android.features.redeem.model.RedeemCategoryModel
import com.onelink.nrlp.android.features.redeem.model.RedeemPartnerModel
import com.onelink.nrlp.android.features.redeem.view.RedeemSuccessActivity
import com.onelink.nrlp.android.features.redeem.viewmodels.RedeemSharedViewModel
import com.onelink.nrlp.android.features.redeem.viewmodels.RedemptionPIAPSIDViewModel
import com.onelink.nrlp.android.utils.*
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertAmountDialogFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.ly_loyalty_points_balance.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

const val REDEMPTION_AMOUNT_DIALOG = 4003
const val TAG_REDEMPTION_AMOUNT_DIALOG = "redemption_amount_dialog"

class RedemptionPIAPSIDFragment : BaseFragment<RedemptionPIAPSIDViewModel,FragmentRedemptionPiaPsidBinding>
    (RedemptionPIAPSIDViewModel::class.java), OneLinkAlertDialogsFragment.OneLinkAlertDialogListeners {

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var redeemSharedViewModel: RedeemSharedViewModel? = null

    private lateinit var redeemPartnerModel: RedeemPartnerModel

    private lateinit var redeemCategoryModel: RedeemCategoryModel

    private lateinit var amount: String

    private var redeemablePKR: Double = 1.1

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() = RedemptionPIAPSIDFragment()
    }

    override fun getTitle(): String = resources.getString(R.string.redeem)
    override fun getLayoutRes() = R.layout.fragment_redemption_pia_psid
    override fun getViewM(): RedemptionPIAPSIDViewModel =
        ViewModelProvider(this,viewModelFactory).get(RedemptionPIAPSIDViewModel::class.java)

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
            if (viewModel.checkPSIDValidation(binding.etPSID.text.toString())) {
                binding.tilPSID.clearError()
                viewModel.makeInitializeRedemptionCall(redeemPartnerModel.partnerName,
                    redeemPartnerModel.partnerName,
                    "",
                    binding.etPSID.text.toString())
            } else {
                binding.tilPSID.error = "Please enter valid PSID"
            }
        }
        binding.btnNegative.setOnClickListener {
            fragmentHelper.onBack()
        }

        initObservers()
    }

    private fun initObservers() {
        redeemSharedViewModel?.redeemCategoryModel?.observe(this, Observer {
            redeemCategoryModel = it
        })

        redeemSharedViewModel?.redeemPartnerModel?.observe(this,
            Observer {
                redeemPartnerModel = it
            })

        viewModel.observeInitializeRedemption().observe(this, Observer { response ->
            when(response.status) {
                Status.SUCCESS -> {
                    response.data?.let {
                        redeemSharedViewModel?.setTransactionId(it.transactionId)
                        val apiDate =
                            SimpleDateFormat("dd/MM/yyyy").parse(it.billInquiryResponse.dueDate.getParse())
                        if (Date().after(apiDate)) {
                            amount = it.billInquiryResponse.amountAfterDueDate
                        } else {
                            amount = it.billInquiryResponse.amountWithinDueDate
                        }
                        val psid = binding.etPSID.text.toString()
                        val points = amount
                        val partner = redeemPartnerModel.partnerName
                        val s = String.format(
                            getString(R.string.psid),
                            psid,
                            points,
                            partner
                        )
                        val str = SpannableStringBuilder(s)
                        str.setSpan(
                            StyleSpan(Typeface.BOLD),
                            s.indexOf(psid),
                            s.indexOf(psid) + psid.length ,
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
                        showRedeemAmountDialog(str.toHtmlString().parseHtml())

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
                        redeemSharedViewModel?.setPSID(binding.etPSID.text.toString())
                        redeemSharedViewModel?.setAmount(amount)
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

    private fun showRedeemAmountDialog(str: Spanned) {
        //val oneLinkAlertDialogsFragment = OneLinkAlertAmountDialogFragment.newInstance(
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
            REDEMPTION_AMOUNT_DIALOG -> {
                if(viewModel.compareRedeemAmount(redeemablePKR, amount.toDouble())) {
                    viewModel.makeInitializeRedemptionOTPCall(
                        redeemPartnerModel.partnerName,
                        redeemPartnerModel.partnerName,
                        "",
                        binding.etPSID.text.toString(),
                        amount,
                        "1"
                    )
                }
                else
                    showNotEnoughPointsDialog()
            }
        }
    }

    /*override fun onPositiveButtonClicked(targetCode: Int,amountEntered: Int) {
        super.onPositiveButtonClicked(targetCode)
        when (targetCode) {
            REDEMPTION_AMOUNT_DIALOG -> {
                if(viewModel.checkAmountValidation(amount.toInt(),amountEntered)) {
                    if(viewModel.compareRedeemAmount(redeemablePKR,amountEntered.toDouble())) {
                        amount = amountEntered.toString()
                        binding.tilPSID.clearError()
                        viewModel.makeInitializeRedemptionOTPCall(
                        redeemPartnerModel.partnerName,
                        redeemPartnerModel.partnerName,
                        "",
                        binding.etPSID.text.toString(),
                        amountEntered.toString(),
                        "1"
                    )
                } else {
                        showNotEnoughPointsDialog()
                    }
            } else {
                    Toast.makeText(requireContext(),"Please enter valid amount",Toast.LENGTH_SHORT).show()
                    oneLinkProgressDialog.hideProgressDialog()
            }
            }
        }
    }*/


    override fun onNegativeButtonClicked(targetCode: Int) {
        super.onNegativeButtonClicked(targetCode)
        oneLinkProgressDialog.hideProgressDialog()
    }
}