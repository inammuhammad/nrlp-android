package com.onelink.nrlp.android.features.redeem.fragments

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.text.*
import android.text.style.StyleSpan
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.databinding.FragmentRedemptionNadraTrackingIdBinding
import com.onelink.nrlp.android.features.login.helper.CnicTextHelper
import com.onelink.nrlp.android.features.redeem.model.RedeemCategoryModel
import com.onelink.nrlp.android.features.redeem.model.RedeemPartnerModel
import com.onelink.nrlp.android.features.redeem.view.RedeemSuccessActivity
import com.onelink.nrlp.android.features.redeem.viewmodels.RedeemSharedViewModel
import com.onelink.nrlp.android.features.redeem.viewmodels.RedemptionNadraTrackingIDViewModel
import com.onelink.nrlp.android.utils.*
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.ly_loyalty_points_balance.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject

class RedemptionNadraTrackingIDFragment : BaseFragment<RedemptionNadraTrackingIDViewModel,FragmentRedemptionNadraTrackingIdBinding>
       (RedemptionNadraTrackingIDViewModel::class.java) , OneLinkAlertDialogsFragment.OneLinkAlertDialogListeners {

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var redeemSharedViewModel: RedeemSharedViewModel? = null

    private lateinit var redeemCategoryModel: RedeemCategoryModel

    private lateinit var redeemPartnerModel: RedeemPartnerModel

    private lateinit var amount: String

    private var redeemablePKR: Double = 1.1

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() = RedemptionNadraTrackingIDFragment()
    }

    override fun getTitle(): String = resources.getString(R.string.redeem)
    override fun getLayoutRes() = R.layout.fragment_redemption_nadra_tracking_id
    override fun getViewM(): RedemptionNadraTrackingIDViewModel =
        ViewModelProvider(this,viewModelFactory).get(RedemptionNadraTrackingIDViewModel::class.java)

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
            if (viewModel.checkTrackingNoValidation(binding.etTrackingNo.text.toString())) {
                binding.tilTrackingNo.clearError()
                if(viewModel.checkCNICNoValidation(binding.etCnic.text.toString())) {
                    binding.tilCNIC.clearError()
                    viewModel.makeInitializeRedemptionCall(redeemPartnerModel.partnerName,
                        redeemPartnerModel.partnerName,
                        "",
                        binding.etTrackingNo.text.toString(),
                        binding.etCnic.text.toString()
                    )
                }
                else {
                    binding.tilCNIC.error = "Please enter valid CNIC"
                }
            } else {
                binding.tilTrackingNo.error = "Please enter valid Tracking No"
            }
        }
        binding.btnNegative.setOnClickListener {
            fragmentHelper.onBack()
        }

        initTextWatchers()
        initObservers()
    }

    private fun initTextWatchers() {
        binding.etCnic.addTextChangedListener(object : TextWatcher {
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                val regex1 = "^\\d{13,}$"
                val regex2 = "^\\d{5}-\\d{8,}$"
                val regex3 = "^[0-9-]{15}$"
                val regex4 = "^\\d{5}-\\d{7}-\\d$"
                val regex5 = "^\\d{12}-\\d"
                val inputString = s.toString()
                if (Pattern.matches(regex1, inputString)) {
                    binding.etCnic.setText(
                        inputString.substring(0, 5) + "-" + inputString.substring(5, 12)
                                +
                                inputString.substring(12, 13)
                    )
                    binding.etCnic.setSelection(15)
                } else if (Pattern.matches(regex2, inputString)) {
                    binding.etCnic.setText(
                        inputString.substring(0, 13) + "-" + inputString.substring(
                            13,
                            14
                        )
                    )
                    binding.etCnic.setSelection(15)
                } else if (Pattern.matches(regex3, inputString) && !Pattern.matches(
                        regex4,
                        inputString
                    )
                ) {
                    val newS = inputString.replace("-".toRegex(), "")
                    binding.etCnic.setText(
                        newS.substring(0, 5) + "-" + newS.substring(
                            5,
                            12
                        ) + newS.substring(12, 13)
                    )

                    Selection.setSelection(binding.etCnic.text, 15)
                } else if (Pattern.matches(regex5, inputString)) {
                    binding.etCnic.setText(
                        inputString.substring(
                            0,
                            5
                        ) + "-" + inputString.substring(5)
                    )
                    binding.etCnic.setSelection(inputString.length + 1)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                binding.etCnic.removeTextChangedListener(this)
                val inputString = s.toString()
                val editTextEditable: Editable? = binding.etCnic.text
                val editTextString = editTextEditable.toString()
                if (inputString.isNotEmpty() && editTextString.isNotEmpty() && before < count) {
                    val regex1 = "^\\d{5}$"
                    val regex2 = "^\\d{5}-\\d{7}$"
                    val regex3 = "^\\d{5,12}$"
                    when {
                        Pattern.matches(regex1, inputString) ||
                                Pattern.matches(regex2, inputString) -> {
                            binding.etCnic.setText("$inputString-")
                            binding.etCnic.setSelection(inputString.length + 1)
                        }

                        Pattern.matches(regex3, inputString) -> {
                            binding.etCnic.setText(
                                inputString.substring(
                                    0,
                                    5
                                ) + "-" + inputString.substring(5)
                            )
                            binding.etCnic.setSelection(inputString.length + 1)
                        }
                    }
                }
                binding.etCnic.addTextChangedListener(this)
            }
        })

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
                        val trackingId = binding.etTrackingNo.text.toString()
                        val partner = redeemPartnerModel.partnerName
                        val s = String.format(
                            getString(R.string.tracking),
                            trackingId,
                            amount,
                            partner
                        )
                        val str = SpannableStringBuilder(s)
                        str.setSpan(
                            StyleSpan(Typeface.BOLD),
                            s.indexOf(trackingId),
                            s.indexOf(trackingId) + trackingId.length ,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        str.setSpan(
                            StyleSpan(Typeface.BOLD), s.indexOf(amount.toString()),
                            s.indexOf(amount.toString()) + amount.toString().length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        str.setSpan(
                            StyleSpan(Typeface.BOLD), s.indexOf(partner),
                            s.indexOf(partner) + partner.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
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
                        redeemSharedViewModel?.setPSID(binding.etTrackingNo.text.toString())
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

    override fun onPositiveButtonClicked(targetCode: Int) {
        super.onPositiveButtonClicked(targetCode)
        when (targetCode) {
            REDEMPTION_CREATE_DIALOG -> {
                if(true
                    //viewModel.compareRedeemAmount(redeemablePKR,amount.toDouble())
                ){
                    viewModel.makeInitializeRedemptionOTPCall(redeemPartnerModel.partnerName,
                        redeemPartnerModel.partnerName,
                        "",
                        binding.etTrackingNo.text.toString(),
                        binding.etCnic.text.toString(),
                        amount,
                        "1"
                    )
                }
                else
                {
                    showNotEnoughPointsDialog()
                }
            }
        }
    }

    override fun onNegativeButtonClicked(targetCode: Int) {
        super.onNegativeButtonClicked(targetCode)
        oneLinkProgressDialog.hideProgressDialog()
    }

}