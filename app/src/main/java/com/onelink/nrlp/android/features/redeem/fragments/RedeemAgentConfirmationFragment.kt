package com.onelink.nrlp.android.features.redeem.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.FragmentAgentConfirmationBinding
import com.onelink.nrlp.android.features.redeem.model.RedeemCategoryModel
import com.onelink.nrlp.android.features.redeem.model.RedeemPartnerModel
import com.onelink.nrlp.android.features.redeem.view.RedeemSuccessActivity
import com.onelink.nrlp.android.features.redeem.viewmodels.RedeemAgentConfirmationViewModel
import com.onelink.nrlp.android.features.redeem.viewmodels.RedeemSharedViewModel
import com.onelink.nrlp.android.utils.*
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

const val TIME_EXPIRED_DIALOG = 5000
const val TAG_TIME_EXPIRED_DIALOG = "time_expired_dialog"
const val FIVE_MINUTE_TIMER_AGENT = 5 * 60 * 1000L

class RedeemAgentConfirmationFragment :
    BaseFragment<RedeemAgentConfirmationViewModel, FragmentAgentConfirmationBinding>(
        RedeemAgentConfirmationViewModel::class.java
    ), OneLinkAlertDialogsFragment.OneLinkAlertDialogListeners {

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var redeemSharedViewModel: RedeemSharedViewModel? = null

    private lateinit var redeemPartnerModel: RedeemPartnerModel

    private lateinit var redeemCategoryModel: RedeemCategoryModel

    private val fiveMinuteTimer =
        object : CountDownTimerCanBePause(FIVE_MINUTE_TIMER_AGENT, TIMER_INTERVAL) {
            @SuppressLint("DefaultLocale")
            override fun onTick(millisUntilFinished: Long) {
                activity?.let {
                    if (!it.isFinishing) binding.textViewTimer.text =
                        formattedCountDownTimer(millisUntilFinished)
                    else cancel()
                }
            }

            override fun onFinish() {
                hideKeyboard()
                activity?.let {
                    if (!it.isFinishing) showTimeExpiredDialog()
                }
            }
        }

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getLayoutRes() = R.layout.fragment_agent_confirmation

    override fun getViewM(): RedeemAgentConfirmationViewModel =
        ViewModelProvider(this, viewModelFactory).get(RedeemAgentConfirmationViewModel::class.java)

    override fun getTitle(): String = resources.getString(R.string.redeem)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        activity?.let {
            redeemSharedViewModel = ViewModelProvider(it).get(RedeemSharedViewModel::class.java)
        }

        fiveMinuteTimer.start()
        binding.eTAgentCode.requestFocus()
        initObservers()
        initListeners()
    }

    override fun onResume() {
        super.onResume()
        fiveMinuteTimer.start()
    }

    override fun onPause() {
        super.onPause()
        fiveMinuteTimer.pause()
    }

    private fun initListeners() {
        showKeyboard()

        binding.btnNext.setOnSingleClickListener {
            if (viewModel.validationsPassed()) viewModel.makeCompleteRedemptionCall()
        }
    }


    private fun initObservers() {
        viewModel.isAgentCodeValidationPassed.observe(this, Observer { validationsPassed ->
            run {
                if (!validationsPassed) binding.tilAgentCode.error =
                    resources.getString(R.string.error_agent_code_invalid)
                else {
                    binding.tilAgentCode.clearError()
                    binding.tilAgentCode.isErrorEnabled = false
                }
            }
        })

        redeemSharedViewModel?.transactionId?.observe(this, Observer {
            viewModel.transactionId.value = it
        })

        redeemSharedViewModel?.redeemPartnerModel?.observe(this, Observer {
            redeemPartnerModel = it
        })

        redeemSharedViewModel?.redeemCategoryModel?.observe(this, Observer {
            redeemCategoryModel = it
        })

        viewModel.observeRedeemSuccess().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        activity?.let {
                            val intent = RedeemSuccessActivity.newRedeemSuccessIntent(it)
                            intent.putExtra(
                                IntentConstants.TRANSACTION_ID, viewModel.transactionId.value
                            )
                            intent.putExtra(
                                IntentConstants.PARTNER_NAME, redeemPartnerModel.partnerName
                            )
                            intent.putExtra(
                                IntentConstants.REDEEM_POINTS, redeemCategoryModel.points
                            )
                            startActivity(intent)
                            it.finish()
                        }
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

    private fun showTimeExpiredDialog() {
        val oneLinkAlertDialogsFragment = OneLinkAlertDialogsFragment.newInstance(
            true,
            R.drawable.ic_error_dialog,
            getString(R.string.oh_snap),
            (getString(R.string.agent_time_expired)).toSpanned(),
            positiveButtonText = resources.getString(R.string.yes),
            negativeButtonText = resources.getString(R.string.no),
            neutralButtonText = getString(R.string.okay)
        )
        oneLinkAlertDialogsFragment.setTargetFragment(
            this, TIME_EXPIRED_DIALOG
        )
        oneLinkAlertDialogsFragment.show(parentFragmentManager, TAG_TIME_EXPIRED_DIALOG)
        oneLinkAlertDialogsFragment.isCancelable = false
    }

    companion object {
        @JvmStatic
        fun newInstance() = RedeemAgentConfirmationFragment()
    }

    private fun finishActivity() {
        activity?.finish()
    }

    override fun onNeutralButtonClicked(targetCode: Int) {
        super.onNeutralButtonClicked(targetCode)
        when (targetCode) {
            TIME_EXPIRED_DIALOG -> finishActivity()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fiveMinuteTimer.cancel()
    }
}