package com.onelink.nrlp.android.features.redeem.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.databinding.FragmentRedeemOtpAuthenticationBinding
import com.onelink.nrlp.android.features.redeem.model.RedeemCategoryModel
import com.onelink.nrlp.android.features.redeem.model.RedeemPartnerModel
import com.onelink.nrlp.android.features.redeem.view.RedeemSuccessActivity
import com.onelink.nrlp.android.features.redeem.viewmodels.RedeemOtpFragmentViewModel
import com.onelink.nrlp.android.features.redeem.viewmodels.RedeemSharedViewModel
import com.onelink.nrlp.android.utils.CountDownTimerCanBePause
import com.onelink.nrlp.android.utils.IntentConstants
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import com.onelink.nrlp.android.utils.formattedCountDownTimer
import com.onelink.nrlp.android.utils.setOnSingleClickListener
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


const val TIMER_MILLIS = 60000L
const val FIVE_MINUTE_TIMER_MILLIS = 5 * 60 * 1000L
const val TIMER_INTERVAL = 1000L
const val RESEND_OTP_CARD_VISIBILITY_TIMER = 10 * 1000L

class RedeemOtpAuthentication :
    BaseFragment<RedeemOtpFragmentViewModel, FragmentRedeemOtpAuthenticationBinding>(
        RedeemOtpFragmentViewModel::class.java
    ) {

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var resentAttempts: Int = 0

    private var redeemSharedViewModel: RedeemSharedViewModel? = null

    private lateinit var redeemPartnerModel: RedeemPartnerModel

    private lateinit var redeemCategoryModel: RedeemCategoryModel

    private val timer = object : CountDownTimer(TIMER_MILLIS, TIMER_INTERVAL) {
        override fun onTick(millisUntilFinished: Long) {
            binding.textViewResendOTP.isEnabled = false
        }

        override fun onFinish() {
            //if (resentAttempts < RETRIES_COUNT) {
            binding.textViewResendOTP.isEnabled = true
            resentAttempts++
            //}
        }
    }

    private val fiveMinuteTimer = object : CountDownTimerCanBePause(
        FIVE_MINUTE_TIMER_MILLIS, TIMER_INTERVAL
    ) {
        @SuppressLint("DefaultLocale")
        override fun onTick(millisUntilFinished: Long) {
            try {
                binding.textViewTimer.text = formattedCountDownTimer(millisUntilFinished)
            } catch (e: Exception) {
                cancel()
            }
        }

        override fun onFinish() {
            binding.textViewTimer.text = getString(R.string.timer_default)
        }
    }


    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getLayoutRes() = R.layout.fragment_redeem_otp_authentication

    override fun getViewM(): RedeemOtpFragmentViewModel = ViewModelProvider(
        this, viewModelFactory
    ).get(RedeemOtpFragmentViewModel::class.java)

    override fun getTitle(): String = resources.getString(R.string.otp_verification_title)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        activity?.let {
            redeemSharedViewModel = ViewModelProvider(it).get(RedeemSharedViewModel::class.java)
        }
        fiveMinuteTimer.start()
        timer.start()
        initObservers()
        initListeners()

        UserData.getUser()?.let {
            val str = String.format(
                getString(R.string.otp_sent_phone_number_msg),
                it.mobileNo.replace("+", "")
            )
            binding.tvOtpSentMsg.text = str
        }
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
        binding.etOTP1.requestFocus()
        showKeyboard()
        initEditTextListeners()

        binding.btnNext.setOnSingleClickListener {
            viewModel.makeVerifyOtpCall()
        }

        binding.textViewResendOTP.setOnSingleClickListener {
            viewModel.verifyRedeemResendOTP()
        }
    }

    private fun initEditTextListeners() {
        binding.etOTP1.setOnKeyListener(onKeyListenerOTP(binding.etOTP1, binding.etOTP1))
        binding.etOTP2.setOnKeyListener(onKeyListenerOTP(binding.etOTP2, binding.etOTP1))
        binding.etOTP3.setOnKeyListener(onKeyListenerOTP(binding.etOTP3, binding.etOTP2))
        binding.etOTP4.setOnKeyListener(onKeyListenerOTP(binding.etOTP4, binding.etOTP3))
    }

    private fun onKeyListenerOTP(etCurrent: EditText, etPrevious: EditText): View.OnKeyListener {
        return View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL
                && event.action == KeyEvent.ACTION_DOWN
                && etCurrent.hasFocus()
            ) {
                if (etCurrent.text.toString().length == 1) {
                    etCurrent.setText("")
                    etCurrent.requestFocus()
                } else if (etCurrent.text.toString().isEmpty()) {
                    etPrevious.setText("")
                    etPrevious.requestFocus()
                }
                return@OnKeyListener true
            }
            false
        }
    }

    private fun initObservers() {
        redeemSharedViewModel?.redeemPartnerModel?.observe(this,
            Observer {
                redeemPartnerModel = it
            })

        redeemSharedViewModel?.redeemCategoryModel?.observe(this, Observer {
            redeemCategoryModel = it
        })

        viewModel.observeRedeemOTP().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                       /* fragmentHelper.addFragment(
                            RedeemAgentConfirmationFragment.newInstance(),
                            clearBackStack = false,
                            addToBackStack = true
                        )*/
                        viewModel.makeCompleteRedemptionCall()

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

        viewModel.observeRedeemResendOTP().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        showNewOTPSentCard()
                        timer.start()
                        fiveMinuteTimer.reset()
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

        redeemSharedViewModel?.transactionId?.observe(this, Observer {
            viewModel.transactionId.value = it
        })

        viewModel.observeRedeemSuccess().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let { response ->
                        activity?.let {
                           /* if (redeemPartnerModel.partnerName == "NADRA" ||
                                redeemPartnerModel.partnerName == "Passport" ||
                                redeemPartnerModel.partnerName == "OPF" ||
                                redeemPartnerModel.partnerName == "SLIC" ||
                                redeemPartnerModel.partnerName == "BEOE") {
                                val intent = RedeemSuccessActivity.newRedeemSuccessIntent(it)
                                intent.putExtra(
                                    IntentConstants.TRANSACTION_ID, viewModel.transactionId.value
                                )
                                intent.putExtra(
                                    IntentConstants.PARTNER_NAME, redeemPartnerModel.partnerName
                                )
                                intent.putExtra(
                                    IntentConstants.REDEEM_POINTS,
                                    redeemCategoryModel.points.toBigInteger()
                                )
                                intent.putExtra(
                                    IntentConstants.PSID, (redeemSharedViewModel?.psid?.value)
                                )
                                startActivity(intent)
                                it.finish()
                            } else {*/
                                val intent = RedeemSuccessActivity.newRedeemSuccessIntent(it)
                                intent.putExtra(
                                    IntentConstants.TRANSACTION_ID, viewModel.transactionId.value
                                )
                                intent.putExtra(
                                    IntentConstants.PARTNER_NAME, redeemPartnerModel.partnerName
                                )
                                intent.putExtra(
                                    IntentConstants.REDEEM_POINTS,
                                    redeemSharedViewModel?.amount?.value?.toBigInteger()
                                )
                                intent.putExtra(
                                    IntentConstants.PSID, (redeemSharedViewModel?.psid?.value)
                                )
                                intent.putExtra(
                                    IntentConstants.AUTH_ID, response.authId
                                )
                                startActivity(intent)
                                it.finish()
                            }
                        }
                    /*}*/
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

        viewModel.validEditText1.observe(this, Observer {
            if (it) binding.etOTP2.requestFocus()
        })
        viewModel.validEditText2.observe(this, Observer {
            if (it) binding.etOTP3.requestFocus()
        })
        viewModel.validEditText3.observe(this, Observer {
            if (it) binding.etOTP4.requestFocus()
        })
        viewModel.validEditText4.observe(this, Observer {
            if (it) hideKeyboard()
        })
    }

    private fun showNewOTPSentCard() {
        binding.tvSentNewOtpMsg.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed(
            {
                binding.tvSentNewOtpMsg.visibility = View.GONE
            },
            RESEND_OTP_CARD_VISIBILITY_TIMER
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
        fiveMinuteTimer.cancel()
    }

    companion object {
        @JvmStatic
        fun newInstance() = RedeemOtpAuthentication()
    }
}